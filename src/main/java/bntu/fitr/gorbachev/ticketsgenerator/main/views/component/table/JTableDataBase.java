package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ReflectionTableHelper;
import lombok.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;

@Getter
public class JTableDataBase extends JTable {
    private final Class<?> classTableView;

    private final JPanel pnlTbl;

    private final JButton btn;

    private final Function<Object, List<?>> supplierDataList;

    private final Function<Object, Object> supplierCreate;

    private final Function<Object, Object> supplierUpdate;

    private final Function<Object, List<?>> supplierDelete;

    private final RelatedTblDataBase relatedMdlTbl;

    private final List<TableSelectedRowsListener> handlers;

    @Builder
    public JTableDataBase(Class<?> clazz, JPanel p, JButton btn, Function<Object, List<?>> supplierDataList,
                          Function<Object, Object> supplierCreate, Function<Object, Object> supplierUpdate,
                          Function<Object, List<?>> supplierDelete,
                          RelatedTblDataBase relatedMdlTbl) {
        super(new RealizeTableModel(clazz, supplierDataList, supplierCreate, supplierUpdate, supplierDelete),
                new RealizeTableColumnModel());
        setAutoCreateColumnsFromModel(true);
        this.classTableView = clazz;
        this.pnlTbl = p;
        this.btn = btn;
        this.supplierDataList = supplierDataList;
        this.supplierCreate = supplierCreate;
        this.supplierUpdate = supplierUpdate;
        this.supplierDelete = supplierDelete;
        this.relatedMdlTbl = relatedMdlTbl;

        handlers = new ArrayList<>();
        combine();
        this.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
            @Override
            public void columnAdded(TableColumnModelEvent e) {
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
                System.out.println("___________________________dfsfsf");
            }
        });
        this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.out.println("first:" + e.getFirstIndex());
                System.out.println("last:" + e.getLastIndex());
                System.out.println("valueisAdjusting: " + e.getValueIsAdjusting());
                if (!e.getValueIsAdjusting()) {
                    fireSelectedRows(TableSelectedRowsEvent.builder().eventSource(e)
                            .classTableView(classTableView)
                            .selectedItems(((RealizeTableModel) dataModel)
                                    .getSelectedObjects(classTableView, getSelectedRows()))
                            .btn(btn).build());
                }
            }

        });
    }

    private void combine() {
        pnlTbl.setLayout(new BorderLayout(10, 10));
        var scroll = new JScrollPane(new JScrollPane(this)); // this is how I made it possible to have a horizontal scroll
        pnlTbl.add(scroll, BorderLayout.CENTER);


        ReflectionTableHelper.checkRuntimeMistakes(classTableView);

        this.setRowMargin(1);
        this.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.setGridColor(new Color(78, 157, 231));
        this.setShowGrid(true);
        this.getTableHeader().setReorderingAllowed(false);
        this.setRowHeight(25);
        this.setPreferredScrollableViewportSize(this.getPreferredSize());
    }

    public void addTableSelectedRowsListener(TableSelectedRowsListener listener) {
        handlers.add(listener);
    }

    public void removeTableSelectedRowsListener(TableSelectedRowsListener listener) {
        handlers.remove(listener);
    }

    private void fireSelectedRows(TableSelectedRowsEvent event) {
        handlers.forEach(handler -> handler.perform(event));
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

    public void performSetData() {
        ((RealizeTableModel) dataModel).performSetData();
        createDefaultColumnsFromModel();
    }

    public void createItem(String s) {
        ((RealizeTableModel) dataModel).performCreateItem(s);
    }

    public void deleteItem() {
        ((RealizeTableModel) dataModel).deleteItems(getSelectedRows());
    }

    public Object getSelectedItem() {
        return getSelectedItems()[0];
    }

    public Object[] getSelectedItems(){
        return ((RealizeTableModel) dataModel).getSelectedObjects(getSelectedRows());
    }

    public void updateItem(Object selectedItem) {
        if(selectedItem.getClass() != classTableView){
            throw new IllegalArgumentException();
        }
        ((RealizeTableModel) dataModel).updateItem(selectedItem);
    }


    private static class RealizeTableColumnModel extends DefaultTableColumnModel {
    }

    private static class RealizeTableModel extends AbstractTableModel {
        private final String[] EMPTY = new String[0];
        private final Class<?> classTableView;

        private final Function<Object, List<?>> supplierDataList;

        private final Function<Object, Object> supplierCreate;

        private final Function<Object, Object> supplierUpdate;

        private final Function<Object, List<?>> supplierDelete;


        private String[] columnNames;

        @Getter
        private Object[][] data;

        private RealizeTableModel(@NonNull Class<?> clazz, Function<Object, List<?>> supplier,
                                  Function<Object, Object> supplierCreate, Function<Object, Object> supplierUpdate,
                                  Function<Object, List<?>> supplierDelete) {
            classTableView = clazz;
            supplierDataList = supplier;
            this.supplierCreate = supplierCreate;
            this.supplierUpdate = supplierUpdate;
            this.supplierDelete = supplierDelete;

            columnNames = EMPTY;
            data = new Object[0][0];
        }


        public void performSetData() {
            data = ReflectionTableHelper.extractDataAndTransformToClass(supplierDataList.apply(classTableView), classTableView);
            if (columnNames == EMPTY) {
                columnNames = ReflectionTableHelper.extractColumnName(classTableView);
            }
        }

        public void performCreateItem(String value) {
            supplierCreate.apply(TransmissionObject.builder().clazzMdlTbl(classTableView)
                    .dataValue(new Object[]{value}).build());
        }

        public void deleteItems(int[] selectedIndexes) {
            Object[] selectedObjects = getSelectedObjects(classTableView, selectedIndexes);
            supplierDelete.apply(TransmissionObject.builder().clazzMdlTbl(classTableView)
                    .dataValue(selectedObjects).build());
        }

        public void updateItem(Object selectedItem) {
            supplierUpdate.apply(selectedItem);
        }

        public Object[] getSelectedObjects(int[] selectedIndexes){
            return getSelectedObjects(classTableView, selectedIndexes);
        }
        // here don't should be such

        private Object[] getSelectedObjects(Class<?> clazz, int[] indexesSelectedRows) {
            Object[][] data = new Object[indexesSelectedRows.length][getColumnCount()];
            int i = 0;
            for (int indexSelectedRow : indexesSelectedRows) {
                for (int j = 0; j < data[i].length; j++) {
                    data[i][j] = getValueAt(indexSelectedRow, j);
                }
                i++;
            }
            return ReflectionTableHelper.convertDataRowsToDataClass(data, clazz);
        }

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
            return data[rowIndex][columnIndex];
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
