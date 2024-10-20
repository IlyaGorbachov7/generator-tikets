package bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.impl;


import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalizerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.BaseDialog;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

/**
 * The class represents a dialog box object "About Author"
 *
 * @author Gorbachev I. D.
 * @version 20.04.2022
 */
public class AboutAuthor extends BaseDialog implements LocalizerListener {
    /**
     * @param frame class object {@link JFrame}
     */
    public AboutAuthor(Window frame, PanelType type) {
        super(frame, type, Localizer.get("frame.title.about.author")); //frame.title.about.author
        TicketGeneratorUtil.getLocalsConfiguration().addListener(this);
        this.setModal(false);
    }

    /**
     * The method initializes the panel
     */
    @Override
    public void initDialog() {
        System.out.println(getFrame().getBounds());
        this.setBounds((int) getFrame().getBounds().getX(),
                (int) getFrame().getBounds().getY(), 250, 450);
        this.setResizable(false);
        add(PanelFactory.getInstance().createPanel(this, getPanelType()));
    }

    @Override
    public void onUpdateLocale(Locale selectedLocale) {
        this.setTitle(Localizer.get("frame.title.about.author"));
    }
}
