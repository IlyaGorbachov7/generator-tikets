package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.AppThemeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ReflectionTableHelper;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private Function<Object[], DefaultTableCellRenderer> supplierCellRender;

    @Builder
    public JTableDataBase(Class<?> clazz, JPanel p, JButton btn, Function<Object, List<?>> supplierDataList,
                          Function<Object, Object> supplierCreate, Function<Object, Object> supplierUpdate,
                          Function<Object, List<?>> supplierDelete,
                          RelatedTblDataBase relatedMdlTbl, Function<Object[], DefaultTableCellRenderer> supplierCellRender) {
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
        this.supplierCellRender = Objects.requireNonNullElse(supplierCellRender, (args) -> new RealizedCellRender((Integer) args[0]));
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
                System.out.println("___________________________dfsfsf :: " + e.getFirstIndex());
                System.out.println("___________________________dfsfsf :: " + e.getLastIndex());
                System.out.println("___________________________dfsfsf :: " + e.getValueIsAdjusting());
                if (sel) {
                    if (isSel && e.getValueIsAdjusting()) {
                        clearSelection();
                        sel = false;
                        isSel = false;
                        list = new boolean[]{false, false};
                        i = 0;
                        return;
                    }
                    if (i <= 1) {
                        list[i] = e.getValueIsAdjusting();
                        if (i == 1) {
                            if (list[i] == list[i - 1]) {
                                System.out.println("clear");
                                isSel = true;
                            }
                        }
                        ++i;
                    }
//                if(!e.getValueIsAdjusting() && (e.getFirstIndex() == e.getFirstIndex())){
//                    clearSelection();
//                }
                }
            }
        });
        this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            /**
             * IT very important notice!!!!
             * This possibility allowed me to define early  pre-selected item for adjusting selections with desirable result
             * See :
             * When  user clicked on the item in table. Firstly invoke handler: {@link ListSelectionListener#valueChanged(ListSelectionEvent)}
             * this event contains property: isAdjusting. This property invoked firstly when item on the table
             * is selected. isAdjusting == true. This event already contains new selected rows
             * <p>
             * Further invoke {@link TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)}.
             *</p>
             * then after that it is called again {@link ListSelectionListener#valueChanged(ListSelectionEvent)},
             * however with new value for property: isAdjusting == false.
             * <p>
             * And now again invoked {@link TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)}.
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.out.print("first:" + e.getFirstIndex());
                System.out.print("  last:" + e.getLastIndex());
                System.out.print("  valueisAdjusting: " + e.getValueIsAdjusting());
                isSel = false;
                list = new boolean[]{false, false};
                i = 0;
                sel = true;
                fireSelectedRows(TableSelectedRowsEvent.builder().eventSource(e)
                        .classTableView(classTableView)
                        .selectedItems(((RealizeTableModel) dataModel)
                                .getSelectedObjects(classTableView, getSelectedRows()))
                        .selectedRows(getSelectedRows())
                        .isAdjusting(e.getValueIsAdjusting())
                        .btn(btn).build());
            }

        });
    }

    boolean sel = false;
    boolean isSel = false;
    boolean[] list = new boolean[]{false, false};
    int i = 0;

    public void setSupplierCellRender(Function<Object[], DefaultTableCellRenderer> supplierCellRender) {
        this.supplierCellRender = supplierCellRender;
        createDefaultColumnsFromModel();
    }

    private void combine() {
        pnlTbl.setLayout(new BorderLayout(10, 10));
        var scroll = new JScrollPane(new JScrollPane(this)); // this is how I made it possible to have a horizontal scroll
        pnlTbl.add(scroll, BorderLayout.CENTER);

        // Added scroll panel for update UI
        AppThemeManager.addThemeChangerListener(() -> scroll);

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
                newColumn.setCellRenderer(supplierCellRender.apply(new Object[]{m.getColumnCount()}));
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
        Object[] items = getSelectedItems();
        return items.length == 1 ? items[0] : null;
    }

    public Object[] getSelectedItems() {
        return ((RealizeTableModel) dataModel).getSelectedObjects(getSelectedRows());
    }

    public void updateItem(Object selectedItem) {
        if (selectedItem.getClass() != classTableView) {
            throw new IllegalArgumentException();
        }
        ((RealizeTableModel) dataModel).updateItem(selectedItem);
    }


    private static class RealizeTableColumnModel extends DefaultTableColumnModel {
    }

    public static class RealizeTableModel extends AbstractTableModel {
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

        public Object[] getSelectedObjects(int[] selectedIndexes) {
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

    public static class RealizedTableColumn extends TableColumn {
        public RealizedTableColumn(int modelIndex) {
            super(modelIndex);
            setHeaderRenderer(createDefaultHeaderRenderer());
        }

        public void setWidthSimple(int width) {
            this.width = width;
        }
    }

    public static class RealizedCellRender extends DefaultTableCellRenderer {
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
