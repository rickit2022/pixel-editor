public interface Tool {
    public void action(PixelCanvas canvas, CanvasSquare square);
    /**
     * Method to be used with drag-and-draw tools, allow the class to keep tracks of the mouseInputListener state
     * @param canvas The current canvas of the program
     * @param square The square that is being pressed on
     * @param state The state of the tool
     */
    public void action(PixelCanvas canvas, CanvasSquare square, ToolState state);
    /**
     * Returns the name of the tool
     * @return
     */
    public String getName();
}
