/**
 * Enumerations that hold menu actions 
 */
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import javax.swing.*;
import javax.swing.UIManager.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static javax.swing.KeyStroke.getKeyStroke;

public enum MenuActionType {
    NEW_FILE(new AbstractMenuAction("New file", getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK))
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(
                null, 
                "Are you sure you want to create a new file? All unsaved work will be lost!",
                "Are You Sure?",
                JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                Window.canvas.resizeCanvas(100, 100);
                AppManager.setCurrentPath(null);
            }
        }
    }),


    OPEN_FILE(new AbstractMenuAction("Open file", getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK))
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int selection = fileChooser.showOpenDialog(null);
            if (selection == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fileChooser.getSelectedFile();
                    if (!file.toPath().toString().endsWith(".pep") && !file.toPath().toString().endsWith(".cpep")) {
                        JOptionPane.showMessageDialog(
                            null, 
                            String.format("%s is not a valid Pixel Editor file!", file.getName()), 
                            "Invalid file type", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    FileInputStream inputStream = new FileInputStream(file);

                    // load and set the file and tool size
                    byte[] metadataBuffer = new byte[3];
                    inputStream.read(metadataBuffer);
                    Window.canvas.resizeCanvas(metadataBuffer[0], metadataBuffer[1]);
                    AppManager.changeThickness(metadataBuffer[2]);

                    // load and set the canvas data
                    int bufferSize = Window.canvas.getCanvasWidth() * Window.canvas.getCanvasHeight() * 3;
                    byte[] pixelBuffer = new byte[bufferSize];
                    inputStream.read(pixelBuffer);
                    inputStream.close();
                    
                    Window.canvas.openCanvas(pixelBuffer);
                    AppManager.setCurrentPath(file.getPath());
                }

                catch (IOException exception) {
                    System.out.println("Could not open file.");
                    exception.printStackTrace();
                }
            }
        }
    }),


    SAVE_AS_FILE(new AbstractMenuAction("Save As", null)
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open the file picker
            JFileChooser fileChooser = new JFileChooser();
            int selection = fileChooser.showSaveDialog(null);
            if (selection == JFileChooser.APPROVE_OPTION) { // if the user selects a file...
                try {
                    // get and create the new file if needed
                    File file = fileChooser.getSelectedFile();
                    if (!file.getAbsolutePath().endsWith(".pep")) {
                        file = new File(fileChooser.getSelectedFile() + ".pep");
                    }

                    file.createNewFile();
                    Window.canvas.save(file);

                    AppManager.setCurrentPath(file.getPath());
                } catch (IOException exception) {
                    System.out.println("Could not save file.");
                    exception.printStackTrace();
                }
            }
        }
    }),


    SAVE_FILE(new AbstractMenuAction("Save", getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (AppManager.getCurrentPath() == null) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Please save file as before attempting to save."
                );
                return;
            }

            File file = new File(AppManager.getCurrentPath());
            Window.canvas.save(file);
        }
    }),


    TAKE_SNAPSHOT(new AbstractMenuAction("Take New Snapshot", null) {
        @Override
        public void actionPerformed(ActionEvent e) {
            String confirmMsg = "Please make sure you want to save this snapshot; the existing snapshot will be irreversibly overwritten!";
            boolean allowContinue = false;
            if (AppManager.snapshot == null) {
                allowContinue = true;
            } else if (JOptionPane.showConfirmDialog(null, confirmMsg) == JOptionPane.OK_OPTION) {
                allowContinue = true;
            }
            if (allowContinue) {
                AppManager.snapshot = Window.canvas.serializeCanvas();
            }
        }
    }),

    COMPARE_SNAPSHOT(new AbstractMenuAction("Switch to Snapshot", null) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(AppManager.snapshot == null) {return;}
            Window.canvas.openCanvas(AppManager.snapshot);
        }
    }),


    EXPORT_TO_BMP(new AbstractMenuAction("BMP", null)
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            String type = ExportType.EXPORT_TO_BMP;
            ExportType.export(type);
        }
    }),
    EXPORT_TO_JPEG(new AbstractMenuAction("JPEG", null)
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            String type = ExportType.EXPORT_TO_JPEG;
            ExportType.export(type);
        }
    }),
    EXPORT_TO_GIF(new AbstractMenuAction("GIF", null)
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            String type = ExportType.EXPORT_TO_GIF;
            ExportType.export(type); 
        }
    }),
    EXPORT_TO_PNG(new AbstractMenuAction("PNG", null)
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            String type = ExportType.EXPORT_TO_PNG;
            ExportType.export(type);
        }
    }),

    UNDO(new AbstractMenuAction("Undo", getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK))
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppManager.undo(Window.canvas);
        }
    }),
    REDO(new AbstractMenuAction("Redo", getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK))
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppManager.redo(Window.canvas);
        }
    }),
    CUT(new AbstractMenuAction("Cut", getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK))
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppManager.SNIPPING.cutPixels();
        }
    }),
    COPY(new AbstractMenuAction("Copy", getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK))
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppManager.SNIPPING.copyPixels();
        }
    }),
    PASTE(new AbstractMenuAction("Paste", getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK))
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            AppManager.SNIPPING.pastePixels();
        }
    }),
    FULL_SCREEN_TOGGLE(new AbstractMenuAction("Toggle Fullscreen", getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK))
    {
        @Override
        public void actionPerformed(ActionEvent e) {
                Window.getInstance().toggleFullscreen();
            }
        
    }),
    DEFAULT_THEME(new AbstractMenuAction("Default", null)
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Set cross-platform Java look and feel
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(Window.mainPanel);
                SwingUtilities.updateComponentTreeUI(Window.colourPalette);
                }
                catch (Exception f) {
                        System.out.println("Could not set default theme");
                    }
             }
    }),
    NIMBUS(new AbstractMenuAction("Nimbus", null)
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception f) {
                    System.out.println("Could not set nimbus theme");
                 }
            UIManager.put("control", Color.decode("#d6d9df"));   //main colour
            UIManager.put("nimbusBase", Color.decode("#33628c"));  //tab and menu color
            UIManager.put("nimbusLightBackground", Color.WHITE);   //text background colour
            UIManager.put("text", Color.BLACK);  //text colour
            SwingUtilities.updateComponentTreeUI(Window.mainPanel);
            SwingUtilities.updateComponentTreeUI(Window.colourPalette);
        }
    }),
    DARK_THEME(new AbstractMenuAction("Dark Theme", null)
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;                    }
                    }
                } catch (Exception f) {
                    System.out.println("Could not set dark theme");
                } 
                UIManager.put("control", Color.decode("#383838"));   //main colour
                UIManager.put("nimbusBase", Color.decode("#383838"));  //tab and menu color
                UIManager.put("nimbusLightBackground", Color.LIGHT_GRAY);   //text background colour
                UIManager.put("text", new Color(128,0,128));  //text colour
                SwingUtilities.updateComponentTreeUI(Window.mainPanel);
                SwingUtilities.updateComponentTreeUI(Window.colourPalette);
             }
    }),
    SOFT_THEME(new AbstractMenuAction("Soft Theme", null)
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;                    }
                    }
                } catch (Exception f) {
                    System.out.println("Could not set soft theme");
                }

                UIManager.put("control", Color.decode("#d3b482"));   //main colour
                UIManager.put("nimbusBase", Color.decode("#b37c19"));//tab and menu color
                UIManager.put("nimbusLightBackground", Color.LIGHT_GRAY);   //text background colour
                UIManager.put("text", Color.DARK_GRAY);  //text colour
                SwingUtilities.updateComponentTreeUI(Window.mainPanel);
                SwingUtilities.updateComponentTreeUI(Window.colourPalette);
             }
    }),
    IMAGE_IMPORT(new AbstractMenuAction("Import Image", getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK))
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Window.canvas.addMouseListener(new MouseAdapter() {
                    ShapeImport si = new ShapeImport();
                    //allows user to choose file
                    BufferedImage img = si.ChooseFile();
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //converts mouse click coords to corresponding square 
                        int x = e.getX() / 10;
                        int y = e.getY() / 10;

                        si.DrawImage(img, x, y);                       
                        Window.canvas.removeMouseListener(this);
                    }
                    
                 });
            } catch (IOException f) {}
        }
        
    }),
    TUTORIAL(new AbstractMenuAction("Tutorial", null)
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            Info.showInfo("txt/guide.txt");
        }
    }),
    ABOUT_US(new AbstractMenuAction("About us", null)
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            Info.showInfo("txt/about.txt");
        }
    });

    private final AbstractMenuAction action;

    private MenuActionType(AbstractMenuAction action)
    {
        this.action = action;
    }

    public AbstractMenuAction getAction() {
        return this.action;
    }
}
