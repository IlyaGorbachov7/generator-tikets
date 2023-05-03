package bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.impl;


import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl.AboutAuthorController;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.InitViewEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.AboutAuthorModel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.tools.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class AboutAuthorPanel extends BasePanel {
    private final AboutAuthorController controller;
    public AboutAuthorPanel(Window frame) {
        super(frame);
        this.initPanel();
        controller = new AboutAuthorController(this,new AboutAuthorModel());
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
        ok.addActionListener(event -> controller.actionOkBtn());
        add(ok, BorderLayout.SOUTH);
    }

    @Override
    public void setComponentsListeners() {

    }

    @Override
    public void setConfigComponents() {

    }

    @Override
    public void changeStateViewElems(ChangeFieldModelEvent event) {

    }

    @Override
    public void actionViewElems(ActionEvent event) {

    }

    @Override
    public void primaryInitViewElems(InitViewEvent event) {

    }
}
