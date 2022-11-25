package bntu.fitr.gorbachev.ticketsgenerator.main.frames.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.frames.DialogFunc;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelType;

import javax.swing.*;
import java.awt.*;

import static bntu.fitr.gorbachev.ticketsgenerator.main.frames.impl.LaunchFrame.toolkit;

public class MainWindowFrame extends JFrame implements DialogFunc {

    public MainWindowFrame(PanelType type) {
        Dimension sizeScreen = toolkit.getScreenSize();
        this.setLayout(new BorderLayout());
        Dimension sizeFrame = new Dimension((int) (sizeScreen.width / 1.4),
                (int) (sizeScreen.height / 1.4));
        this.setBounds((sizeScreen.width - sizeFrame.width) / 2,
                (sizeScreen.height - sizeFrame.height) / 2,
                sizeFrame.width, sizeFrame.height);
        this.setMinimumSize(sizeFrame);
        this.setResizable(true);


        JPanel panel = PanelFactory.getInstance().createPanel(this, type);
        this.add(panel, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void initDialog() {

    }
}
