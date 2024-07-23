package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForReadToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.exep.NotAccessForWriteToFileException;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.AppThemeManager;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;

@Slf4j
public class Main {

    // TODO: нужно сделать какой то мэнеджер  А не работать через Main класс
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        try {
            AppThemeManager.run();
            FrameDialogFactory.getInstance().createJFrame(FrameType.SPLASH_SCREEN, PanelType.SPLASH_SCREEN).setVisible(true);
        } catch (NotAccessForReadToFileException | NotAccessForWriteToFileException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Access undefined", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Undefinded Exception", "",JOptionPane.ERROR_MESSAGE );
        }
    }
}
