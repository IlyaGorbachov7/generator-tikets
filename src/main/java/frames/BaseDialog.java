package frames;

import panels.PanelType;
import panels.BasePanel;

import javax.swing.*;
import java.awt.*;

/**
 * This class represent basic for {@link JDialog}
 */
public abstract class BaseDialog extends JDialog implements DialogFunc {
    public BaseDialog(Window owner) {
        super(owner);
    }

    public BaseDialog(Window owner, ModalityType modalityType) {
        super(owner, modalityType);
    }

    public BaseDialog(Window owner, String title) {
        super(owner, title);
    }

    public BaseDialog(Window owner, String title, ModalityType modalityType) {
        super(owner, title, modalityType);
    }

    public BaseDialog(Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc) {
        super(owner, title, modalityType, gc);
    }

    @Override
    public abstract void initDialog();
}
