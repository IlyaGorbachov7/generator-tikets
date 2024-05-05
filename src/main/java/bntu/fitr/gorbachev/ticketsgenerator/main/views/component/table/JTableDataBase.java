package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ReflectionTableHelper;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@Getter
@Setter
public class JTableDataBase extends JTable {
    private Class<?> classTableView;

    private JPanel pnlTbl;

    @Builder
    public JTableDataBase(Class<?> clazz, JPanel p) {
        this.classTableView = clazz;
        this.pnlTbl = p;
        setBackground(new Color(225, 223, 223, 255));

        pnlTbl.setLayout(new BorderLayout(10, 10));
        pnlTbl.add(this);
        combine();
    }

    private void combine() {
        ReflectionTableHelper.checkRuntimeMistakes(classTableView);

        setModel(new AbstractTableModel() {
            private final String[] columnNames = ReflectionTableHelper.extractColumnName(classTableView);

            @Override
            public int getRowCount() {
                return 0;
            }

            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return null;
            }
        });
    }
}
