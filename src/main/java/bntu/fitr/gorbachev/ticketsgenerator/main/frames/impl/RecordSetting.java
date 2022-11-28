package bntu.fitr.gorbachev.ticketsgenerator.main.frames.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.frames.BaseDialog;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelType;

import java.awt.*;

public class RecordSetting extends BaseDialog {


    public RecordSetting(Window owner, PanelType panelType) {
        super(owner, panelType, "Свойство записи");
    }

    @Override
    public void initDialog() {
        this.setBounds((int) getFrame().getBounds().getX(), (int) getFrame().getBounds().getY(), 600, 500);
        this.setResizable(false);
        this.add(PanelFactory.getInstance().createPanel(this, getPanelType()));
    }
}
