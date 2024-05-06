package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.reflectionapi.ReflectionListDataBaseHelper;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.UniversityModelTbl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ReflectionTableHelper;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public class JTableDataBase extends JTable {
    private Class<?> classTableView;

    private JPanel pnlTbl;

    private Function<Object, List<?>> supplierDataList;

    @Builder
    public JTableDataBase(Class<?> clazz, JPanel p, Function<Object, List<?>> supplierDataList) {
        this.classTableView = clazz;
        this.pnlTbl = p;
        this.supplierDataList = supplierDataList;

        pnlTbl.setLayout(new BorderLayout(10, 10));
        var scroll = new JScrollPane(new JScrollPane(this)); // this is how I made it possible to have a horizontal scroll
        pnlTbl.add(scroll, BorderLayout.CENTER);

        combine();
    }

    private void combine() {
        ReflectionTableHelper.checkRuntimeMistakes(classTableView);

        this.setRowMargin(1);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setGridColor(new Color(78, 157, 231));
        this.setShowGrid(true);
        setModel(new AbstractTableModel() {
            private final String[] columnNames = ReflectionTableHelper.extractColumnName(classTableView);
            // here don't should be such
            private Object[][] data = ReflectionTableHelper.extractDataAndTransformToClass(supplierDataList
                    .apply(UniversityModelTbl.class), UniversityModelTbl.class);

            @Override
            public int getRowCount() {
                return data.length;
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
                try {
                    // This is temporary handler exception by reason, that other model view table contaons by 2-column,
                    // however universityModelView contains only 1-column. Now all tables used output university database rows
                    return data[rowIndex][columnIndex];
                } catch (RuntimeException ignored) {
                    return ReflectionListDataBaseHelper.extractTableViewName(classTableView);
                }
//                return data[rowIndex][columnIndex];
            }

        });


        this.setColumnModel( new DefaultTableColumnModel(){

        });
    }

}
