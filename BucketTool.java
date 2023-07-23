/**
 * Fills a bounded area to the current selected colour using a FloodFill recursive algorithm.
 */
public class BucketTool implements Tool {
    private String name;
    private CanvasSquare[][] grid;
    private CanvasSquare originSquare;
    private SquareColour originColour;
    private int defaultDarkRGB;
    private int defaultLightRGB;

    public BucketTool(String name) {
        this.name = name;
    }
    
    @Override
    public void action(PixelCanvas canvas, CanvasSquare square) {
        grid = canvas.getGrid();
        this.originSquare = square;
        this.originColour = originSquare.getColour();
        floodFill(canvas, square.getX(), square.getY());
        canvas.repaint();
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Checks the colour of the square clicked on, and then calls the relevant flood fill method
     * If the square clicked on is a default colour, then call the floodFillFromDefault method
     * If the square clicked on isn't a default colour, then call the floodFillFromColoured method
     */
    private void floodFill(PixelCanvas canvas, int x, int y) {
        CanvasSquare currentSquare = grid[x][y];

        int currentRGB = currentSquare.getColour().getRGB();
        defaultLightRGB = canvas.getDefaultLight().getRGB();
        defaultDarkRGB = canvas.getDefaultDark().getRGB();

        if(currentRGB == defaultDarkRGB|| currentRGB == defaultLightRGB)
        {
            floodFillFromDefault(canvas, x, y);
        }
        else {
            floodFillFromColoured(canvas, x, y);
        }
    }

    /**
     * Colours all squares in a bounded area that are default colours
     * Uses FloodFill recursive algorithm
     * @param canvas the Pixel Canvas in use
     * @param x the x coordinate of the current square being filled
     * @param y the y coordinate of the current square being filled
     */
    private void floodFillFromDefault(PixelCanvas canvas, int x, int y)
    {
        canvas.repaint();
        try {
            CanvasSquare currentSquare = grid[x][y];
            int currentRGB = currentSquare.getColour().getRGB();
            if(currentRGB != defaultDarkRGB && currentRGB != defaultLightRGB) {return;}
            if(isBoundary(canvas,currentSquare)) {
                currentSquare.setColour(AppManager.primaryColour);
                grid[0][0].setColour(AppManager.primaryColour);
                return;
            } 
                
            currentSquare.setColour(AppManager.primaryColour);
            floodFillFromDefault(canvas,x-1,y);
            floodFillFromDefault(canvas,x+1,y);
            floodFillFromDefault(canvas,x,y-1);
            floodFillFromDefault(canvas,x,y+1);
        } catch(ArrayIndexOutOfBoundsException | StackOverflowError e) {}
        return;
    }

    /**
     * Colours all squares in a bounded area that are already coloured
     * Uses FloodFill recursive algorithm
     * @param canvas the Pixel Canvas in use
     * @param x the x coordinate of the current square being filled
     * @param y the y coordinate of the current square being filled
     */
    private void floodFillFromColoured(PixelCanvas canvas, int x, int y)
    {
        canvas.repaint();
        try {
            CanvasSquare currentSquare = grid[x][y];
            int currentRGB = currentSquare.getColour().getRGB();
            //if the colour of the square being checked is not the same as the colour of the square first clicked on, then dont colour this square
            if(currentRGB != originColour.getRGB()) {return;} 
            if(isBoundary(canvas,currentSquare)) {
                currentSquare.setColour(AppManager.primaryColour);
                grid[0][0].setColour(AppManager.primaryColour);
                return;
            }

            currentSquare.setColour(AppManager.primaryColour);
            floodFillFromColoured(canvas,x-1,y);
            floodFillFromColoured(canvas,x+1,y);
            floodFillFromColoured(canvas,x,y-1);
            floodFillFromColoured(canvas,x,y+1); 
        } catch(ArrayIndexOutOfBoundsException | StackOverflowError e) {}
        return;
    }

    /**
     * Checks a square to see if it borders the canvas
     * @param canvas the PixelCanvas
     * @param square the square being checked
     * @return true if the square is on the boundary of the canvas, and false if not
     */
    private boolean isBoundary(PixelCanvas canvas, CanvasSquare square) {
        int width = grid.length;
        int height = grid[0].length;
        if(square.getX() == width || square.getX() == 0) {
            return true;
        }
        if(square.getY() == height || square.getY() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void action(PixelCanvas canvas, CanvasSquare square, ToolState state) {
        // TODO Auto-generated method stub
        
    }
}
