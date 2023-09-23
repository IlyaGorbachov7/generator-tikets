package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        System.out.println("Обращаемся к базе данных, чтобы узанать нужную тему для отображения. Только когда узали, устанавливам ");
        FlatLightLaf.setup();
//        FlatDarkLaf.setup();
        FrameDialogFactory.getInstance().createJFrame(PanelType.SPLASH_SCREEN).setVisible(true);
    }
}
