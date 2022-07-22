package panels;

import panels.impl.AboutAuthorPanel;
import panels.impl.AboutProgramPanel;
import panels.impl.MainWindowPanel;
import panels.impl.SplashScreenPanel;

import java.awt.*;

public class PanelFactory {
    private static final PanelFactory instance = new PanelFactory();

    private PanelFactory() {

    }

    public static PanelFactory getInstance() {
        return instance;
    }

    public BasePanel createPanel(Window rootWindow, PanelType type) {
        return switch (type) {
            case SPLASH_SCREEN -> new SplashScreenPanel(rootWindow);
            case ABOUT_PROGRAM -> new AboutProgramPanel(rootWindow);
            case ABOUT_AUTHOR -> new AboutAuthorPanel(rootWindow);
            case MAIN_WINDOW -> new MainWindowPanel(rootWindow);
        };
    }

}
