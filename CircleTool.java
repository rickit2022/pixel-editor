import java.util.ArrayList;

public class CircleTool extends PenTool{
    private String name = "circle";
    private final SquareColour defaultDark = new SquareColour(60, 60, 60);
    private final SquareColour defaultLight = new SquareColour(80, 80, 80);
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private CanvasSquare[][] currentCoords;
    private ArrayList<CanvasSquare> oldCoords;
    private ArrayList<CanvasSquare> newCoords;
    private ArrayList<CanvasSquare> allCoords;

    public CircleTool(){
        oldCoords = new ArrayList<>();
        newCoords = new ArrayList<>();
        allCoords = new ArrayList<>();
    }

    public void drawCircle(ArrayList<CanvasSquare> list, int startX, int startY, int endX, int endY) {
        int xDiff = endX - startX;
        int yDiff = endY - startY;
        int radius = (int)Math.round(Math.sqrt(xDiff * xDiff + yDiff * yDiff));

        int x = radius + 1;
        int y = 0;
        int decisionOver2 = 1 - x;  
        //Bresenham's circle drawing algorithm
        while(y <= x) {
            list.add(currentCoords[x + startX][y + startY]); 
            list.add(currentCoords[y + startX][x + startY]); 
            list.add(currentCoords[-x + startX][y + startY]); 
            list.add(currentCoords[-y + startX][x + startY]); 
            list.add(currentCoords[-x + startX][-y + startY]); 
            list.add(currentCoords[-y + startX][-x + startY]); 
            list.add(currentCoords[x + startX][-y + startY]); 
            list.add(currentCoords[y + startX][-x + startY]); 
            y++;
            if (decisionOver2 <= 0) {
                decisionOver2 += 2 * y + 1;
            } else {
                x--;
                decisionOver2 += 2 * (y - x) + 1;
            }
        }
        clearPixels(oldCoords);
        setPixels(newCoords);
        oldCoords = new ArrayList<>(newCoords);
        newCoords.clear();
    }    


    /**
     * Set the pixels passed in to the current selected colours
     * @param pixels ArrayList of pixels to be changed
     */
    public void setPixels(ArrayList<CanvasSquare> pixels) {
        for (CanvasSquare pixel : pixels) {
            int x = pixel.getX();
            int y = pixel.getY();
            if (x >= 0 && x < currentCoords.length && y >= 0 && y < currentCoords[0].length) {
                pixel.setColour(AppManager.primaryColour);
            }
        }
    }

    public void clearPixels(ArrayList<CanvasSquare> pixels) {
        for (CanvasSquare pixel : pixels) {
            int x = pixel.getX();
            int y = pixel.getY();
            if (x >= 0 && x < currentCoords.length && y >= 0 && y < currentCoords[0].length && !allCoords.contains(pixel)) {
                pixel.setColour((x + y) % 2 == 0 ? defaultLight: defaultDark);
            }
        }
    }

    public void reset(){
        oldCoords.clear();
        newCoords.clear();
    }

    @Override
    public void action(PixelCanvas canvas, CanvasSquare square){}

    /**
     * Called when the tool is drag-and-draw, tracking the 3 states of the mouse action
     */
    @Override
    public void action(PixelCanvas canvas, CanvasSquare square, ToolState state){
        switch(state){
            case PRESSED:
                startX = square.getX();
                startY = square.getY();
                square.setColour(AppManager.primaryColour);

                currentCoords = canvas.getGrid();
                oldCoords.add(square);
                canvas.repaint();
                break;

            case DRAGGED:
                endX = square.getX();
                endY = square.getY();
                //draw the new circle & delete old circle
                drawCircle(newCoords, startX, startY, endX, endY);
                canvas.repaint();
                break;

            case RELEASED:
                //ended dragged
                allCoords.addAll(oldCoords);
                reset();
                break;
        }
    }

    public String getName(){
        return this.name;
    }
}