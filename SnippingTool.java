import java.util.ArrayList;

public class SnippingTool extends RectangleTool {
    protected CanvasSquare selectedSquare;
    protected CanvasSquare[][] copiedPixels;
    protected byte[] canvasBeforeRectangle;
    
    public SnippingTool(String name) {
        super(name);
        previousColours = new ArrayList<SquareColour>();
    }

    /**
     * Sets the pixels of the rectangle to alternate between black and white
     * so that they can be viewed on top of any colour
     */
    @Override
    public void setPixels(ArrayList<CanvasSquare> pixels) {
        for (int i = 0; i < pixels.size() - 1; i++) {
            int x = pixels.get(i).getX();
            int y = pixels.get(i).getY();
            if (x >= 0 && x < currentCoords.length && y >= 0 && y < currentCoords[0].length) {
                previousColours.add(pixels.get(i).getColour());
                if(i % 2 == 0) {
                    pixels.get(i).setColour(new SquareColour(0,0,0));
                }
                else {
                    pixels.get(i).setColour(new SquareColour(150,150,150));
                }
            }
        }
    }

    /**
     * Cuts all the pixels within the selected area, setting them to their default colour
     */
    public void cutPixels() {
        CanvasSquare[][] grid = Window.canvas.getGrid();
        for(int x = startX; x < endX + 1; x++) {
            for(int y = startY; y < endY + 1; y++) {
                grid[x][y].setColour((x + y) % 2 == 0 ? Window.canvas.getDefaultLight() : Window.canvas.getDefaultDark());
            }
        }
        canvasBeforeRectangle = null;
        Window.canvas.repaint();
    }

    /**
     * Copies all of the pixels within the selected area
     */
    public void copyPixels() {
        CanvasSquare[][] grid = Window.canvas.getGrid();
        copiedPixels = new CanvasSquare[(endX - 1) - (startX)][(endY - 1) - (startY)];
        for(int x = startX + 1; x < endX; x++) {
            for(int y = startY + 1; y < endY; y++) {
                copiedPixels[(x-1) - startX][(y-1) - startY] = grid[x][y];
            }
        }
        Window.canvas.openCanvas(canvasBeforeRectangle);
        canvasBeforeRectangle = null;
        Window.canvas.repaint();
    }

    /**
     * Pastes all of the copied pixels around the point that the user has selected
     */
    public void pastePixels() {
        if(selectedSquare == null) {return;}  //if no square is selected then dont paste
        CanvasSquare[][] grid = Window.canvas.getGrid();
        int selectedX = selectedSquare.getX();
        int selectedY = selectedSquare.getY();
        for(int x = 0; x < copiedPixels.length; x++) {
            for(int y = 0; y < copiedPixels[0].length; y++) {
                if(copiedPixels[x][y].getColour().compare(Window.canvas.getDefaultDark()) || copiedPixels[x][y].getColour().compare(Window.canvas.getDefaultLight())) {
                    continue;   
                }
                //pastes the pixels around the selected square
                try {
                    grid[(selectedX - copiedPixels.length/2) + x][(selectedY - copiedPixels[0].length/2) + y].setColour(copiedPixels[x][y].getColour());
                } catch (IndexOutOfBoundsException e) {continue;}
            }
        }
        canvasBeforeRectangle = null;
        Window.canvas.repaint();
    }
    
    @Override
    public void action(PixelCanvas canvas, CanvasSquare square, ToolState state) {
        switch(state){
            case PRESSED:
                canvasBeforeRectangle = Window.canvas.serializeCanvas();
                startX = square.getX();
                startY = square.getY();

                currentCoords = canvas.getGrid();
                oldCoords.add(square);
                canvas.repaint();
                break;

            case DRAGGED:
                endX = square.getX();
                endY = square.getY();

                drawRectangle(startX, startY, endX, endY);
                canvas.repaint();
                break;

            case RELEASED:
                allCoords.addAll(oldCoords);
                reset();
                break;
        }
    }
}

