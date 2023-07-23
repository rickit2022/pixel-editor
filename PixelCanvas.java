import java.awt.*;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

public class PixelCanvas extends JPanel implements MouseInputListener {
    private int width;
    private int height;
    private CanvasSquare[][] grid;
    private int startX;
    private int startY;
    private boolean isMovingX;
    private final SquareColour defaultDark = new SquareColour(60, 60, 60);
    private final SquareColour defaultLight = new SquareColour(80, 80, 80);

    /**
     * Represents the Canvas in the pixel editor UI, which is composed of
     * CanvasSquare objects
     * 
     * @param width  The width of the canvas in CanvasSquares
     * @param height The height of the canvas in CanvasSquares
     */
    public PixelCanvas(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new CanvasSquare[width][height];
        this.setLayout(new GridLayout(width, height));

        setDefaultCanvasSquares();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Gets the current state of the grid, iterates through each pixel and converts
     * the colour to
     * a single int with red | blue | green in each 2 byte position.
     * 
     * @return 1D grid of pixels with colours represented as int
     */
    public byte[] serializeCanvas() {
        byte[] serializedData = new byte[width * height * 3];
        int index = 0;

        for (CanvasSquare[] column : grid) {
            for (CanvasSquare pixel : column) {
                serializedData[index + 0] = (byte) pixel.getColour().red;
                serializedData[index + 1] = (byte) pixel.getColour().green;
                serializedData[index + 2] = (byte) pixel.getColour().blue;

                index += 3;
            }
        }

        return serializedData;
    }

    /**
     * Sets the default colours of the canvas squares when the canvas is initalised
     */
    public void setDefaultCanvasSquares() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = new CanvasSquare(
                        (i + j) % 2 == 0 ? defaultLight : defaultDark,
                        i, j);
            }
        }
    }

    /**
     * Also set the colour to default background, but doesn't create a new grid,
     * only clears the only specified
     */
    public void resetToDefault() {
    }

    /**
     * Updates the current view of the canvas
     * 
     * @param g The graphics object associated with the canvas, get with
     *          getGraphics()
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        SquareColour colour;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colour = grid[i][j].getColour();
                g.setColor(colour);
                g.fillRect(i * 10, j * 10, 10, 10);
            }
        }
    }

    /**
     * Takes a pixel coordinate on the canvas, for example from a click event, and
     * returns the
     * CanvasPixel clicked on.
     * 
     * @param x The x-coordinate of the screen pixel on the canvas
     * @param y The y-coordinate of the screen pixel on the canvas
     * @return The CanvasSquare clicked on
     */
    private CanvasSquare convertCoordToSquare(int x, int y) {
        int xSquare = x / 10;
        int ySquare = y / 10;
        return grid[xSquare][ySquare];
    };

    /**
     * Get height of the canvas in CanvasSquares
     * 
     * @return Height in squares
     */
    public int getCanvasWidth() {
        return this.width;
    }

    /**
     * Get width of the canvas in CanvasSquares
     * 
     * @return Width in squares
     */
    public int getCanvasHeight() {
        return this.height;
    }

    public void setCanvasWidth(int width) {
        this.width = width;
    }

    public void setCanvasHeight(int height) {
        this.height = height;
    }

    public void resizeCanvas(int width, int height) {
        if(width < 0) {
            width *=-1;
        }
        if(height < 0) {
            height *=-1;
        }
        this.setCanvasWidth(width);
        this.setCanvasHeight(height);


        this.setGrid(new CanvasSquare[width][height]);
        this.setLayout(new GridLayout(width, height));
        this.setDefaultCanvasSquares();

        AppManager.previousCanvasStates.clear();
        this.revalidate();
        this.repaint();
    }

    public CanvasSquare[][] getGrid() {
        return this.grid;
    }

    public void setGrid(CanvasSquare[][] grid) {
        this.grid = grid;
    }

    /**
     * Saves the canvas and current tool settings to the specified file. Will apply
     * run-length
     * encoding compression if specified.
     * 
     * @param file     The file to save to
     * @param compress True if the file should use RLE compression
     */
    public void save(File file) {
        byte[] canvasData = this.serializeCanvas();

        // get size of canvas and tool size as bytes
        byte canvasWidthBytes = Integer.valueOf(Window.canvas.getCanvasWidth()).byteValue();
        byte canvasHeightBytes = Integer.valueOf(Window.canvas.getCanvasHeight()).byteValue();
        byte toolThickness = Integer.valueOf(AppManager.toolThickness).byteValue();

        // write the data to the file
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(canvasWidthBytes);
            stream.write(canvasHeightBytes);
            stream.write(toolThickness);
            stream.write(canvasData);

            stream.close();
        } catch (IOException exception) {
            System.out.println("Could not save file.");
            exception.printStackTrace();
        }
    }

    /**
     * Rotates the canvas 90 degrees anticlockwise or flips the canvas
     * @param rotate true if the canvas is to be rotated, false if it is to be flipped
     */
    public void rotate(boolean rotate) {
        AppManager.previousCanvasStates.push(Window.canvas.serializeCanvas());
        SquareColour[][] flippedGrid = new SquareColour[width][height];
        final int M = grid.length;
        //rotates the canvas grid by 90 degrees
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(rotate) {
                    flippedGrid[j][M-1-i] = grid[i][j].getColour();  
                }
                else {
                    flippedGrid[M-1-i][j] = grid[i][j].getColour();
                }
            }
        }
        Window.canvas.setDefaultCanvasSquares();
        //sets the new grid colours to the flipped grid colours
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                SquareColour flippedCol = flippedGrid[i][j];
                if(flippedCol.compare(defaultDark) || flippedCol.compare(defaultLight)) {
                    continue;
                }
                grid[i][j].setColour(flippedCol);
            }
        }
        this.repaint();
    }

    /**
     * Gets the square on the canvas at the given coordinate
     * 
     * @param x The x-coord of the square
     * @param y The y-coord of the square
     * @return The square at that coordinate, or `null` if invalid coordinate
     */
    public CanvasSquare getSquare(int x, int y) {
        try {
            return grid[x][y];
        }

        catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public SquareColour getDefaultLight() {
        return defaultLight;
    }

    public SquareColour getDefaultDark() {
        return defaultDark;
    }

    /**
     * Takes the array of pixel data and uploads it to the canvas, restoring it.
     * 
     * @param pixelBuffer Buffer of pixel data to upload to the canvas
     */
    public void openCanvas(byte[] pixelBuffer) {
        Window.canvas.setDefaultCanvasSquares();
        int canvasWidth = Window.canvas.getCanvasWidth();
        for (int i = 0; i < pixelBuffer.length; i += 3) {
            SquareColour colour = new SquareColour(
                    Byte.toUnsignedInt(pixelBuffer[i]),
                    Byte.toUnsignedInt(pixelBuffer[i + 1]),
                    Byte.toUnsignedInt(pixelBuffer[i + 2]));

            CanvasSquare square = Window.canvas.getSquare(
                    (i / 3) / canvasWidth,
                    (i / 3) % canvasWidth);
            square.setColour(colour);
        }
        Window.canvas.repaint();
    }

    public void mouseClicked(MouseEvent event) {
        try {
            if (AppManager.currentTool == AppManager.CIRCLE_TOOL){
                return;
            }
            else if(AppManager.currentTool == null) {
                AppManager.SNIPPING.selectedSquare = convertCoordToSquare(event.getX(), event.getY());
                return;
            }
            AppManager.currentTool.action(this, convertCoordToSquare(event.getX(), event.getY()));
        } catch (ArrayIndexOutOfBoundsException e) {}
    }

    public void mousePressed(MouseEvent event) { // when mouse pressed, store the coordinates of the current X and Y
                                                 // values, as well as the canvas state
        try {
            AppManager.previousCanvasStates.push(this.serializeCanvas()); //stores the canvas state before change for undo/redo functions
        } catch(StackOverflowError e) {AppManager.previousCanvasStates.remove(0);}
        
        if(AppManager.SNIPPING.canvasBeforeRectangle != null) {  //MAKE THIS SO THAT IT CLEARS WHEN OPENED
            Window.canvas.openCanvas(AppManager.SNIPPING.canvasBeforeRectangle);
            AppManager.SNIPPING.canvasBeforeRectangle = null;
        }
        startX = event.getX();
        startY = event.getY();
        // Handles tools that can be assigned to both the left and right mouse
        if (event.getButton() == MouseEvent.BUTTON2 && AppManager.currentTool == AppManager.SOLID_PEN) {
            AppManager.currentTool = AppManager.ERASER;
            Window.canvas.setCursor(Toolbar.getEraserCursor());
        } else if (event.getButton() == MouseEvent.BUTTON2 && AppManager.currentTool == AppManager.ERASER) {
            AppManager.currentTool = AppManager.SOLID_PEN;
            Window.canvas.setCursor(Toolbar.getPenCursor());

        }

        if (event.getButton() == MouseEvent.BUTTON3 && AppManager.currentTool == AppManager.LIGHTEN) {
            AppManager.currentTool = AppManager.DARKEN;
        } else if (event.getButton() == MouseEvent.BUTTON1 && AppManager.currentTool == AppManager.DARKEN) {
            AppManager.currentTool = AppManager.LIGHTEN;
        }
        // if the middle mouse button is pressed with the symmetry tool, the canvas will
        // display the line of symmetry
        if (event.getButton() == MouseEvent.BUTTON2 && AppManager.currentTool == AppManager.SYMMETRY) {
            SymmetryLine.canvasWithoutLine = Window.canvas.serializeCanvas();
            if (AppManager.SYMMETRY.islineOfSymmetryVertical) {
                SymmetryLine.drawVerticalLine(SymmetryLine.xVal);
            } else {
                SymmetryLine.drawHorizontalLine(SymmetryLine.yVal);
            }
        }
        if (AppManager.currentTool == AppManager.CIRCLE_TOOL || AppManager.currentTool == AppManager.RECTANGLE_TOOL) {
            // get starting point of where the mouse is first pressed
            AppManager.currentTool.action(this, convertCoordToSquare(startX, startY), ToolState.PRESSED);
        }
        if(AppManager.currentTool == null) {
            AppManager.SNIPPING.action(this, convertCoordToSquare(startX, startY), ToolState.PRESSED);
        }

    }

    /**
     * When the mouse is dragged normally, all squares that it passes over are
     * coloured.
     * If the shift key is being held, the line drawn will snap to the closest axis.
     */
    public void mouseDragged(MouseEvent event) {
        try {
            if(AppManager.currentTool == null) {
                AppManager.SNIPPING.action(this, convertCoordToSquare(event.getX(), event.getY()), ToolState.DRAGGED);
                return;
            }
            if (AppManager.currentTool == AppManager.BUCKET || AppManager.currentTool == AppManager.SYMMETRY_LINE || AppManager.currentTool == AppManager.COLOUR_PICKER) {
                return;
            } else if (AppManager.currentTool == AppManager.CIRCLE_TOOL
                    || AppManager.currentTool == AppManager.RECTANGLE_TOOL) {
                // dragging
                AppManager.currentTool.action(this, convertCoordToSquare(event.getX(), event.getY()),
                        ToolState.DRAGGED);
                return;
            }
            if (event.getX() != startX && (event.getY() < startY + 5 && event.getY() > startY - 5)) {
                isMovingX = true;
            } // if x axis is closer, snap to x
            if (event.getY() != startY && (event.getX() < startX + 5 && event.getX() > startX - 5)) {
                isMovingX = false;
            } // if y axis is closer, snap to y

            if (isMovingX && event.isShiftDown()) { // if shift key held while moving diagonally, mouse will snap to the
                                                    // x axis
                AppManager.currentTool.action(this, convertCoordToSquare(event.getX(), startY));
            } else if (!isMovingX && event.isShiftDown()) { // if shift key held while moving vertically, mouse will
                                                            // snap to the y axis
                AppManager.currentTool.action(this, convertCoordToSquare(startX, event.getY()));
            } else { // if the shift key is not being held, colour all squares that the cursor passes
                     // over
                AppManager.currentTool.action(this, convertCoordToSquare(event.getX(), event.getY()));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
    }

    public void mouseReleased(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON2 && AppManager.currentTool == AppManager.SYMMETRY) {
            Window.canvas.openCanvas(SymmetryLine.canvasWithoutLine);
        }
        // reset the start/end points
        if (AppManager.currentTool == AppManager.CIRCLE_TOOL || AppManager.currentTool == AppManager.RECTANGLE_TOOL) {
            // end
            AppManager.currentTool.action(this, convertCoordToSquare(event.getX(), event.getY()), ToolState.RELEASED);
        }
        if(AppManager.currentTool == null) {
            AppManager.SNIPPING.action(this, convertCoordToSquare(event.getX(), event.getY()), ToolState.RELEASED);
        }
    }
    
    public void mouseMoved(MouseEvent event) {}  
    public void mouseEntered(MouseEvent event) {}
    public void mouseExited(MouseEvent event) {}
}
