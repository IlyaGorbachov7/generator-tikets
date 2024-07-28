package bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.BaseFrame;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.impl.LaunchFrame.toolkit;

@Slf4j
public class MainWindowFrame extends BaseFrame {

    public MainWindowFrame(PanelType type) {
        setPanelType(type);
        Dimension sizeScreen = toolkit.getScreenSize();
        log.info("Size Window: width={}, height={}",sizeScreen.width, sizeScreen.height);
        this.setLayout(new BorderLayout());
        Dimension sizeFrame = new Dimension((int) (sizeScreen.width / 1.7),
                (int) (sizeScreen.height / 2.4));
        log.info("Size Frame: {}",sizeScreen);
        this.setBounds((sizeScreen.width - sizeFrame.width) / 2,
                (sizeScreen.height - sizeFrame.height) / 2,
                sizeFrame.width, sizeFrame.height);
        this.setPreferredSize(sizeFrame);
        this.setMinimumSize(new Dimension(940, 410));
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initDialog();
    }

    @Override
    public void initDialog() {
            JPanel panel = PanelFactory.getInstance().createPanel(this, getPanelType());
            this.add(panel, BorderLayout.CENTER);
    }

    private void initPanelErrorConnectionPool(ConnectionPoolException ex) {
        // general condition exception and execution exception
        this.add(new JLabel("Произошла ошибка подключения к базе данных"));
        JButton btnOk = new JButton("Ok");
        btnOk.addActionListener(e -> MainWindowFrame.this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        this.add(btnOk, BorderLayout.SOUTH);

        Dimension sizeScreen = toolkit.getScreenSize();
        Dimension sizeFrame = new Dimension(200, 100);
        this.setBounds((sizeScreen.width - sizeFrame.width) / 2,
                (sizeScreen.height - sizeFrame.height) / 2,
                sizeFrame.width, sizeFrame.height);
        this.setMinimumSize(sizeFrame);
        this.pack();
        this.validate();
    }
}
