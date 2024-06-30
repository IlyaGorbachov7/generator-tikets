package bntu.fitr.gorbachev.ticketsgenerator.main.views.frames;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.DialogFunc;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.thememanag.AppThemeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.thememanag.ThemeChangerListener;

import javax.swing.*;
import java.awt.*;

public abstract class BaseFrame extends JFrame implements DialogFunc, ThemeChangerListener {
    private PanelType panelType;

    {
        AppThemeManager.addThemeChangerListener(this);
    }
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

    @Override
    public abstract void initDialog();

    @Override
    public Component getComponent() {
        return this;
    }
}
