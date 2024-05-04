package bntu.fitr.gorbachev.ticketsgenerator.main.views.frames;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.impl.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;

import javax.swing.*;
import java.awt.*;

/**
 * The class is a factory for creating bntu.fitr.gorbachev.ticketsgenerator.main.views.frames and dialogs
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
    public JFrame createJFrame(FrameType frameType, PanelType rootPanelType) {
        return switch (frameType) {
            case SPLASH_SCREEN -> new LaunchFrame(rootPanelType);
            case MAIN_WINDOW -> new MainWindowFrame(rootPanelType);
            default -> throw new IllegalArgumentException("The frame is not define for the type: " + frameType);
        };
    }

    /**
     * Factory method creating Dialog
     *
     * @return {@link BaseDialog}
     * @throws IllegalArgumentException in case dialog is not define for the type, which defines {@link PanelType}:
     */
    public BaseDialog createJDialog(Window root, FrameType type, PanelType rootPanelType) {
        return switch (type) {
            case ABOUT_AUTHOR -> new AboutAuthor(root, rootPanelType);
            case ABOUT_PROGRAM -> new AboutProgram(root, rootPanelType);
            case FILE_VIEWER -> new FileViewer(root);
            case RECORD_SETTING -> new RecordSetting(root, rootPanelType);
            case INPUT_PARAM_DB -> new InputParametersDialog(root, rootPanelType);
            default -> throw new IllegalArgumentException("The dialog is not define for the type: " + type);
        };
    }
}
