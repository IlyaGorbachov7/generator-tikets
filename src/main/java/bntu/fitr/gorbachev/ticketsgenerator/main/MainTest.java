package bntu.fitr.gorbachev.ticketsgenerator.main;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameDialogFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.FrameType;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.SneakyThrows;

import javax.swing.*;

public class MainTest {
    @SneakyThrows
    public static void main(String[] args) {
        JFrame dialog = FrameDialogFactory.getInstance().createJFrame(FrameType.MAIN_WINDOW, PanelType.INPUT_PARAM_DB);
        FlatLightLaf.setup();
        dialog.setVisible(true);
    }
}
