/**
 * Gives a menu item an abstract action using its name and keyboard shortcut
 * @author Elliot
 */
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public abstract class AbstractMenuAction extends AbstractAction {
    
    public AbstractMenuAction(String text, KeyStroke kbShortcut)
    {
        super(text);
        putValue(SHORT_DESCRIPTION, text);
        if(kbShortcut != null)
        {
            putValue(ACCELERATOR_KEY, kbShortcut);
        }
    }
}
