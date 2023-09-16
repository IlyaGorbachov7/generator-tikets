package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.abservers.RelatedComponentEvent;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.event.*;

@Slf4j
public class MyComboBoxUI extends MetalComboBoxUI {
    private final MyJCompoBox myCompoBox;

    public MyComboBoxUI(MyJCompoBox jCompoBox) {
        myCompoBox = jCompoBox;
        comboBox = jCompoBox;
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
                    log.debug("Selected Item : {}", comboBox.getSelectedItem());
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
                log.debug("Count item: {}", comboBox.getItemCount());
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (comboBox.getItemCount() > 0) {
                        ((MyMetalComboBoxEditor) MyComboBoxUI.this.comboBox.getEditor()).setRealListItem();
                    } else {
                        ((MyMetalComboBoxEditor) MyComboBoxUI.this.comboBox.getEditor()).setRealValue();
                    }
                    myCompoBox.fireRelatedComponentListener(new RelatedComponentEvent(myCompoBox));
                }
            }
        };
    }

    @Override
    protected ComboPopup createPopup() {
        return new MyPopup(myCompoBox);
    }

    public JButton getArrowButton() {
        return arrowButton;
    }

    public ComboPopup getPopup() {
        return popup;
    }
}
