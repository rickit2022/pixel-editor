import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;

/**
 * The window that contains all UI elements
 */
public class Window extends JFrame {
    private static Window INSTANCE;
    public static PixelCanvas canvas;
    public static ColourPalette colourPalette;
    public static JPanel mainPanel;
    static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    private MenuToolBar menuBar;
    private Toolbar toolbar;
    private JScrollPane scrollPane;
    public static Cursor mainCursor;

    private static final ImageIcon icon = new ImageIcon("cursorImages/mainIcon.png");
    private static final AutosaverThread autosaver = new AutosaverThread();

    public Window(int width, int height)
    {
        setSize(width, height);
        setTitle("PixelEditor");
        setIconImage(icon.getImage());
        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        canvas = new PixelCanvas(128,128);
        mainPanel.add("Center", canvas);

        menuBar = new MenuToolBar();
        mainPanel.add("North", menuBar);

        toolbar = new Toolbar(canvas);
        mainPanel.add("East", toolbar);

        colourPalette = new ColourPalette();
        colourPalette.setPreferredSize(new Dimension(160,210));
        mainPanel.add("South", colourPalette);

        scrollPane = new JScrollPane(toolbar);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add("East", scrollPane);

        mainCursor = new CursorCreator(new Point(0,0)).create(canvas, "cursorImages/cursor.png", "Main Cursor");
        canvas.setCursor(mainCursor);
        
        setLocationRelativeTo(null);
        setVisible(true);
        INSTANCE = this;
        canvas.setPreferredSize(canvas.getSize());
        pack();
        autosaver.run();
    }

   
    public void toggleFullscreen() {
        if(this.getExtendedState() == JFrame.MAXIMIZED_BOTH){
            this.setExtendedState(JFrame.NORMAL);
        }
        else{
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }   

    public static Window getInstance() {
        return INSTANCE;
    }
}
