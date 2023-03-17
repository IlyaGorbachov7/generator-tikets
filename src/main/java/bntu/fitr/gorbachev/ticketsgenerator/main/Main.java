package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelType;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatLightLaf());
        FrameDialogFactory.getInstance().createJFrame(PanelType.SPLASH_SCREEN).setVisible(true);
    }
}
