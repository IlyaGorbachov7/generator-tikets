package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.PanelType;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

/**
 * MVC pattern
 * <p>
 * https://www.oracle.com/technical-resources/articles/javase/mvc.html
 */
public class Main {
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatLightLaf());
        FrameDialogFactory.getInstance().createJFrame(PanelType.SPLASH_SCREEN).setVisible(true);
    }
}
