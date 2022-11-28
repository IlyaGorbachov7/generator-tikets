package bntu.fitr.gorbachev.ticketsgenerator.main.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.panels.BasePanel;

import javax.swing.*;
import java.awt.*;

public class RecordSettingPanel extends BasePanel {
    public RecordSettingPanel(Window rootWindow) {
        super(rootWindow);
    }

    @Override
    public void initPanel() {
        this.add(new JLabel("Привет Илья !"));
    }

    @Override
    public void setComponentsListeners() {

    }

    @Override
    public void setConfigComponents() {

    }
}
