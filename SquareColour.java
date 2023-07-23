import java.awt.*;
public class SquareColour extends Color {
    protected int red;
    protected int green;
    protected int blue;

    
    /**
     * Represents a generic colour on the Canvas in the default RGB format
     * @param red   amount of red between 0-255
     * @param green amount of green between 0-255
     * @param blue  amount of blue between 0-255
     */
    public SquareColour(int red, int green, int blue)
    {
        super(red, green, blue);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }


    /**
     * Gets the colour formatted as #XXXXXX, where hex digits 0,1 are red, 2,3 are green, 
     * and 4,5 are blue.
     * @return The formatted string
     */
    public String getAsHexStr() {
        return String.format("#%02X%02X%02X", this.red, this.green, this.blue);
    }

    public void setColour(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }


    /** Get the red component of this colour */
    public int getRed() {
        return red;
    }


    /** Get the green component of this colour */
    public int getGreen() {
        return green;
    }


    /** Get the blue component of this colour */
    public int getBlue() {
        return blue;
    }

    /**
     * Compares two square colours
     * @param colour the colour of the square being compared
     * @return true if colours are equal, false if not
     */
    public boolean compare(SquareColour colour) {
        if(this.red == colour.getRed() && this.blue == colour.getBlue() && this.green == colour.getGreen()) {
            return true;
        }
        return false;
    }
}
