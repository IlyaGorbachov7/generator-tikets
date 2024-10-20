package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;


import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalizerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.FileNames;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.Objects;

@Log4j2
public class AboutAuthorPanel extends BasePanel {

    public AboutAuthorPanel(Window frame) {
        super(frame);
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

        JLabel lbPost = new JLabel(" " + Localizer.get("panel.infoDev"));
        lbPost.setFont(new Font("Serif", Font.PLAIN, 17));
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 5);
        panelInfo.add(lbPost, gbc);

        JLabel lbName = new JLabel(Localizer.get("panel.nameDev"));
        lbName.setFont(new Font("Serif", Font.PLAIN, 17));
        gbc.gridy = 2;
        panelInfo.add(lbName, gbc);

        JLabel lbRelate = new JLabel(Localizer.get("panel.emailDev"));
        lbRelate.setFont(new Font("Serif", Font.PLAIN, 17));
        gbc.gridy = 3;
        panelInfo.add(lbRelate, gbc);

        this.add(panelInfo, BorderLayout.CENTER);

        var ok = new JButton(Localizer.get("btn.ok"));
        ok.addActionListener(event -> getRootFrame().setVisible(false));
        add(ok, BorderLayout.SOUTH);

        TicketGeneratorUtil.getLocalsConfiguration().addListener(new LocalizerListener() {
            @Override
            public void onUpdateLocale(Locale selectedLocale) {
                lbPost.setText(" " + Localizer.get("panel.infoDev"));
                lbName.setText(Localizer.get("panel.nameDev"));
                lbRelate.setText(Localizer.get("panel.emailDev"));
                ok.setText(Localizer.get("btn.ok"));
            }
        });
    }

    @Override
    public void setComponentsListeners() {

    }

    @Override
    public void setConfigComponents() {

    }
}
