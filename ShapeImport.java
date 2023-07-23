import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;

public class ShapeImport{
    
    public BufferedImage ChooseFile()  throws IOException  {
        //creates new file selector and filter so that it only shows images
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Images", "jpg","png","gif","bmp","webmp");
        fileChooser.setFileFilter(filter);

        //checks weather the user has selected a file, if they have it saves the file path and reads the image which is then returned
        int approval = fileChooser.showOpenDialog(null);
        if(approval == JFileChooser.APPROVE_OPTION){
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            BufferedImage img = ImageIO.read(file);
            JOptionPane.showMessageDialog(Window.canvas, "Click canvas to place image",
            "", JOptionPane.PLAIN_MESSAGE);  
            return img;
        }
        return null;

    }

    public void DrawImage(BufferedImage img, double xoff, double yoff){
        
        CanvasSquare[][] grid = Window.canvas.getGrid();
        //checks to see if the image will fit, if it doesnt it shows an error message
        if ((int) (img.getWidth() + xoff) > Window.canvas.getCanvasWidth() || (int) (img.getHeight() + yoff) > Window.canvas.getCanvasHeight()) {
            JOptionPane.showMessageDialog(Window.canvas, "Image too large or exceeds canvas boundaries",
            "Error", JOptionPane.ERROR_MESSAGE);           
        }else{
            //loops through the individual pixels in the image
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    Color c = new Color(img.getRGB(x,y));
                    int red = c.getRed();
                    int green = c.getGreen();
                    int blue = c.getBlue();
                    
                    //check for transparrent pixel first then skip square
                    if( (img.getRGB(x,y)>>24) == 0x00 ) {}
                    else {
                        //sets the current grid square to the corresponding colour from the image
                        grid[(int) (x + xoff)][(int) (y + yoff)].setColour(new SquareColour(red,green,blue));
                    }
                }
              }
              Window.canvas.repaint();
              AppManager.SNIPPING.canvasBeforeRectangle = null;
        }
    }

}
