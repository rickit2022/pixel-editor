/**
 * Allows the user to set either horizontal or vertical symmetry from a specific point.
 * When the user then draws on the canvas, it will draw on both sides of the line of symmetry.
 */
public class SymmetryTool extends PenTool {
    protected String name;
    protected boolean islineOfSymmetryVertical;

    public SymmetryTool(String name) {
        this.name = name;
        this.islineOfSymmetryVertical = false;  //sets default line of symmetry to be a vertical line
    }

    /**
     * 
     */
    @Override
    public void action(PixelCanvas canvas, CanvasSquare square) {
        AppManager.SOLID_PEN.action(canvas,square);
        if(islineOfSymmetryVertical) { 
            int difference = SymmetryLine.xVal - square.getX();
            CanvasSquare symmetricalSquare = canvas.getGrid()[SymmetryLine.xVal + difference][square.getY()];
            AppManager.SOLID_PEN.action(canvas, symmetricalSquare);
        }
        else {
            int difference = SymmetryLine.yVal - square.getY();
            CanvasSquare symmetricalSquare = canvas.getGrid()[square.getX()][SymmetryLine.yVal + difference];
            AppManager.SOLID_PEN.action(canvas, symmetricalSquare);
        }
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
