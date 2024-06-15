package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.List;
import java.util.function.Function;

public class CombaBoxSupplierView extends JComboBox<Object> {

    @Getter
    @Setter
    private Function<Object, String> mapper;


    private List<?> listData;

    public CombaBoxSupplierView(Function<Object, String> mapper, List<?> listData) {
        setModel(new ComboBoxModelSupplierView(listData.toArray()));
//        setEditor(new MyMetalComboBoxEditor(mapper));
        setRenderer(new MyBasicComboBoxRenderer(mapper));
        this.mapper = mapper;
        this.listData = listData;
    }


    private final class ComboBoxModelSupplierView extends DefaultComboBoxModel<Object>{

        public ComboBoxModelSupplierView(Object[] items) {
            super(items);
        }

        @Override
        public Object getElementAt(int index) {
            return super.getElementAt(index);
        }
    }
}
