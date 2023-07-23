import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.Point;

public class Toolbar extends JPanel implements ChangeListener, MouseInputListener {
    private PixelCanvas canvas;
    private JOptionPane sliderPane;
    private JDialog dialog;
    private JButton cursorBtn;
    private static JButton colourBtn;
    private JButton penBtn;
    private JButton eraserBtn;
    private JButton thicknessBtn;
    private JButton resolutionBtn;
    private JButton clearCanvasBtn;
    private JButton lightenDarkenBtn;
    private JButton symmetryBtn;
    private JButton bucketBtn;
    private JButton circleBtn;
    private JButton rectangleBtn;
    private JButton rotateBtn;
    private JButton flipBtn;
    private final JSlider thicknessSlider;
    protected static Cursor penCursor;
    private static Cursor eraserCursor;
    private Cursor lightenDarkenCursor;
    private static Cursor bucketCursor;
    private static Cursor horizontalCursor;
    private static Cursor verticalCursor;
    private static final ImageIcon colourPickerImg = new ImageIcon("toolImages/colourPicker.png");
    private static final ImageIcon cursorImg = new ImageIcon("cursorImages/cursor1.png");
    public static final ImageIcon penImg = new ImageIcon("toolImages/pen.png");
    private static final ImageIcon eraserImg = new ImageIcon("toolImages/eraser.png");
    private static final ImageIcon thicknessImg = new ImageIcon("toolImages/thickness.png");
    private static final ImageIcon resizeImg = new ImageIcon("toolImages/resolution.png");
    private static final ImageIcon resetImg = new ImageIcon("toolImages/reset.png");
    private static final ImageIcon lightenDarkenImg = new ImageIcon("toolImages/lightendarken.png");
    private static final ImageIcon bucketImg = new ImageIcon("toolImages/bucket.png");
    private static final ImageIcon symmetryImg = new ImageIcon("toolImages/symmetry.png");
    private static final ImageIcon circleImg = new ImageIcon("toolImages/circle.png");
    private static final ImageIcon rectangleImg = new ImageIcon("toolImages/rectangle.png");
    private static final ImageIcon rotateImg = new ImageIcon("toolImages/rotate.png");
    private static final ImageIcon flipImg = new ImageIcon("toolImages/flip.png");

    /** 
     * Represents the toolbar of tools at the side of the UI as a vertical stack of buttons
     */
    public Toolbar(PixelCanvas canvas) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.canvas = canvas;

        sliderPane = new JOptionPane();
        dialog = sliderPane.createDialog(canvas, "Line thickness");
        cursorBtn = new JButton(cursorImg);
        colourBtn = new JButton(colourPickerImg);
        penBtn = new JButton(penImg);
        penBtn.setDisabledIcon(penImg);
        eraserBtn = new JButton(eraserImg);
        eraserBtn.setDisabledIcon(eraserImg);
        circleBtn = new JButton(circleImg);
        rectangleBtn = new JButton(rectangleImg);
        bucketBtn = new JButton(bucketImg);
        symmetryBtn = new JButton(symmetryImg);
        resolutionBtn = new JButton(resizeImg);
        clearCanvasBtn = new JButton(resetImg);
        thicknessBtn = new JButton(thicknessImg);
        lightenDarkenBtn = new JButton(lightenDarkenImg);
        rotateBtn = new JButton(rotateImg);
        flipBtn = new JButton(flipImg);
        thicknessSlider = new JSlider(JSlider.HORIZONTAL,1,5,1);

        sliderPane.setMessage(new Object[] {thicknessSlider});

        thicknessSlider.addChangeListener(this);
        thicknessSlider.setMajorTickSpacing(1);
        thicknessSlider.setPaintTicks(true);
        thicknessSlider.setPaintLabels(true);
        
        addButton(cursorBtn, "Snipping tool: Drag to select pixels");
        addButton(penBtn, "Pen: Fill pixel(s) with the chosen colour");
        addButton(eraserBtn, "Eraser: Clear pixel(s) to the default background");
        addButton(thicknessBtn, "Change thickness of the current tool");
        addButton(resolutionBtn, "Change canvas size");
        addButton(clearCanvasBtn, "Reset canvas");
        addButton(lightenDarkenBtn, "Lighten/Darken pixels");
        addButton(bucketBtn, "Bucket fill");
        addButton(symmetryBtn, "Symmetry draw: Draws symmetrical pixels");
        addButton(colourBtn, "Eye dropper: Click a square to get its colour");
        addButton(circleBtn, "Circle draw");
        addButton(rectangleBtn, "Rectangle draw");
        addButton(rotateBtn, "Rotate the canvas");
        addButton(flipBtn, "Flip the canvas");

