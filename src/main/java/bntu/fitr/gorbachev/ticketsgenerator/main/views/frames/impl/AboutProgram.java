package bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.impl;


import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.BaseDialog;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;

import javax.swing.*;
import java.awt.*;

/**
 * The class represents a dialog box object "About Program"
 *
 * @author Gorbachev I. D.
 * @version 21.04.2022
 */
public class AboutProgram extends BaseDialog {

    /**
     * @param frame class object {@link JFrame}
     */
    public AboutProgram(Window frame, PanelType type) {
        super(frame, type, "О программе");
        this.setModal(false);
    }

    /**
     * The method initializes the panel
     */
    @Override
    public void initDialog() {
        this.setBounds((int) getFrame().getBounds().getX(), (int) getFrame().getBounds().getY(), 600, 500);
        this.setResizable(false);
        this.add(PanelFactory.getInstance().createPanel(this, getPanelType()));
    }
}
