package bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.impl;


import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.BaseFrame;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;

import javax.swing.*;
import java.awt.*;

/**
 * This class is represented {@link JFrame}
 */
public class LaunchFrame extends BaseFrame {
    public static final Toolkit toolkit = Toolkit.getDefaultToolkit();

    /**
     * This constructor created this Frame
     */
    public LaunchFrame(PanelType type) {
        setPanelType(type);
        Dimension sizeScreen = toolkit.getScreenSize();
        Dimension sizeFrame = new Dimension(sizeScreen.width / 2 - 150, sizeScreen.height / 2);
        this.setLayout(new BorderLayout());
        this.setBounds((sizeScreen.width - sizeFrame.width) / 2,
                (sizeScreen.height - sizeFrame.height) / 2,
                sizeFrame.width, sizeFrame.height);
        this.setMinimumSize(sizeFrame);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initDialog();
    }

    @Override
    public void initDialog() {
        JPanel splashScreenPanel = PanelFactory.getInstance().createPanel(this, getPanelType());
        this.add(splashScreenPanel, BorderLayout.CENTER);
    }
}
