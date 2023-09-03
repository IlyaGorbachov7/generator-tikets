package bntu.fitr.gorbachev.ticketsgenerator.main.views.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.metal.MetalComboBoxUI;

public class MyComboBoxUI<E> extends MetalComboBoxUI {
    public MyComboBoxUI() {
        super();
    }
    public JList<E> getJList(){
        return (JList<E>) listBox;
    }
}
