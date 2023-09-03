package bntu.fitr.gorbachev.ticketsgenerator.main.views.component;

import javax.swing.*;

public class MyJCompoBox<E> extends JComboBox<E> {
    private final MyComboBoxUI<E> compoBoxUI = new MyComboBoxUI<E>();
    public MyJCompoBox() {
        super();
        setUI(compoBoxUI);
    }

    public JList<E> getJList(){
        return compoBoxUI.getJList();
    }
}
