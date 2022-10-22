package bntu.fitr.gorbachev.ticketsgenerator.main.frames.impl;


import bntu.fitr.gorbachev.ticketsgenerator.main.frames.BaseDialog;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelType;

import javax.swing.*;
import java.awt.*;

/**
 * The class represents a dialog box object "About Author"
 *
 * @author Gorbachev I. D.
 * @version 20.04.2022
 */
public class AboutAuthor extends BaseDialog {

    private final Window frame;
    private final PanelType type;

    /**
     * @param frame class object {@link JFrame}
     */
    public AboutAuthor(Window frame, PanelType type) {
        super(frame, "О авторе");
        this.setModal(false);
        this.frame = frame;
        this.type = type;
        this.initDialog();
    }

    /**
     * The method initializes the panel
     */
    @Override
    public void initDialog() {
        System.out.println(frame.getBounds());
        this.setBounds((int) frame.getBounds().getX(),
                (int) frame.getBounds().getY(), 250, 450);
        this.setResizable(false);
        add(PanelFactory.getInstance().createPanel(this, type));
    }

}
