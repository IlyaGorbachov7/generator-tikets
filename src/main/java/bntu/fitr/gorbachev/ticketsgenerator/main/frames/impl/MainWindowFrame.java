package bntu.fitr.gorbachev.ticketsgenerator.main.frames.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.dto.connectionpool.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.dto.connectionpool.PoolConnection;
import bntu.fitr.gorbachev.ticketsgenerator.main.frames.BaseFrame;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static bntu.fitr.gorbachev.ticketsgenerator.main.frames.impl.LaunchFrame.toolkit;

public class MainWindowFrame extends BaseFrame {

    public MainWindowFrame(PanelType type) {
        setPanelType(type);
        Dimension sizeScreen = toolkit.getScreenSize();
        this.setLayout(new BorderLayout());
        Dimension sizeFrame = new Dimension((int) (sizeScreen.width / 1.4),
                (int) (sizeScreen.height / 1.5));
        this.setBounds((sizeScreen.width - sizeFrame.width) / 2,
                (sizeScreen.height - sizeFrame.height) / 2,
                sizeFrame.width, sizeFrame.height);
        this.setMinimumSize(sizeFrame);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initDialog();
    }

    @Override
    public void initDialog() {
        try {
            PoolConnection.getInstance().initPool();
            JPanel panel = PanelFactory.getInstance().createPanel(this, getPanelType());
            this.add(panel, BorderLayout.CENTER);

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    PoolConnection.getInstance().destroyConnectionPool();
                }
            });
        } catch (ConnectionPoolException e) {
            initPanelErrorConnectionPool(e);
            PoolConnection.getInstance().destroyConnectionPool();
        }
    }

    private void initPanelErrorConnectionPool(ConnectionPoolException ex) {
        // general condition exception and execution exception
        Dimension sizeScreen = toolkit.getScreenSize();
        Dimension sizeFrame = new Dimension(200, 100);
        this.setBounds((sizeScreen.width - sizeFrame.width) / 2,
                (sizeScreen.height - sizeFrame.height) / 2,
                sizeFrame.width, sizeFrame.height);
        this.setMinimumSize(sizeFrame);
        this.pack();
        this.validate();

        this.add(new JLabel(ex.getMessage()));
        JButton btnOk = new JButton("Ok");
        btnOk.addActionListener(e -> {
            MainWindowFrame.this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        this.add(btnOk, BorderLayout.SOUTH);
    }
}
