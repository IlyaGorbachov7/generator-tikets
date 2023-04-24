package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl.*;

import java.awt.*;

/**
 * This class is a factory for creating bntu.fitr.gorbachev.ticketsgenerator.main.views.panels
 *
 * @author Gorvachev I. D.
 * @version 15.10.2022
 * @apiNote Some factory methods throw exception {@link IllegalArgumentException}
 * @see bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameDialogFactory
 * @see PanelType
 */
public class PanelFactory {
    private static final PanelFactory instance = new PanelFactory();

    private PanelFactory() {
    }

    public static PanelFactory getInstance() {
        return instance;
    }

    /**
     * Factory method creating Panel
     *
     * @return {@link BasePanel}
     * @throws IllegalArgumentException in case panel is not define for the type, which defines {@link PanelType}:
     */
    public BasePanel createPanel(Window rootWindow, PanelType type) {
        return switch (type) {
            case SPLASH_SCREEN -> new SplashScreenPanel(rootWindow);
            case ABOUT_PROGRAM -> new AboutProgramPanel(rootWindow);
            case ABOUT_AUTHOR -> new AboutAuthorPanel(rootWindow);
            case MAIN_WINDOW -> new MainWindowPanel(rootWindow);
            case RECORD_SETTING -> new RecordSettingPanel(rootWindow);
            default -> throw new IllegalArgumentException("The panel is not define for the type: " + type);
        };
    }

}
