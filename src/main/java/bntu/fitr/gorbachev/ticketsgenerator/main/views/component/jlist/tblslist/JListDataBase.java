package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.JTableDataBase;
import lombok.Builder;
import lombok.Getter;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class JListDataBase extends JList<String> {
    // outside pointer
    JPanel rootPnlForTable;

    // inside pointers on the panel
    JPanel[] tablePanels;
    Class<?>[] classesTableView;
    Map<Class<?>, JTableDataBase> jTblsDataTable;

    public JListDataBase(ListModel<String> dataModel) {
        super(dataModel);
    }

    @Builder
    public JListDataBase(String[] listData) {
        super(listData);
    }
    // Теперь будем работать над reflection api
    // TODO: В классах, которые будут выступать в качестве map-ингом таблицы в бд и view мы создадим аннотацию, которая позволит нам указать название таблицы, то есть в Jlist-е название !
    @SuppressWarnings("unchecked")
    public JListDataBase(Class<?>[] classesTableView,
                         JPanel rootPnl) { // root panel - is panel on which displayed content other panels for dynamic changes ui-panel
        this.rootPnlForTable = rootPnl;
        this.classesTableView = classesTableView;
        jTblsDataTable = Map.ofEntries(
                Arrays.stream(classesTableView)
                        .map((classTblView) -> Map.entry(classTblView,
                                JTableDataBase.builder().classTableView(classTblView).build()))
                        .toArray(Map.Entry[]::new));
    }
}
