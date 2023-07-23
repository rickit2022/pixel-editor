import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.*;

public class ExportType {
    public static final String EXPORT_TO_BMP = "bmp";
    public static final String EXPORT_TO_JPEG = "jpeg";
    public static final String EXPORT_TO_GIF = "gif";
    public static final String EXPORT_TO_PNG = "png";
    
    /**
     * Once the contents of the canvas are saved, they can be exported to the same directory, in the form of either:
     * bmp, jpeg, gif or png
     * @param type a string of the file abbreviation to export to e.g. "png"
     */
    public static void export(String type) {
        if (AppManager.getCurrentPath() == null) {
            JOptionPane.showMessageDialog(
                null, 
                "Please save file as before attempting to export."
            );
            return;
        }

        String currentPath = AppManager.getCurrentPath();
        String newPath = currentPath.substring(0, currentPath.length() - 3);
        File file = new File(newPath + type);

        BufferedImage image = new BufferedImage(Window.canvas.getCanvasWidth() * 10, Window.canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = image.createGraphics();
        Window.canvas.paintAll(cg);
        Image transparentImg = makeTransparent(image, Window.canvas.getDefaultDark(), Window.canvas.getDefaultLight()); //sets the default pixels to be transparent
        BufferedImage result = ImageToBufferedImage(transparentImg, Window.canvas.getWidth(), Window.canvas.getHeight());
        try {
            //if the format is bmp or jpeg, then dont set as transparent
            if((type == ExportType.EXPORT_TO_JPEG || type == ExportType.EXPORT_TO_BMP) && ImageIO.write(image,type,file)) {
              JOptionPane.showMessageDialog(Window.canvas,"Export complete!", "Success", JOptionPane.PLAIN_MESSAGE);
            }
            else if (ImageIO.write(result, type, file)) //set as transparent if format is png or gif
                {
                    JOptionPane.showMessageDialog(Window.canvas,"Export complete! Your image can be found in the same directory as the .pep file.",
                     "Success", JOptionPane.PLAIN_MESSAGE);
                }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(Window.canvas,"Export failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Takes an Image and 2 colours, and sets the Image as transparent wherever these colours appear.
     * @param image the Image that is made transparent
     * @param c1 the first colour that will be made transparent
     * @param c2 the second colour that will be made transparent
     * @return the new transparent BufferedImage
     */
    private static Image makeTransparent(BufferedImage image, Color c1, Color c2)
    {
      final int r1 = c1.getRed();
      final int g1 = c1.getGreen();
      final int b1 = c1.getBlue();
      final int r2 = c2.getRed();
      final int g2 = c2.getGreen();
      final int b2 = c2.getBlue();
      
      ImageFilter filter = new RGBImageFilter()
      {
        public final int filterRGB(int x, int y, int rgb)
        {
          int r = (rgb & 0xFF0000) >> 16;
          int g = (rgb & 0xFF00) >> 8;
          int b = rgb & 0xFF;
          if (r >= r1 && r <= r2 &&
              g >= g1 && g <= g2 &&
              b >= b1 && b <= b2)
          {
            return rgb & 0xFFFFFF;
          }
          return rgb;
        }
      };
      ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    /**
     * Takes an Image and converts it into a BufferedImage
     * @param image the Image to convert to a BufferedImage
     * @param width the width of the desired image
     * @param height the height of the desired image
     * @return the new BufferedImage
     */
    private static BufferedImage ImageToBufferedImage(Image image, int width, int height)
    {
      BufferedImage bImg = new BufferedImage(
          width, height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = bImg.createGraphics();
      g2.drawImage(image, 0, 0, null);
      g2.dispose();
      return bImg;
    }
}