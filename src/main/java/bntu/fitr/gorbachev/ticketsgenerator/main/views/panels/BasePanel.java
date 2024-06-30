package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.PanelFunc;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.thememanag.AppThemeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.thememanag.ThemeChangerListener;

import javax.swing.*;
import java.awt.*;

/**
 * This class present base panel with basic functionality and
 * interact with {@link JPanel}
 *
 * @author SecuRiTy
 * @version 27.05.2022
 */
public abstract class BasePanel extends JPanel implements PanelFunc {
    protected Window frame;

    public BasePanel(Window frame) {
        this.frame = frame;
    }

    @Override
    public abstract void initPanel();

    @Override
    public abstract void setComponentsListeners();

    @Override
    public abstract void setConfigComponents();

    @Override
    public Window getRootFrame() {
        return frame;
    }
}

