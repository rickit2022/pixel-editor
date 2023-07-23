public class EraserPenTool extends PenTool {
    public EraserPenTool(String name) {
        this.name = name;
    }


    /**
     * Erases squares back to their original colour.
     * The size of the eraser depends on the thickness of the Pen Tool
     */
    @Override
    public void action(PixelCanvas canvas, CanvasSquare square) {
        int xVal = square.getX();
        int yVal = square.getY();
        SquareColour lightColour = canvas.getDefaultLight();
        SquareColour darkColour = canvas.getDefaultDark();
        CanvasSquare[][] grid = canvas.getGrid();
        initialiseThickness();
        if(thickness != Thickness.ONE_PIXELS)   //if the thickness is larger than one pixel, change the size of the eraser and erase
        {
            for (int x = startX; x < endX; x++) {
                for(int y = startY; y < endY; y++)
                {
                    grid[xVal + x][yVal - y].setColour(((xVal + x) + (yVal - y)) % 2 == 0 ? lightColour : darkColour);
                }
            }
        }
        else {  //if the thickness is one pixel, just change the square
            square.setColour((xVal + yVal) % 2 == 0 ? lightColour : darkColour);
        }
        canvas.repaint();
    }


    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public void action(PixelCanvas canvas, CanvasSquare square, ToolState state) {
        // TODO Auto-generated method stub
        
    }
}
