import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.awt.Image;
import java.awt.Toolkit;
import javax.imageio.ImageIO;

public class CursorCreator extends JPanel{
    private Point point;
    private Image image;

    public CursorCreator(Point point) {this.point = point;}
    public Cursor create(PixelCanvas canvas, String filePath, String name){
        File file = new File(filePath);
        try{
            image = ImageIO.read(file);
        } catch (IOException e){
            e.printStackTrace();
        }
        Point hotSpot = point;
        Cursor newCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, hotSpot, name);
        return newCursor;
    }
}
