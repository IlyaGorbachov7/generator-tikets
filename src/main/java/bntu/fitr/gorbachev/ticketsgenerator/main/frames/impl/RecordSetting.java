package bntu.fitr.gorbachev.ticketsgenerator.main.frames.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.WriterTicketProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.frames.BaseDialog;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.PanelType;
import bntu.fitr.gorbachev.ticketsgenerator.main.panels.impl.RecordSettingPanel;

import java.awt.*;

public class RecordSetting extends BaseDialog {
    private RecordSettingPanel panel;

    public RecordSetting(Window owner, PanelType panelType) {
        super(owner, panelType, "Свойство записи");
        this.setModal(true);
    }

    @Override
    public void initDialog() {
        this.setResizable(false);
        panel = (RecordSettingPanel) PanelFactory.getInstance().createPanel(this, getPanelType());
        this.add(panel);
        // setting dialog size == size all regarding components preferred sizes of all components
        //https://stackoverflow.com/questions/13046508/calculate-sizes-of-components-of-jdialog-with-total-size-set-before-making-visib
        this.setBounds((int)  getFrame().getBounds().getX(), (int) getFrame().getBounds().getY(), 250, 300);
        this.pack();
        this.validate();
    }

    public WriterTicketProperty getWriterTicketProperty() {
        return panel.getProperty();
    }

    public void setWriterTicketProperty(WriterTicketProperty writerTicketProperty) {
        panel.setProperty(writerTicketProperty);
    }
}
