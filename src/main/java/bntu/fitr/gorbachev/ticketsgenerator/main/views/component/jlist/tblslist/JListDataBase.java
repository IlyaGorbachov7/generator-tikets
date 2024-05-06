package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.reflectionapi.ReflectionListDataBaseHelper;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.JTableDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.KeyForViewUI;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.ModelTableViewSupplier;
import lombok.Builder;
import lombok.Getter;

import javax.swing.*;
import java.util.*;
import java.util.function.Function;

@Getter
public class JListDataBase extends JList<String> {
    // outside pointer
    private final JPanel rootPnlForTable;

    private final Map<KeyForViewUI, JTableDataBase> jTblsDataTable;

    // Теперь будем работать над reflection api
    // TODO: В классах, которые будут выступать в качестве map-ингом таблицы в бд и view мы создадим аннотацию, которая позволит нам указать название таблицы, то есть в Jlist-е название !
    @Builder
    @SuppressWarnings("unchecked")
    public JListDataBase(ModelTableViewSupplier[] modelTableViewSuppliers,
                         JPanel rootPnl) { // root panel - is panel on which displayed content other panels for dynamic changes ui-panel
        // Checking point
        var classesTableView = Arrays.stream(modelTableViewSuppliers).map(ModelTableViewSupplier::getClazzModelView).toArray(Class[]::new);
        ReflectionListDataBaseHelper.checkClassesOnTheModelViewTable(classesTableView);
        // Farther means that all right
        this.rootPnlForTable = rootPnl;
        jTblsDataTable = Map.ofEntries(
                Arrays.stream(modelTableViewSuppliers)
                        .map(modelTableViewSupplier -> {
                            JPanel p = new JPanel();
                            Class<?> clazzTblView = modelTableViewSupplier.getClazzModelView();
                            Function<Object, List<?>> supplierDataList = modelTableViewSupplier.getSupplierData();
                            return Map.entry(
                                    KeyForViewUI.builder().clazzModelTbl(clazzTblView).pnlTbl(p).build(),
                                    JTableDataBase.builder().clazz(clazzTblView).p(p)
                                            .supplierDataList(supplierDataList).build());
                        })
                        .peek(entry -> {
                            rootPnlForTable.add(entry.getValue().getPnlTbl());
                        })
                        .toArray(Map.Entry[]::new));
        setModel(new AbstractListModel<>() {
            private final String[] tblNames = ReflectionListDataBaseHelper.extractTableViewName(classesTableView);

            @Override
            public int getSize() {
                return tblNames.length;
            }

            @Override
            public String getElementAt(int index) {
                return tblNames[index];
            }
        });
    }
}
