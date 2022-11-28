package bntu.fitr.gorbachev.ticketsgenerator.main.frames;

import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelType;

import javax.swing.*;
import java.awt.*;

public abstract class BaseFrame extends JFrame implements DialogFunc {
    private PanelType panelType;

    public BaseFrame() throws HeadlessException {
    }

    public BaseFrame(String title) throws HeadlessException {
        super(title);
    }

    public BaseFrame(PanelType panelType) throws HeadlessException {
        this.panelType = panelType;
    }

    public BaseFrame(String title, PanelType panelType) throws HeadlessException {
        super(title);
        this.panelType = panelType;
    }

    public PanelType getPanelType() {
        return panelType;
    }

    public void setPanelType(PanelType panelType) {
        this.panelType = panelType;
    }
}
