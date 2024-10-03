package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.FileNames;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AboutProgramPanel extends BasePanel {

    public AboutProgramPanel(Window frame) {
        super(frame);
        this.initPanel();
    }

    @SneakyThrows
    @Override
    public void initPanel() {
        this.setLayout(new BorderLayout());
        // TODO: отображение html старнцы также зависит от локализации!!!!!!!!!!!!
        JLabel lbHtml = new JLabel(FileNames.readResourceToString(FileNames.aboutProgramHtml));

        lbHtml.setFont(new Font("Serif", Font.PLAIN, 14));
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));

        JPanel panelHtml = new JPanel(new GridLayout(1, 1, 5, 5));
        panelHtml.add(lbHtml);
        panelInfo.add(panelHtml);

        JLabel lbImage = new JLabel(new ImageIcon(Objects.requireNonNull(
                FileNames.getResource(FileNames.picturePrim2))));
        JPanel panelImage = new JPanel(new GridLayout(1, 1, 5, 5));
        panelImage.add(lbImage);
        panelInfo.add(panelImage);

        lbHtml = new JLabel(FileNames.readResourceToString(FileNames.aboutProgramSnippet1Html));
        lbHtml.setFont(new Font("Serif", Font.PLAIN, 14));
        panelHtml = new JPanel(new GridLayout(1, 1, 5, 5));
        panelHtml.add(lbHtml);
        panelInfo.add(panelHtml);
        this.add(new JScrollPane(panelInfo), BorderLayout.CENTER);

        var btnOk = new JButton("OK");
        btnOk.addActionListener(event -> getRootFrame().setVisible(false));
        add(btnOk, BorderLayout.SOUTH);
    }

    @Override
    public void setComponentsListeners() {

    }

    @Override
    public void setConfigComponents() {

    }
}
