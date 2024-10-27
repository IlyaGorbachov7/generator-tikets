package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.util.function.Function;


public class MyBasicComboBoxRenderer extends BasicComboBoxRenderer {
    private final Function<Object, String> mapper;

    public MyBasicComboBoxRenderer(Function<Object, String> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Icon) {
            setIcon((Icon) value);
        } else {
            setText((value == null) ? "" : mapper.apply(value));
        }
        return this;
    }


}


