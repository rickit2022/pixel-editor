import java.awt.*;

public class PixelEditor {
    public static void main(String[] args)
    {
        @SuppressWarnings("unused")
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int width = (int)(screenSize.getWidth()* 0.8); //setting the size of the editor to 80% of the user's screen
        int height = (int)(screenSize.getHeight()* 0.8); //setting the size of the editor to 80% of the user's screen
        new Window(width,height);
    }
}
