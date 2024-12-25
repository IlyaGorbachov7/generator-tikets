package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.FilesUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;

import javax.swing.*;
import java.util.Objects;

public class Main {
    public static final String SYS_PROP_APP_STORAGE = "os.storage";

    static {
        if (Objects.isNull(System.getProperty(SYS_PROP_APP_STORAGE))) {
            System.setProperty(SYS_PROP_APP_STORAGE, FilesUtil.getRootStore().toString());
        }
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        try {
            Class.forName(TicketGeneratorUtil.class.getName());
            FrameDialogFactory.getInstance().createJFrame(FrameType.SPLASH_SCREEN, PanelType.SPLASH_SCREEN).setVisible(true);
        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }
    }
}
