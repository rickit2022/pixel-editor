import java.util.ArrayList;

public class RectangleTool extends PenTool {
    protected String name;
    protected final SquareColour defaultDark = new SquareColour(60, 60, 60);
    protected final SquareColour defaultLight = new SquareColour(80, 80, 80);
    protected int startX;
    protected int startY;
    protected int endX;
    protected int endY;
    protected CanvasSquare[][] currentCoords;
    protected ArrayList<CanvasSquare> oldCoords;
    protected ArrayList<CanvasSquare> newCoords;
    protected ArrayList<CanvasSquare> allCoords;
    protected ArrayList<SquareColour> previousColours;
    private CanvasSquare lastSquare;
    
    public RectangleTool(String name) {
        this.name = name;
        oldCoords = new ArrayList<>();
        newCoords = new ArrayList<>();
        allCoords = new ArrayList<>();
        previousColours = new ArrayList<SquareColour>();
    }

    public void drawRectangle(int startX, int startY, int endX, int endY) {
        //because the direction where the user drags the tool can messed up the for loop if the end conditions are < or >
        int xDirection = Integer.signum(endX - startX); //0 = neg, 1 = pos
        int yDirection = Integer.signum(endY - startY);

        for (int x = startX; x != endX + xDirection; x += xDirection) {
            if(x == startX || x == endX){
                for (int y = startY; y != endY + yDirection; y += yDirection) {
                    newCoords.add(currentCoords[x][y]);
                }
            } else{
                newCoords.add(currentCoords[x][startY]);
                newCoords.add(currentCoords[x][endY]);
            }
        }
        clearPixels(oldCoords);
        setPixels(newCoords);
        try {
            lastSquare = newCoords.get(newCoords.size()-1);
        } catch(IndexOutOfBoundsException e) {}
        oldCoords = new ArrayList<>(newCoords);
        newCoords.clear();
    }


    /**
     * Set the pixels passed in to the current selected colours
     * @param pixels ArrayList of pixels to be changed
     */
    public void setPixels(ArrayList<CanvasSquare> pixels) {
        for (int i = 0; i < pixels.size() - 1; i++) {
            int x = pixels.get(i).getX();
            int y = pixels.get(i).getY();
            if (x >= 0 && x < currentCoords.length && y >= 0 && y < currentCoords[0].length) {
                previousColours.add(pixels.get(i).getColour());
                pixels.get(i).setColour(AppManager.primaryColour);
            }
        }
    }

    public void clearPixels(ArrayList<CanvasSquare> pixels) {
        for (int i = 0; i < pixels.size() - 1; i++) {
            int x = pixels.get(i).getX();
            int y = pixels.get(i).getY();
            if (x >= 0 && x < currentCoords.length && y >= 0 && y < currentCoords[0].length && !allCoords.contains(pixels.get(i))) {
                pixels.get(i).setColour(previousColours.get(i));
            }
        }
        previousColours.clear();
    }

    @Override
    public void action(PixelCanvas canvas, CanvasSquare square) {}

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void action(PixelCanvas canvas, CanvasSquare square, ToolState state) {
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

                drawRectangle(startX, startY, endX, endY);
                canvas.repaint();
                break;

            case RELEASED:
                allCoords.addAll(oldCoords);
                lastSquare.setColour(AppManager.primaryColour);
                reset();
                canvas.repaint();
                break;
        }
    }

    public void reset(){
        oldCoords.clear();
        newCoords.clear();
    }
}
