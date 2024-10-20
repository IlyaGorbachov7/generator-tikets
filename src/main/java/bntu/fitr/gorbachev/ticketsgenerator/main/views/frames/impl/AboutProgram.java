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
 * The class represents a dialog box object "About Program"
 *
 * @author Gorbachev I. D.
 * @version 21.04.2022
 */
public class AboutProgram extends BaseDialog implements LocalizerListener {

    /**
     * @param frame class object {@link JFrame}
     */
    public AboutProgram(Window frame, PanelType type) {
        super(frame, type, Localizer.get("frame.title.about.program")); //frame.title.about.program
        TicketGeneratorUtil.getLocalsConfiguration().addListener(this);
        this.setModal(false);
    }

    /**
     * The method initializes the panel
     */
    @Override
    public void initDialog() {
        this.setBounds((int) getFrame().getBounds().getX(), (int) getFrame().getBounds().getY(), 860, 500);
        this.setResizable(false);
        this.add(PanelFactory.getInstance().createPanel(this, getPanelType()));
    }

    @Override
    public void onUpdateLocale(Locale selectedLocale) {
        this.setTitle(Localizer.get("frame.title.about.program"));
    }
}
