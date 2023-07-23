public class ColourPickerTool implements Tool {

    private String name; 

    public ColourPickerTool(String name) {
        this.name = name;
    }
    
    @Override
    public void action(PixelCanvas canvas, CanvasSquare square) {
        SquareColour col = square.getColour();
        if(col.compare(canvas.getDefaultDark()) || col.compare(canvas.getDefaultLight())) {
            return;
        }
        AppManager.primaryColour = square.getColour();
        AppManager.currentTool = AppManager.SOLID_PEN;
        Window.canvas.setCursor(Toolbar.penCursor);
    }

    @Override
    public void action(PixelCanvas canvas, CanvasSquare square, ToolState state) {
        //
    }

    @Override
    public String getName() {
        return name;
    }
}
