package bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.BaseDialog;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;

import java.awt.*;

public class InputParametersDialog extends BaseDialog {

    public InputParametersDialog(Window owner, PanelType panelType) {
        super(owner, panelType);
    }

    @Override
    public void initDialog() {
        this.setContentPane(PanelFactory.getInstance().createPanel(this,PanelType.INPUT_PARAM_DB));
        this.setSize(780, 400);
        this.setResizable(true);
    }
}
