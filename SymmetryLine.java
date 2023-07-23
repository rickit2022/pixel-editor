/**
 * Allows the user to select a line of symmetry, which will then be drawn and used to mirror
 * what the user draws on the canvas.
 */
public class SymmetryLine extends PenTool {
    private String name;
    protected static int xVal;
    protected static int yVal;
    protected static byte[] canvasWithoutLine;
    
    public SymmetryLine(String name) {
        this.name = name;
    }

    /**
     * Allows user to set the line of symmetry, and then draws it.
     * @param canvas the current PixelCanvas
     * @param square the square that the user has clicked on to draw the line of symmetry
     */
    @Override
    public void action(PixelCanvas canvas, CanvasSquare square) {
        if(AppManager.SYMMETRY.islineOfSymmetryVertical) {
            xVal = square.getX();
            yVal = 0;
            canvasWithoutLine = canvas.serializeCanvas();
            drawVerticalLine(xVal);
        }
        else {
            yVal = square.getY();
            xVal = 0;
            canvasWithoutLine = canvas.serializeCanvas();
            drawHorizontalLine(yVal);
        }
        canvas.repaint();
        Window.canvas.setCursor(Toolbar.penCursor);
        //shows the symmetry line for half a second before the user can draw, the line of symmetry can then be viewed by holding MB2
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                AppManager.currentTool = AppManager.SYMMETRY;    //once the line has been drawn, set the current tool to be the symmetry draw tool
                canvas.openCanvas(canvasWithoutLine);
            }
        }, 
        500);
    }

    /**
     * Draws a vertical line of symmetry.
     * @param x the x coordinate of the line
     */
    public static void drawVerticalLine(int x) {
        CanvasSquare[][] grid = Window.canvas.getGrid();
        int height = grid[0].length;
        for(int y = 0; y < height; y++) {
            if(y % 2 == 0) {
                grid[x][y].setColour(new SquareColour(0,0,0));
                continue;
            }
            grid[x][y].setColour(new SquareColour(150,150,150));
        }
    }
    
    /**
     * Draws a horizontal line of symmetry.
     * @param y the y coordinate of the line
     */
    public static void drawHorizontalLine(int y) {
        CanvasSquare[][] grid = Window.canvas.getGrid();
        int height = grid[0].length;
        for(int x = 0; x < height; x++) {
            if(x % 2 == 0) {
                grid[x][y].setColour(new SquareColour(0,0,0));
                continue;
            }
            grid[x][y].setColour(new SquareColour(150,150,150));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void action(PixelCanvas canvas, CanvasSquare square, ToolState state) {
        // TODO Auto-generated method stub
        
    }
}
