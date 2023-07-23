/**
 * The Menu bar displayed at the top of the screen
 * @author Elliot
 */
import javax.swing.JMenu;

public class MenuToolBar extends javax.swing.JMenuBar {
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu viewMenu;
    private JMenu aboutMenu;
    private JMenu exportMenu;
    private JMenu snapshotMenu;
    private JMenu themeMenu;

    public MenuToolBar()
    { 
        this.fileMenu = new JMenu("File");
        this.fileMenu.add(MenuActionType.NEW_FILE.getAction());
        this.fileMenu.add(MenuActionType.OPEN_FILE.getAction());
        this.fileMenu.add(MenuActionType.SAVE_AS_FILE.getAction());
        this.fileMenu.add(MenuActionType.SAVE_FILE.getAction());
        this.fileMenu.add(MenuActionType.IMAGE_IMPORT.getAction());
        
        this.editMenu = new JMenu("Edit");
        this.editMenu.add(MenuActionType.UNDO.getAction());
        this.editMenu.add(MenuActionType.REDO.getAction());
        this.editMenu.add(MenuActionType.CUT.getAction());
        this.editMenu.add(MenuActionType.COPY.getAction());
        this.editMenu.add(MenuActionType.PASTE.getAction());

        this.viewMenu = new JMenu("View");

        this.aboutMenu = new JMenu("About");
        this.aboutMenu.add(MenuActionType.TUTORIAL.getAction());
        this.aboutMenu.add(MenuActionType.ABOUT_US.getAction());

        this.exportMenu = new JMenu("Export to...");
        this.exportMenu.add(MenuActionType.EXPORT_TO_BMP.getAction());
        this.exportMenu.add(MenuActionType.EXPORT_TO_JPEG.getAction());
        this.exportMenu.add(MenuActionType.EXPORT_TO_GIF.getAction());
        this.exportMenu.add(MenuActionType.EXPORT_TO_PNG.getAction());

        this.fileMenu.add(exportMenu);

        this.themeMenu = new JMenu("Themes");
        this.themeMenu.add(MenuActionType.DEFAULT_THEME.getAction());
        this.themeMenu.add(MenuActionType.NIMBUS.getAction());
        this.themeMenu.add(MenuActionType.DARK_THEME.getAction());
        this.themeMenu.add(MenuActionType.SOFT_THEME.getAction());

        this.viewMenu.add(MenuActionType.FULL_SCREEN_TOGGLE.getAction());
        this.viewMenu.add(themeMenu);

        this.snapshotMenu = new JMenu("Snapshot");
        this.snapshotMenu.add(MenuActionType.TAKE_SNAPSHOT.getAction());
        this.snapshotMenu.add(MenuActionType.COMPARE_SNAPSHOT.getAction());
        
        this.add(fileMenu);
        this.add(editMenu);
        this.add(viewMenu);
        this.add(aboutMenu);
        this.add(snapshotMenu);
    }
}
