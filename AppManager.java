import java.util.Stack;

public final class AppManager {
    // The tools the user may use
    public static final SolidPenTool SOLID_PEN = new SolidPenTool("pen");
    public static final EraserPenTool ERASER = new EraserPenTool("eraser");
    public static final CircleTool CIRCLE_TOOL = new CircleTool();
    public static final RectangleTool RECTANGLE_TOOL = new RectangleTool("rectangle");
    public static final LightenTool LIGHTEN = new LightenTool("lighten");
    public static final DarkenTool DARKEN = new DarkenTool("darken");
    public static final BucketTool BUCKET = new BucketTool("bucket");
    public static final SymmetryTool SYMMETRY = new SymmetryTool("symmetry");
    public static final SymmetryLine SYMMETRY_LINE = new SymmetryLine("line of symmetry");
    public static final SnippingTool SNIPPING = new SnippingTool("snipping");
    public static final ColourPickerTool COLOUR_PICKER = new ColourPickerTool("colour picker");

    // tracking the current state of the application
    public static SquareColour primaryColour = new SquareColour(0, 0, 0);
    public static SquareColour secondaryColour = new SquareColour(255, 255, 255);
    public static Tool currentTool = null;
    public static int toolThickness = 1;

    private static String filepath = null;

    public static Stack<byte[]> previousCanvasStates = new Stack<byte[]>(); //stack of previous canvas states for undo and redo functions
    public static Stack<byte[]> redoStack = new Stack<byte[]>();            //stack of previous canvas states before user has undone a change
    public static byte[] snapshot;

    /**
     * This method undoes the last change the user has made to the canvas
     * @param canvas The current PixelCanvas
     */
    public static void undo(PixelCanvas canvas) {
        if(previousCanvasStates.size() == 0) {return;}  //if there are no previous states, there is nothing to undo
        byte[] serializedCurrentState = canvas.serializeCanvas(); //serialize the current state of the canvas
        
        try {
            redoStack.push(serializedCurrentState);     
        } catch(StackOverflowError e) {redoStack.remove(0);}
        
        canvas.openCanvas(previousCanvasStates.pop());         //deserialize data and set the canvas to this
    }

    /**
     * This method redoes the last undone change to the canvas
     * @param canvas The current PixelCanvas
     */
    public static void redo(PixelCanvas canvas) {
        if(redoStack.size() == 0) {return;}

        try {
            previousCanvasStates.push(canvas.serializeCanvas());
        } catch(StackOverflowError e) {previousCanvasStates.remove(0);}
        
        canvas.openCanvas(redoStack.pop());         //deserialize data and set the canvas to this
    }

    
    /**
     * Changes the thickness of the tools
     * @param thickness New thickness
     */
    public static void changeThickness(int thickness) {
        AppManager.toolThickness = thickness;
        switch(thickness) {
            case 1:
                AppManager.SOLID_PEN.setThickness(PenTool.Thickness.ONE_PIXELS);
                AppManager.ERASER.setThickness(PenTool.Thickness.ONE_PIXELS);
                AppManager.LIGHTEN.setThickness(PenTool.Thickness.ONE_PIXELS);
                AppManager.DARKEN.setThickness(PenTool.Thickness.ONE_PIXELS);
                AppManager.CIRCLE_TOOL.setThickness(PenTool.Thickness.ONE_PIXELS);
                AppManager.RECTANGLE_TOOL.setThickness(PenTool.Thickness.ONE_PIXELS);
                break;
            case 2:
                AppManager.SOLID_PEN.setThickness(PenTool.Thickness.FOUR_PIXELS);
                AppManager.ERASER.setThickness(PenTool.Thickness.FOUR_PIXELS);
                AppManager.LIGHTEN.setThickness(PenTool.Thickness.FOUR_PIXELS);
                AppManager.DARKEN.setThickness(PenTool.Thickness.FOUR_PIXELS);
                AppManager.CIRCLE_TOOL.setThickness(PenTool.Thickness.FOUR_PIXELS);
                AppManager.RECTANGLE_TOOL.setThickness(PenTool.Thickness.FOUR_PIXELS);
                break;
            case 3:
                AppManager.SOLID_PEN.setThickness(PenTool.Thickness.NINE_PIXELS);
                AppManager.ERASER.setThickness(PenTool.Thickness.NINE_PIXELS);
                AppManager.LIGHTEN.setThickness(PenTool.Thickness.NINE_PIXELS);
                AppManager.DARKEN.setThickness(PenTool.Thickness.NINE_PIXELS);
                AppManager.CIRCLE_TOOL.setThickness(PenTool.Thickness.NINE_PIXELS);
                AppManager.RECTANGLE_TOOL.setThickness(PenTool.Thickness.NINE_PIXELS);
                break;
            case 4:
                AppManager.SOLID_PEN.setThickness(PenTool.Thickness.SIXTEEN_PIXELS);
                AppManager.ERASER.setThickness(PenTool.Thickness.SIXTEEN_PIXELS);
                AppManager.LIGHTEN.setThickness(PenTool.Thickness.SIXTEEN_PIXELS);
                AppManager.DARKEN.setThickness(PenTool.Thickness.SIXTEEN_PIXELS);
                AppManager.CIRCLE_TOOL.setThickness(PenTool.Thickness.SIXTEEN_PIXELS);
                AppManager.RECTANGLE_TOOL.setThickness(PenTool.Thickness.SIXTEEN_PIXELS);
                break;
            case 5:
                AppManager.SOLID_PEN.setThickness(PenTool.Thickness.TWENTY_FIVE_PIXELS);
                AppManager.ERASER.setThickness(PenTool.Thickness.TWENTY_FIVE_PIXELS);
                AppManager.LIGHTEN.setThickness(PenTool.Thickness.TWENTY_FIVE_PIXELS);
                AppManager.DARKEN.setThickness(PenTool.Thickness.TWENTY_FIVE_PIXELS);
                AppManager.CIRCLE_TOOL.setThickness(PenTool.Thickness.TWENTY_FIVE_PIXELS);
                AppManager.RECTANGLE_TOOL.setThickness(PenTool.Thickness.TWENTY_FIVE_PIXELS);
                break;
        }
    }
    

    public static String getCurrentPath() {
        return AppManager.filepath;
    }


    /**
     * Sets the title of the pixel editor window and filepath in AppManager to the new filepath
     * @param filepath The new filepath
     */
    public static void setCurrentPath(String filepath) {
        AppManager.filepath = filepath;
    }

    /**
     * Sets the state of the canvas to the state of the snapshot, and vice versa. A quick way to switch
     * between the two.
     */
    public static void switchCanvasAndSnapshot() {

    }
}
