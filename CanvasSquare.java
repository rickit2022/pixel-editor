public class CanvasSquare {
    private SquareColour colour;
    private final int posX;
    private final int posY;


    /**
     * Construct the canvas square with a default colour and an x and y coord
     * @param colour The starting colour of the square
     */
    public CanvasSquare(SquareColour colour, int x, int y) {
        this.colour = colour;
        this.posX = x;
        this.posY = y;
    }


    /** Sets a new colour for the canvas square */
    public void setColour(SquareColour colour) {
        this.colour = colour;
    }

    /**
     * Lightens or darkens a squares RGB values
     * @param b true means the square will be lightened, false means the square will be darkened.
     */
    public void lighten(boolean b) {
        int newRed = this.colour.red;
        int newGreen = this.colour.green;
        int newBlue = this.colour.blue;
        if(b == true) {
            if(newRed < 255) {newRed++;}
            if(newGreen < 255) {newGreen++;}
            if(newBlue < 255) {newBlue++;}
        }
        else {
            if(newRed > 0) {newRed--;}
            if(newGreen > 0) {newGreen--;}
            if(newBlue > 0) {newBlue--;}
        }
        try {
            this.setColour(new SquareColour(newRed, newGreen, newBlue));
        } catch(IllegalArgumentException e) {}
    }

    /**
     * Returns the colour of the square
     * @return The colour of the square
     */
    public SquareColour getColour() {
        return colour;
    }


    /**
     * Returns the coordinates of this pixel as its index in the grid of pixels held by PixelCanvas
     * @return Coordinates in the form { x, y }
     */
    public int[] getCoordsOnCanvas() {
        int[] coords = { posX, posY };
        return coords;
    }    //method doesn't actually work i don't think

    public CanvasSquare getSquare(){
        return this;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }
}
