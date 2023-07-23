public class LightenDarkenTool extends PenTool {
    private String name;
    private boolean lighten;
    
    public LightenDarkenTool(String name, boolean lighten)
    {
        this.name = name;
        this.lighten = lighten;
    }
    
    /**
     * Only lightens or darkens squares that are not their default colour
     */
    @Override
    public void action(PixelCanvas canvas, CanvasSquare square) {
        int xVal = square.getX();
        int yVal = square.getY();
        SquareColour lightColour = canvas.getDefaultLight();
        SquareColour darkColour = canvas.getDefaultDark();
        CanvasSquare[][] grid = canvas.getGrid();
        
        initialiseThickness();
        if(thickness != Thickness.ONE_PIXELS)
        {
            for (int x = startX; x < endX; x++) {
                for(int y = startY; y < endY; y++)
                {
                    if(grid[xVal + x][yVal - y].getColour() != lightColour && grid[xVal + x][yVal - y].getColour() != darkColour) {
                        grid[xVal + x][yVal - y].lighten(this.lighten);
                    }
                }
            }
        }
        else {  //if the thickness is one pixel, just change the square
            if(square.getColour() != lightColour && square.getColour() != darkColour) {
                square.lighten(this.lighten);
            }
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