        penCursor = new CursorCreator(new Point(10,30)).create(canvas, "toolImages/pen.png", "Pen Cursor");
        eraserCursor = new CursorCreator(new Point(10,30)).create(canvas, "toolImages/eraser.png", "Eraser Cursor");
        lightenDarkenCursor = new CursorCreator(new Point(0,30)).create(canvas, "cursorImages/pipette.png", "LightenDarken Cursor");
        bucketCursor = new CursorCreator(new Point(10,30)).create(canvas, "toolImages/bucket.png", "Bucket Cursor");
        horizontalCursor = new CursorCreator(new Point(20,30)).create(canvas, "cursorImages/horizontalSymmetry.png", "Horizontal Symmetry");
        verticalCursor = new CursorCreator(new Point(20,20)).create(canvas, "cursorImages/verticalSymmetry.png", "Vertical Symmetry");
    }


    private void addButton(JButton button, String toolTipText) {
        button.addMouseListener(this);
        button.setToolTipText(toolTipText);
        setTransparent(button);
        this.add(button);
    }

    /**
     * Sets a JButton to have a transparent background and border
     * @param button The JButton being modified
     */
    private void setTransparent(JButton button)
    {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    }
    
    
    /**
     * Called when the user selects a new tool, disables their selected tool and enables all others
     */
    public void updateButtonStatuses() {
        cursorBtn.setEnabled(true);
        colourBtn.setEnabled(true);
        penBtn.setEnabled(true);
        eraserBtn.setEnabled(true);
        circleBtn.setEnabled(true);
        rectangleBtn.setEnabled(true);
        lightenDarkenBtn.setEnabled(true);
        bucketBtn.setEnabled(true);
        symmetryBtn.setEnabled(true);

        if(AppManager.currentTool == null) {
            
            return;
        }
        switch (AppManager.currentTool.getName()) {
            case "pen":
                penBtn.setEnabled(false);
                break;
            
            case "eraser":
                eraserBtn.setEnabled(false);
                break;

            case "circle":
                circleBtn.setEnabled(false);
                break;

            case "rectangle":
                rectangleBtn.setEnabled(false);
                break;
            case "line of symmetry":
                symmetryBtn.setEnabled(false);
                break;
            case "symmetry":
                symmetryBtn.setEnabled(false);
                break;
        }
    }
    /**
     * Sets the line thickness of the Pen Tools to the value of the JSlider
     * @param e The slider being moved
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        AppManager.changeThickness(thicknessSlider.getValue());
    }

    /**
     * Tracks the events that take place on the canvas, for now clicks and drags by the mouse, and
     * responds according to the newly selected tool by updating AppManager and the buttons in its 
     * own UI.
     * @param event The mouse event that just occurred
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        if(event.getSource() == cursorBtn) {
            canvas.setCursor(Window.mainCursor);
            AppManager.currentTool = null;
            updateButtonStatuses();
            return;
        } 
        
        if (event.getSource() == penBtn) {
            AppManager.currentTool = AppManager.SOLID_PEN;
            canvas.setCursor(penCursor);
        } else if (event.getSource() == colourBtn) {
            AppManager.currentTool = AppManager.COLOUR_PICKER;
            canvas.setCursor(lightenDarkenCursor);
        } else if (event.getSource() == eraserBtn) {
            AppManager.currentTool = AppManager.ERASER;
            canvas.setCursor(eraserCursor);
        } else if(event.getSource() == circleBtn){
            AppManager.currentTool = AppManager.CIRCLE_TOOL;
            updateButtonStatuses();
            canvas.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        } else if(event.getSource() == rectangleBtn){
            AppManager.currentTool = AppManager.RECTANGLE_TOOL;
            updateButtonStatuses();
            canvas.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        } else if (event.getSource() == resolutionBtn){
            new CustomResolutionBox(canvas); 
        } else if (event.getSource() == lightenDarkenBtn){
            AppManager.currentTool = AppManager.LIGHTEN;
            canvas.setCursor(lightenDarkenCursor);
        } else if(event.getSource() == clearCanvasBtn) {
            int confirmation = JOptionPane.showConfirmDialog(
                canvas, 
                "Are you sure you want to clear the canvas? \nAny unsaved chages will be lost.", 
                "Confirm", 
                JOptionPane.YES_NO_OPTION
            );
                
            if(confirmation == JOptionPane.YES_OPTION) {
                canvas.setDefaultCanvasSquares();
                canvas.repaint();
                AppManager.previousCanvasStates.clear();
                AppManager.redoStack.clear();
            }
        } else if(event.getSource() == thicknessBtn) {
            dialog.setVisible(true);
        } else if(event.getSource() == bucketBtn) {
            AppManager.currentTool = AppManager.BUCKET;
            canvas.setCursor(bucketCursor);
        } else if(event.getSource() == rotateBtn) {
            Window.canvas.rotate(true);
        } else if(event.getSource() == flipBtn) {
            Window.canvas.rotate(false);
        } else if(event.getSource() == symmetryBtn) {
            if(AppManager.SYMMETRY.islineOfSymmetryVertical) {
                canvas.setCursor(verticalCursor);
                AppManager.SYMMETRY.islineOfSymmetryVertical = false;
            }
            else if(!AppManager.SYMMETRY.islineOfSymmetryVertical) {
                canvas.setCursor(horizontalCursor);
                AppManager.SYMMETRY.islineOfSymmetryVertical = true;
            }
            AppManager.currentTool = AppManager.SYMMETRY_LINE;
        }

        if(AppManager.SNIPPING.canvasBeforeRectangle != null) {
            Window.canvas.openCanvas(AppManager.SNIPPING.canvasBeforeRectangle);
        AppManager.SNIPPING.canvasBeforeRectangle = null;
        }
        updateButtonStatuses();
    }
    public void mouseEntered(MouseEvent e) {
        if(e.getSource() instanceof JButton){
        }
    }
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

    public static Cursor getPenCursor() {
        return penCursor;
    }

    public static Cursor getEraserCursor() {
        return eraserCursor;
    }

    public static Cursor getBucketCursor() {
        return bucketCursor;
    }
}
