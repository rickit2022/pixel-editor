import java.awt.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Displaying txt docs such as guides, about us, etc.
 */

public class Info {
    public static String filePath;
    static JPanel display;

    public static void showInfo(String filePath) {
        JFrame frame = new JFrame("User guide");
        display = new JPanel();
        display.setBackground(Color.WHITE);
        try {
            display.add(readGuide(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.setSize(new Dimension(display.getPreferredSize()));
        frame.setContentPane(display);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.pack();
    }

    public static JTextArea readGuide(String filePath) throws IOException{
        JTextArea textArea = new JTextArea();
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        ArrayList<String> contents = new ArrayList<>();

        try{
            String line;
            while((line = reader.readLine()) != null){
                contents.add(line);
            }
            reader.close();

            for(String lineRead : contents){
                textArea.append(lineRead + "\n");
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);

        return textArea;
    }
}
