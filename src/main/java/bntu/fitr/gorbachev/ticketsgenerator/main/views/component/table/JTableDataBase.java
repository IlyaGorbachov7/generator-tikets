package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.reflectionapi.ReflectionListDataBaseHelper;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.UniversityModelTbl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ReflectionTableHelper;
import lombok.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

@Getter
@Setter
public class JTableDataBase extends JTable {
    private Class<?> classTableView;

    private JPanel pnlTbl;

    private Function<Object, List<?>> supplierDataList;

    @Builder
    public JTableDataBase(Class<?> clazz, JPanel p, Function<Object, List<?>> supplierDataList) {
        super(new RealizeTableModel(clazz, supplierDataList), new RealizeTableColumnModel());
        setAutoCreateColumnsFromModel(true);
        this.classTableView = clazz;
        this.pnlTbl = p;
        this.supplierDataList = supplierDataList;
        combine();
    }


    @Override
    protected void createDefaultRenderers() {
        super.createDefaultRenderers();
    }

    @Override
    protected void createDefaultEditors() {
        super.createDefaultEditors();
    }

    @Override
    protected ListSelectionModel createDefaultSelectionModel() {
        return super.createDefaultSelectionModel();
    }

    @Override
    protected JTableHeader createDefaultTableHeader() {
        return super.createDefaultTableHeader();
    }

    @Override
    public void createDefaultColumnsFromModel() {
        TableModel m = getModel();
        if (m != null) {
            // Remove any current columns
            TableColumnModel cm = getColumnModel();
            while (cm.getColumnCount() > 0) {
                cm.removeColumn(cm.getColumn(0));
            }

            // Create new columns from the data model info
            for (int i = 0; i < m.getColumnCount(); i++) {
                TableColumn newColumn = new RealizedTableColumn(i);
                newColumn.setCellRenderer(new RealizedCellRender(m.getColumnCount()));
                addColumn(newColumn);
            }
        }
        ;
    }


    private void combine() {
        pnlTbl.setLayout(new BorderLayout(10, 10));
        var scroll = new JScrollPane(new JScrollPane(this)); // this is how I made it possible to have a horizontal scroll
        pnlTbl.add(scroll, BorderLayout.CENTER);


        ReflectionTableHelper.checkRuntimeMistakes(classTableView);

        this.setRowMargin(1);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setGridColor(new Color(78, 157, 231));
        this.setShowGrid(true);
        this.getTableHeader().setReorderingAllowed(false);
    }

    private static class RealizeTableColumnModel extends DefaultTableColumnModel {
        public RealizeTableColumnModel() {
            super();
        }

        @Override
        public void addColumn(TableColumn aColumn) {
            super.addColumn(aColumn);
        }

        @Override
        public void removeColumn(TableColumn column) {
            super.removeColumn(column);
        }

        @Override
        public void moveColumn(int columnIndex, int newIndex) {
            super.moveColumn(columnIndex, newIndex);
        }

        @Override
        public void setColumnMargin(int newMargin) {
            super.setColumnMargin(newMargin);
        }

        @Override
        public int getColumnCount() {
            return super.getColumnCount();
        }

        @Override
        public Enumeration<TableColumn> getColumns() {
            return super.getColumns();
        }

        @Override
        public int getColumnIndex(Object identifier) {
            return super.getColumnIndex(identifier);
        }

        @Override
        public TableColumn getColumn(int columnIndex) {
            return super.getColumn(columnIndex);
        }

        @Override
        public int getColumnMargin() {
            return super.getColumnMargin();
        }

        @Override
        public int getColumnIndexAtX(int x) {
            return super.getColumnIndexAtX(x);
        }

        @Override
        public int getTotalColumnWidth() {
            return super.getTotalColumnWidth();
        }

        @Override
        public void setSelectionModel(ListSelectionModel newModel) {
            super.setSelectionModel(newModel);
        }

        @Override
        public ListSelectionModel getSelectionModel() {
            return super.getSelectionModel();
        }

