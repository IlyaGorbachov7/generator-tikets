package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxEditor;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.util.function.Function;

public class MyMetalComboBoxEditor extends MetalComboBoxEditor {
    Object oldValue;

    Function<Object, String> mapper;

    public MyMetalComboBoxEditor(Function<Object, String> mapper) {
        super();
        this.mapper = mapper;
    }

    @Override
    public Component getEditorComponent() {
        return super.getEditorComponent();
    }

    @Override
    protected JTextField createEditorComponent() {
        return super.createEditorComponent();
    }

    @Override
    public void setItem(Object anObject) {
        String text;
        if (anObject instanceof String) {
            oldValue = anObject;
            editor.setText((String) oldValue);
            System.out.println("setItem: " + oldValue);
            return;
        }
        if (anObject != null) {
            text = mapper.apply(anObject);
            if (text == null) {
                text = "";
            }
            oldValue = text;
            editor.setText(text);
            System.out.println("setItem: " + oldValue);
        } else {
            oldValue = editor.getText();
            System.out.println("setItem: " + oldValue);
        }
    }

    public void setItemByPassedKeyEnter(Object anObject) {
        String text;
        if (anObject instanceof String) {
            oldValue = anObject;
            System.out.println("setItem: " + oldValue);
            return;
        }
        if (anObject != null) {
            text = mapper.apply(anObject);
            if (text == null) {
                text = "";
            }
            oldValue = text;
            System.out.println("setItem: " + oldValue);
        } else {
            oldValue = editor.getText();
            System.out.println("setItem: " + oldValue);
        }
    }

    public void setRealListItem() {
        editor.setText((String) oldValue);
        System.out.println("setRealListItem: " + oldValue);
    }

    public void setRealValue() {
        oldValue = editor.getText();
    }

    @Override
    public Object getItem() {
        System.out.println("getItem: " + oldValue);
        return oldValue;
    }

    @Override
    public void selectAll() {
        super.selectAll();
    }

    @Override
    public void focusGained(FocusEvent e) {
        super.focusGained(e);
    }

    @Override
    public void focusLost(FocusEvent e) {
        super.focusLost(e);
    }

    @Override
    public void addActionListener(ActionListener l) {
        super.addActionListener(l);
    }

    @Override
    public void removeActionListener(ActionListener l) {
        super.removeActionListener(l);
    }
}
