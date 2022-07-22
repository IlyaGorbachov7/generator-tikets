package frames.impl;


import frames.BaseDialog;
import panels.PanelFactory;
import panels.PanelType;

import javax.swing.*;
import java.awt.*;

/**
 * The class represents a dialog box object "About Program"
 *
 * @author Gorbachev I. D.
 * @version 21.04.2022
 */
public class AboutProgram extends BaseDialog {

    private final Window frame;
    private final PanelType type;

    /**
     * @param frame class object {@link JFrame}
     */
    public AboutProgram(Window frame, PanelType type) {
        super(frame, "О программе");
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
        this.setBounds((int) frame.getBounds().getX(), (int) frame.getBounds().getY(), 600, 500);
        this.setResizable(false);
        this.add(PanelFactory.getInstance().createPanel(this, type));
    }
}
