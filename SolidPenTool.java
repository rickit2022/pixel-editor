public class SolidPenTool extends PenTool {
   
    /**
     * Sets the name of the tool, in this case should be 'pen'
     * @param name The name of the tool
     */
    public SolidPenTool(String name) {
        this.name = name;
    }


    /**
     * Executes the action for the solid pen tool, in this case just setting the colour of the
     * square to AppManager.primaryColour. How many squares it changes at a time depends on the thickness.
     * @param canvas The main UI canvas
     * @param square The square the event calling the tool was activated on
     */
    @Override
    public void action(PixelCanvas canvas, CanvasSquare square) {
        int xVal = square.getX();
        int yVal = square.getY();
        CanvasSquare[][] grid = canvas.getGrid();
        
        initialiseThickness();
        if(thickness != Thickness.ONE_PIXELS)
        {
            for (int x = startX; x < endX; x++) {
                for(int y = startY; y < endY; y++)
                {
                    grid[xVal + x][yVal - y].setColour(AppManager.primaryColour);
                }
            }
        }
        else {  //if the thickness is one pixel, just change the square
            square.setColour(AppManager.primaryColour);
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
