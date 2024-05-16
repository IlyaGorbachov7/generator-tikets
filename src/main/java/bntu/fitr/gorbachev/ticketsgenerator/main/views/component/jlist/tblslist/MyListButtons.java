package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.reflectionapi.ReflectionListDataBaseHelper;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.JTableDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.KeyForViewUI;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.ModelTableViewSupplier;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class MyListButtons extends JPanel {

    private final JPanel rootPnlForTable;
    private final Map<JButton, KeyForViewUI> mapBtnForKeyViewUI;
    private final JButton[] arrBtn;

    @Builder
    public MyListButtons(ModelTableViewSupplier[] modelTableViewSuppliers,
                         JPanel rootPnl) {
        this.setLayout(new GridBagLayout());

        var classesTableView = Arrays.stream(modelTableViewSuppliers).map(ModelTableViewSupplier::getClazzModelView).toArray(Class[]::new);
        ReflectionListDataBaseHelper.checkClassesOnTheModelViewTable(classesTableView);
        // Farther means that all right
        this.rootPnlForTable = rootPnl;
        this.arrBtn = new JButton[modelTableViewSuppliers.length];
        ActionListener handlerBtnAction = this.new HandlerButtonActionListener();
        TableSelectedRowsListener handlerSelection = this.new HandlerSelectionRowsListener();

        IntIterator iIter = IntIterator.builder().build();
        mapBtnForKeyViewUI = Arrays.stream(modelTableViewSuppliers)
                .map(modelTableViewSupplier -> {
                    JPanel p = new JPanel();
                    JButton btn = new JButton(ReflectionListDataBaseHelper.extractTableViewName(modelTableViewSupplier.getClazzModelView()));
                    Class<?> clazzTblView = modelTableViewSupplier.getClazzModelView();
                    Function<Object, List<?>> supplierDataList = modelTableViewSupplier.getSupplierData();
                    return Map.entry(btn, // key
                            KeyForViewUI.builder() // value
                                    .index(iIter.cur()).btn(btn)
                                    .tbl(JTableDataBase.builder()
                                            .clazz(clazzTblView).p(p)
                                            .btn(btn)
                                            .supplierDataList(supplierDataList).build()).build());
                }).peek(entry -> {
                    arrBtn[iIter.cur()] = entry.getKey();
                })
                .peek(entry -> { // initialization GUI bloke
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.fill = GridBagConstraints.BOTH;
                    gbc.gridx = 0;
                    gbc.gridy = iIter.incr(); // increment iteration
                    gbc.weightx = 1;
                    gbc.weighty = 2.5;
                    this.add(entry.getValue().getBtn(), gbc);
                    entry.getValue().getBtn().setEnabled(false);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v, v1) -> {
                    throw new AssertionError("keys should already be unique");
                }, LinkedHashMap::new));

        mapBtnForKeyViewUI.forEach((btn, keyView) -> {
            btn.addActionListener(handlerBtnAction);
            keyView.getTbl().addTableSelectedRowsListener(handlerSelection);
        });

        defaultSelectedBtn();
    }

    private class HandlerButtonActionListener implements ActionListener {
        KeyForViewUI cur;
        KeyForViewUI prev;

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$");
            JButton btnSource = (JButton) e.getSource();
            KeyForViewUI v = Objects.requireNonNull(mapBtnForKeyViewUI.get(btnSource));
            prev = cur;
            cur = v;

            if (prev != null) prev.getBtn().setBackground(Color.LIGHT_GRAY);
            setSelectedColorBtn(cur.getBtn());
            rootPnlForTable.removeAll();
            rootPnlForTable.add(cur.getTbl().getPnlTbl());
            rootPnlForTable.repaint();
            rootPnlForTable.validate();
        }
    }

    private class HandlerSelectionRowsListener implements TableSelectedRowsListener {

        @Override
        public void perform(TableSelectedRowsEvent event) {
            int index = mapBtnForKeyViewUI.get(event.getBtn()).getIndex();
            if (index < mapBtnForKeyViewUI.size() - 1) {
                arrBtn[index + 1].setEnabled(true);
            }

        }
    }

    protected void defaultSelectedBtn() {
        rootPnlForTable.add((mapBtnForKeyViewUI.get(arrBtn[0]).getTbl().getPnlTbl()));
        setSelectedColorBtn(arrBtn[0]);
        arrBtn[0].setEnabled(true);
    }

    protected void setSelectedColorBtn(@NonNull JButton btn) {
        btn.setBackground(new Color(84, 151, 213));
    }

    @Builder
    private static final class IntIterator {
        int i = 0;

        public int incr() {
            int old = i;
            i++;
            return old;
        }

        public int discr() {
            int old = i;
            i--;
            return old;
        }

        public int cur() {
            return i;
        }
    }
}
