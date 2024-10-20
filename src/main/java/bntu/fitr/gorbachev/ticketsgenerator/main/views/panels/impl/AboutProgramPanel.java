package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalizerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.FileNames;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.Objects;

public class AboutProgramPanel extends BasePanel {

    protected String getPathAboutProgramSnippet0Html() {
        return String.format(Locale.ROOT, FileNames.aboutProgramHtml,
                TicketGeneratorUtil.getLocalsConfiguration().getSelectedLocale());
    }

    protected String getPathAboutProgramSnippet1Html() {
        return String.format(Locale.ROOT, FileNames.aboutProgramSnippet1Html,
                TicketGeneratorUtil.getLocalsConfiguration().getSelectedLocale());
    }

    public AboutProgramPanel(Window frame) {
        super(frame);
        this.initPanel();
    }

    @SneakyThrows
    @Override
    public void initPanel() {
        this.setLayout(new BorderLayout());
        // TODO: отображение html старнцы также зависит от локализации!!!!!!!!!!!!
        JLabel lbHtml = new JLabel(FileNames.readResourceToString(getPathAboutProgramSnippet0Html()));

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

        JLabel lbHtml1 = new JLabel(FileNames.readResourceToString(getPathAboutProgramSnippet1Html()));
        lbHtml1.setFont(new Font("Serif", Font.PLAIN, 14));
        panelHtml = new JPanel(new GridLayout(1, 1, 5, 5));
        panelHtml.add(lbHtml1);
        panelInfo.add(panelHtml);
        this.add(new JScrollPane(panelInfo), BorderLayout.CENTER);

        var btnOk = new JButton(Localizer.get("btn.ok"));
        btnOk.addActionListener(event -> getRootFrame().setVisible(false));
        add(btnOk, BorderLayout.SOUTH);

        TicketGeneratorUtil.getLocalsConfiguration().addListener(new LocalizerListener() {
            @Override
            public void onUpdateLocale(Locale selectedLocale) {
                lbHtml.setText(getPathAboutProgramSnippet0Html());
                lbHtml1.setText(getPathAboutProgramSnippet1Html());
                btnOk.setText(Localizer.get("btn.ok"));
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
