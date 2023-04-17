package bntu.fitr.gorbachev.ticketsgenerator.main.frames;

import bntu.fitr.gorbachev.ticketsgenerator.main.frames.impl.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelType;

import javax.swing.*;
import java.awt.*;

/**
 * The class is a factory for creating bntu.fitr.gorbachev.ticketsgenerator.main.frames and dialogs
 *
 * @author Gorvachev I. D.
 * @version 15.10.2022
 * @apiNote Some factory methods throw exception {@link IllegalArgumentException}
 * @see PanelType
 */
public class FrameDialogFactory {
    private static final FrameDialogFactory instance = new FrameDialogFactory();

    private FrameDialogFactory() {
    }

    public static FrameDialogFactory getInstance() {
        return instance;
    }

    /**
     * Factory method creating Frame
     *
     * @return Frame
     * @throws IllegalArgumentException in case frame is not define for the type, which defines {@link PanelType}:
     */
    public JFrame createJFrame(PanelType type) {
        return switch (type) {
            case SPLASH_SCREEN -> new LaunchFrame(type);
            case MAIN_WINDOW -> new MainWindowFrame(type);
            default -> throw new IllegalArgumentException("The frame is not define for the type: " + type);
        };
    }

    /**
     * Factory method creating Dialog
     *
     * @return {@link BaseDialog}
     * @throws IllegalArgumentException in case dialog is not define for the type, which defines {@link PanelType}:
     */
    public BaseDialog createJDialog(Window root, PanelType type) {
        return switch (type) {
            case ABOUT_AUTHOR -> new AboutAuthor(root, type);
            case ABOUT_PROGRAM -> new AboutProgram(root, type);
            case FILE_VIEWER -> new FileViewer(root);
            case RECORD_SETTING -> new RecordSetting(root, type);
            default -> throw new IllegalArgumentException("The dialog is not define for the type: " + type);
        };
    }
}
