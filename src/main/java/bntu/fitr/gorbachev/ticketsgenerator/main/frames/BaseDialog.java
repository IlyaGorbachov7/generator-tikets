package bntu.fitr.gorbachev.ticketsgenerator.main.frames;

import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelType;

import javax.swing.*;
import java.awt.*;

/**
 * This class represent basic for {@link JDialog}
 */
public abstract class BaseDialog extends JDialog implements DialogFunc {
    protected PanelType panelType;

    public BaseDialog(Window owner) {
        super(owner);
    }

    public BaseDialog(Window owner, String title) {
        super(owner, title);
        initDialog();
    }

    public BaseDialog(Window owner, PanelType panelType) {
        super(owner);
        this.panelType = panelType;
        initDialog();
    }

    public BaseDialog(Window owner, PanelType panelType, String title) {
        super(owner, title);
        this.panelType = panelType;
        initDialog();
    }

    public BaseDialog(Frame owner, PanelType panelType) {
        super(owner);
        this.panelType = panelType;
        initDialog();
    }

    public BaseDialog(Frame owner, PanelType panelType, String title) {
        super(owner, title);
        this.panelType = panelType;
        initDialog();
    }

    public BaseDialog(Dialog owner, PanelType panelType) {
        super(owner);
        this.panelType = panelType;
        initDialog();
    }

    public BaseDialog(Dialog owner, PanelType panelType, String title) {
        super(owner, title);
        this.panelType = panelType;
        initDialog();
    }


    public Window getFrame() {
        return getOwner();
    }

    public PanelType getPanelType() {
        return panelType;
    }

    public void setPanelType(PanelType panelType) {
        this.panelType = panelType;
    }

    @Override
    public abstract void initDialog();
}
