package bntu.fitr.gorbachev.ticketsgenerator.main.views.component;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;

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
            text = ((UniversityDTO) anObject).getName();
            if (text == null) {
                text = "";
            }
            oldValue = anObject;
            System.out.println("setItem: " + oldValue);
            editor.setText(text);
        } else {
            oldValue = editor.getText();
            System.out.println("setItem: " + oldValue);
        }
    }

    @Override
    public Object getItem() {
        Object newValue = editor.getText();
        if (oldValue != null && !(oldValue instanceof String)) {
            // The original value is not a string. Should return the value in it's
            // original type.
            if (newValue.equals(mapper.apply(oldValue))) {
                System.out.println("getItem: " + oldValue);
                return oldValue;
            }
        }
        System.out.println("getItem: " + oldValue);
        return newValue;
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
