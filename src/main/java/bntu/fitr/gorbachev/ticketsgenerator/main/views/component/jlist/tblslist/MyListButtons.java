package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.reflectionapi.ReflectionListDataBaseHelper;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.JTableDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.KeyForViewUI;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.ModelTableViewSupplier;
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
                    Function<Object, Object> supplierCreate = modelTableViewSupplier.getSupplierCreate();
                    Function<Object, Object> supplierUpdate = modelTableViewSupplier.getSupplierUpdate();
                    Function<Object, List<?>> supplierDelete = modelTableViewSupplier.getSupplierDelete();

                    return Map.entry(btn, // key
                            KeyForViewUI.builder() // value
                                    .index(iIter.cur()).btn(btn)
                                    .tbl(JTableDataBase.builder()
                                            .clazz(clazzTblView).p(p)
                                            .btn(btn)
                                            .supplierDataList(supplierDataList)
                                            .supplierCreate(supplierCreate)
                                            .supplierUpdate(supplierUpdate)
                                            .supplierDelete(supplierDelete).build()).build());
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

    private JButton selectedBtn;

    private class HandlerButtonActionListener implements ActionListener {
        JButton cur;
        JButton prev;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (cur == null) cur = arrBtn[0]; // initialization 1 раз
            JButton btnSource = (JButton) e.getSource();
            prev = cur;
            cur = btnSource;
            selectedBtn = cur;
            KeyForViewUI v = Objects.requireNonNull(mapBtnForKeyViewUI.get(cur));
            v.getTbl().performSetData();
            if (prev != null) prev.setBackground(Color.LIGHT_GRAY);
            setSelectedColorBtn(cur);
            rootPnlForTable.removeAll();
            rootPnlForTable.add(v.getTbl().getPnlTbl());
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
        selectedBtn = arrBtn[0];
        rootPnlForTable.add((mapBtnForKeyViewUI.get(selectedBtn).getTbl().getPnlTbl()));
        selectedBtn.setEnabled(true);
    }

    protected void setSelectedColorBtn(@NonNull JButton btn) {
        btn.setBackground(new Color(84, 151, 213));
    }

    public void deSelectInclude() {
        List<JButton> btns = new ArrayList<>(Arrays.asList(arrBtn));
        Collections.reverse(btns);
        for (JButton btn : btns) {
            KeyForViewUI value = mapBtnForKeyViewUI.get(btn);
            if (btn != selectedBtn) {
                btn.setEnabled(false);
                btn.setBackground(Color.WHITE);
                value.getTbl().getSelectionModel().clearSelection();
            } else {
                value.getTbl().getSelectionModel().clearSelection();
                break;
            }
        }
    }

    public void deSelectExclude() {
        List<JButton> btns = new ArrayList<>(Arrays.asList(arrBtn));
        Collections.reverse(btns);
        KeyForViewUI selectedValue = mapBtnForKeyViewUI.get(selectedBtn);
        int selectedIndex = selectedValue.getIndex();
        for (JButton btn : btns) {
            KeyForViewUI value = mapBtnForKeyViewUI.get(btn);
            if (btn != selectedBtn) {
                if (value.getIndex() != selectedIndex + 1) {
                    btn.setEnabled(false);
                }
                btn.setBackground(Color.WHITE);
                value.getTbl().getSelectionModel().clearSelection();
            } else {
                break;
            }
        }
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
