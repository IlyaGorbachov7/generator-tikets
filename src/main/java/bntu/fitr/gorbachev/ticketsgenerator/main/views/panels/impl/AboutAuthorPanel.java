package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;


import bntu.fitr.gorbachev.ticketsgenerator.main.util.logger.LoggerMethod;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.logger.Slf4jLoggable;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.FileNames;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AboutAuthorPanel extends BasePanel implements Slf4jLoggable {

    public AboutAuthorPanel(Window frame) {
        super(frame);
        log(LoggerMethod.error, "Привет 11 08 2024]");
        this.initPanel();
    }

    @Override
    public void initPanel() {
        this.setLayout(new BorderLayout());

        JPanel panelInfo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lbImage = new JLabel(new ImageIcon(Objects.requireNonNull(
                getClass().getResource(FileNames.pictureMy))
        ));
        panelInfo.add(lbImage, gbc);

        JLabel lbPost = new JLabel(" студент группы 10702419");
        lbPost.setFont(new Font("Serif", Font.PLAIN, 17));
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 5);
        panelInfo.add(lbPost, gbc);

        JLabel lbName = new JLabel("Горбачёв Илья Дмитриевич");
        lbName.setFont(new Font("Serif", Font.PLAIN, 17));
        gbc.gridy = 2;
        panelInfo.add(lbName, gbc);

        JLabel lbRelate = new JLabel("gorbacevaili891@gmail.com");
        lbRelate.setFont(new Font("Serif", Font.PLAIN, 17));
        gbc.gridy = 3;
        panelInfo.add(lbRelate, gbc);

        this.add(panelInfo, BorderLayout.CENTER);

        var ok = new JButton("OK");
        ok.addActionListener(event -> getRootFrame().setVisible(false));
        add(ok, BorderLayout.SOUTH);
    }

    @Override
    public void setComponentsListeners() {

    }

    @Override
    public void setConfigComponents() {

    }
}
