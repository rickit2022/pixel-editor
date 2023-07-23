import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;

public class CustomResolutionBox extends JDialog implements ActionListener, KeyListener {
    private PixelCanvas canvas;
    private JPanel panel;
    private JLabel inputLabel;
    private JButton submitButton;
    private JButton fullButton;
    private JTextField widthField;
    private JTextField heightField;
    private CanvasSquare[][] currentGrid;
    private CanvasSquare[][] newGrid;

    public CustomResolutionBox(PixelCanvas canvas) {
        this.canvas = canvas;
        String currentCanvasWidth = Integer.toString(canvas.getCanvasWidth());
        String currentCanvasHeight = Integer.toString(canvas.getCanvasHeight());
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JLabel widthLabel = new JLabel("Width:");
        widthField = new JTextField();
        widthField.setText(currentCanvasWidth);
        panel.add(widthLabel);
        panel.add(widthField);

        JLabel heightLabel = new JLabel("Height:");
        heightField = new JTextField();
        heightField.setText(currentCanvasHeight);
        panel.add(heightLabel);
        panel.add(heightField);

        inputLabel = new JLabel();
        inputLabel.setForeground(Color.RED);
        inputLabel.setFont(new Font("Courier New", Font.BOLD, 11));
        panel.add(inputLabel);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        buttonPane.add(Box.createHorizontalGlue());

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        buttonPane.add(submitButton);

        fullButton = new JButton("Fullsize");
        fullButton.addActionListener(this);
        buttonPane.add(fullButton);

        Container contentPane = getContentPane();
        contentPane.add(panel,BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);

        widthField.addKeyListener(this);
        heightField.addKeyListener(this);

        setTitle("Resize canvas");
        setModal(true);
        setLocationRelativeTo(canvas);
        contentPane.setPreferredSize(new Dimension(300,250));
        pack();
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        String widthInput = widthField.getText();
        String heightInput = heightField.getText();

        if(event.getSource() == submitButton){
            if (widthInput.isEmpty() || heightInput.isEmpty()) {
                inputLabel.setText("Please enter a new width and height!");
            } else {
                try {
                    int width = Integer.parseInt(widthInput);
                    int height = Integer.parseInt(heightInput);

                    if(width < 8 || height < 8 || width > 256 || height > 256) {
                        inputLabel.setText("Minimum: 8x8, Maximum: 256x256");
                        return;
                    }
                    else if(width < canvas.getCanvasWidth() || height < canvas.getCanvasHeight()){
                        int confirmation = JOptionPane.showConfirmDialog(
                            canvas, 
                            "If you resize the canvas to a smaller resolution, the current drawing will be deleted. Are you sure?", 
                            "Confirm", 
                            JOptionPane.YES_NO_OPTION
                        );
                            
                        if(confirmation == JOptionPane.NO_OPTION) {
                            return;
                        }
                        canvas.resizeCanvas(width, height);
                        canvas.repaint();
                        this.dispose();
                        return;
                    }
                    restoreCanvas(width, height);
                    canvas.repaint();
                    this.dispose();

                } catch (NumberFormatException e) {  
                    inputLabel.setText("Please enter a valid number!");
                }
            }
        }
        else if(event.getSource() == fullButton){
            restoreCanvas(256, 256);
            this.dispose();
        }
    }

    public void restoreCanvas(int width, int height){
        currentGrid = canvas.getGrid();
        canvas.resizeCanvas(width, height);
        newGrid = canvas.getGrid();
        if(width >= canvas.getCanvasWidth() || height >= canvas.getCanvasHeight() ){
            for(int x = 0; x< currentGrid.length; x++){
                for(int y = 0; y < currentGrid[0].length; y++){
                    newGrid[x][y] = currentGrid[x][y];
                }
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        if((JTextField) e.getSource() == widthField) {
            heightField.setText(widthField.getText());
        }
        else {
            widthField.setText(heightField.getText());
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        //
    }


    @Override
    public void keyReleased(KeyEvent e) {
        if((JTextField) e.getSource() == widthField) {
            heightField.setText(widthField.getText());
        }
        else {
            widthField.setText(heightField.getText());
        }
    }
}