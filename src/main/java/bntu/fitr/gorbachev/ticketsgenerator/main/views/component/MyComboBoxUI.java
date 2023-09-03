package bntu.fitr.gorbachev.ticketsgenerator.main.views.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.metal.MetalComboBoxUI;

public class MyComboBoxUI extends MetalComboBoxUI {
    private MyJCompoBox jCompoBox;
    public MyComboBoxUI(MyJCompoBox jCompoBox) {
        super();
        this.jCompoBox = jCompoBox;
    }

    public JList<Object> getJList() {
        return listBox;
    }
}
