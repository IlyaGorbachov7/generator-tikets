package bntu.fitr.gorbachev.ticketsgenerator.main.views.component;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import sun.reflect.misc.MethodUtil;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxEditor;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.lang.reflect.Method;

public class MyMetalComboBoxEditor extends MetalComboBoxEditor {
    Object oldValue;
    public MyMetalComboBoxEditor() {
        super();

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
        if(anObject instanceof String){
            oldValue = anObject;
            return;
        }
        if ( anObject != null )  {
            text = ((UniversityDTO)anObject).getName();
            if (text == null) {
                text = "";
            }
            oldValue = anObject;
        } else {
            text = "";
        }
        // workaround for 4530952
//        if (! text.equals(editor.getText())) {
//            editor.setText(text);
//        }
    }

    @Override
    public Object getItem() {
        Object newValue = editor.getText();

        if (oldValue != null && !(oldValue instanceof String))  {
            // The original value is not a string. Should return the value in it's
            // original type.
            if (newValue.equals(((UniversityDTO)oldValue).getName()))  {
                return oldValue;
            }
        }
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
