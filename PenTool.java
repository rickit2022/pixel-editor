public abstract class PenTool implements Tool {
    protected String name;
    protected enum Thickness {
        ONE_PIXELS,
        FOUR_PIXELS,
        NINE_PIXELS,
        SIXTEEN_PIXELS,
        TWENTY_FIVE_PIXELS
    }

    protected Thickness thickness;
    protected int startX;
    protected int startY;
    protected int endX;
    protected int endY;

    /**
     * Sets the default line thickness to one pixel
     */
    public PenTool() {
        thickness = Thickness.ONE_PIXELS;
    }

    /**
     * Sets the iterator values depending on the chosen thickness.
     * These iterator values are then used in a loop to simulate line thickness.
     */
    protected void initialiseThickness()
    {
        endX = 2;
        endY = 2;
        
        if(this.thickness == PenTool.Thickness.FOUR_PIXELS)
        {
            startX = 0;
            startY = 0;
        }
        else if(this.thickness == PenTool.Thickness.NINE_PIXELS)
        {
            startX = -1;
            startY = -1;
        }
        else if(this.thickness == PenTool.Thickness.SIXTEEN_PIXELS)
        {
            startX = -2;
            startY = -2;
        }
        else {  //if thickness is equal to twenty five pixels
            startX = -2;
            startY = -2;
            endX = 3;
            endY = 3;
        } 
    }

    public void setThickness(Thickness thickness) 
    {
        this.thickness = thickness;
    }

    public Thickness getThickness(){
        return this.thickness;
    }
}
