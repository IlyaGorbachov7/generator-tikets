package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.event.*;

public class MyComboBoxUI extends MetalComboBoxUI {

    public MyComboBoxUI(MyJCompoBox jCompoBox) {
        super();
        installUI(jCompoBox);
        jCompoBox.getEditor().getEditorComponent().addKeyListener(this.createKeyListener());
    }

    @Override
    public ListDataListener createListDataListener() {
        return new ListDataHandler() {
            @Override
            public void contentsChanged(ListDataEvent e) {
                if (!(e.getIndex0() == -1 && e.getIndex1() == -1)) {
                    isMinimumSizeDirty = true;
                    comboBox.revalidate();
                }
                if (comboBox.isEditable() && editor != null) {
                    ((MyMetalComboBoxEditor) comboBox.getEditor())
                            .setItemByPassedKeyEnter(comboBox.getSelectedItem());
                    System.out.println("Selected item : " + comboBox.getSelectedItem());
                    System.out.println("Selected item : " + comboBox.getSelectedItem().getClass());
                }
                comboBox.repaint();
            }
        };
    }

    @Override
    public KeyListener createKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println(comboBox.getItemCount());
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (comboBox.getItemCount() > 0) {
                        ((MyMetalComboBoxEditor) MyComboBoxUI.this.comboBox.getEditor()).setRealListItem();
                    } else {
                        ((MyMetalComboBoxEditor) MyComboBoxUI.this.comboBox.getEditor()).setRealValue();
                    }
                }
            }
        };
    }

    public JButton getArrowButton() {
        return arrowButton;
    }

    public ComboPopup getPopup() {
        return popup;
    }
}
