package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.thememanag;

import java.awt.*;

public interface ThemeChangerListener {

    Component getComponent();

    default void updateComponent() {
        AppThemeManager.updateComponentTreeUI(getComponent());
    }
}