        @Override
        public void setColumnSelectionAllowed(boolean flag) {
            super.setColumnSelectionAllowed(flag);
        }

        @Override
        public boolean getColumnSelectionAllowed() {
            return super.getColumnSelectionAllowed();
        }

        @Override
        public int[] getSelectedColumns() {
            return super.getSelectedColumns();
        }

        @Override
        public int getSelectedColumnCount() {
            return super.getSelectedColumnCount();
        }

        @Override
        public void addColumnModelListener(TableColumnModelListener x) {
            super.addColumnModelListener(x);
        }

        @Override
        public void removeColumnModelListener(TableColumnModelListener x) {
            super.removeColumnModelListener(x);
        }

        @Override
        public TableColumnModelListener[] getColumnModelListeners() {
            return super.getColumnModelListeners();
        }

        @Override
        protected void fireColumnAdded(TableColumnModelEvent e) {
            super.fireColumnAdded(e);
        }

        @Override
        protected void fireColumnRemoved(TableColumnModelEvent e) {
            super.fireColumnRemoved(e);
        }

        @Override
        protected void fireColumnMoved(TableColumnModelEvent e) {
            super.fireColumnMoved(e);
        }

        @Override
        protected void fireColumnSelectionChanged(ListSelectionEvent e) {
            super.fireColumnSelectionChanged(e);
        }

        @Override
        protected void fireColumnMarginChanged() {
            super.fireColumnMarginChanged();
        }

        @Override
        public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
            return super.getListeners(listenerType);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            super.propertyChange(evt);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            super.valueChanged(e);
        }

        @Override
        protected ListSelectionModel createSelectionModel() {
            return super.createSelectionModel();
        }

        @Override
        protected void recalcWidthCache() {
            super.recalcWidthCache();
        }
    }

    private static class RealizeTableModel extends AbstractTableModel {
        private final Class<?> classTableView;

        private Function<Object, List<?>> supplierDataList;

        private final String[] columnNames;

        private Object[][] data;

        private RealizeTableModel(@NonNull Class<?> clazz, Function<Object, List<?>> supplier) {
            classTableView = clazz;
            supplierDataList = supplier;

            columnNames = ReflectionTableHelper.extractColumnName(classTableView);
            data = ReflectionTableHelper.extractDataAndTransformToClass(supplierDataList.apply(UniversityModelTbl.class),
                    UniversityModelTbl.class);
        }
        // here don't should be such

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

    }

    private static class RealizedTableColumn extends TableColumn {
        public RealizedTableColumn(int modelIndex) {
            super(modelIndex);
            setHeaderRenderer(createDefaultHeaderRenderer());
        }

        public void setWidthSimple(int width) {
            this.width = width;
        }

//        @Override
//        protected TableCellRenderer createDefaultHeaderRenderer() {
//            TableCellRenderer cellRenderer = new TableCellRenderer() {
//                @Override
//                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                    System.err.println("-------" + value);
//                    return null;
//                }
//            };
//            return cellRenderer;
//        }
    }

    private static class RealizedCellRender extends DefaultTableCellRenderer {
        private final int[] columnsMaxWidth;
        private final int[] columnsCurrentWidth;

        public RealizedCellRender(int countColumns) {
            columnsMaxWidth = new int[countColumns];
            columnsCurrentWidth = new int[countColumns];
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, columnIndex);
            RealizedTableColumn column = (RealizedTableColumn) table.getColumnModel().getColumn(columnIndex);
            JLabel tmp = new JLabel(value.toString());
            columnsCurrentWidth[columnIndex] = tmp.getPreferredSize().width;
            if (columnsCurrentWidth[columnIndex] > columnsMaxWidth[columnIndex]) {
                columnsMaxWidth[columnIndex] = columnsCurrentWidth[columnIndex];
                column.setPreferredWidth(columnsMaxWidth[columnIndex]);
                column.setWidthSimple(columnsMaxWidth[columnIndex]);
            }
            return this;
        }
    }

}
