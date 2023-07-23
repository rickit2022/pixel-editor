import java.awt.Color;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColourPalette extends JColorChooser implements ChangeListener {
    /** Creates a new colour palette and sets the inital colour */
    public ColourPalette() { 
        this.setColor(Color.BLACK);
        this.getSelectionModel().addChangeListener(this);
        this.setPreviewPanel(new JPanel());  //removes preview panel
    }


    /**
     * Detects a change in the selected colour and updated the primary colour in the AppManager
     * state manager.
     */
    @Override
    public void stateChanged(ChangeEvent event) {
        AppManager.primaryColour = new SquareColour(
            this.getColor().getRed(),
            this.getColor().getGreen(),
            this.getColor().getBlue()
        );
    }
}