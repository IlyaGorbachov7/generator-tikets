package bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.frames.BaseDialog;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.PanelType;

import java.awt.*;

public class InputParametersDialog extends BaseDialog {

    public InputParametersDialog(Window owner, PanelType panelType) {
        super(owner, panelType, Localizer.get("frame.title.setting.input"));
//        this.setModal(true);
    }

    @Override
    public void initDialog() {
        this.setBounds((int) getFrame().getBounds().getX(), (int) getFrame().getBounds().getY(), 1080, 400);
        this.setContentPane(PanelFactory.getInstance().createPanel(this, PanelType.INPUT_PARAM_DB));
        this.setResizable(true);
    }
}
