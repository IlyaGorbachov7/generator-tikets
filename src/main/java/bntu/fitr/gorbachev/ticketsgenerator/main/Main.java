package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.frames.impl.LaunchFrame;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelType;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FrameDialogFactory.getInstance().createJFrame(PanelType.SPLASH_SCREEN).setVisible(true);
    }
}
