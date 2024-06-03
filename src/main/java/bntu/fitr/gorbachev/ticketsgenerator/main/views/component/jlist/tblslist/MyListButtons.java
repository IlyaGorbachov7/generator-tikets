package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.reflectionapi.ReflectionListDataBaseHelper;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.JTableDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.KeyForViewUI;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.RelatedTblDataBase;
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
                    RelatedTblDataBase relatedMdlTbl = modelTableViewSupplier.getRelatedMdlTbl();

                    return Map.entry(btn, // key
                            KeyForViewUI.builder() // value
                                    .index(iIter.cur()).btn(btn)
                                    .tbl(JTableDataBase.builder()
                                            .clazz(clazzTblView).p(p)
                                            .btn(btn)
                                            .supplierDataList(supplierDataList)
                                            .supplierCreate(supplierCreate)
                                            .supplierUpdate(supplierUpdate)
                                            .supplierDelete(supplierDelete)
                                            .relatedMdlTbl(relatedMdlTbl).build()).build());
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
            if (prev != null) {
                if (mapBtnForKeyViewUI.get(prev).getTbl().getSelectedRowCount() > 0) {
                    prev.setBackground(Color.LIGHT_GRAY);
                } else prev.setBackground(Color.WHITE);
            }

            setSelectedColorBtn(cur);
            rootPnlForTable.removeAll();
            rootPnlForTable.add(v.getTbl().getPnlTbl());
            rootPnlForTable.repaint();
            rootPnlForTable.validate();
        }
    }

    private class HandlerSelectionRowsListener implements TableSelectedRowsListener {

        /**
         * This is handler listener was for set enable list of the button.
         * However, now it is function performed from {@link  #deSelectExclude()}.
         * It means that method performs same logic, that in the handler.
         * I don't remove this code, that leave logic in the future with goal using
         */
        @Override
        public void perform(TableSelectedRowsEvent event) {
            /*KeyForViewUI value = mapBtnForKeyViewUI.get(event.getBtn());
            RelatedTblDataBase relatedMdlTbl = value.getTbl().getRelatedMdlTbl();
            if (relatedMdlTbl == null) {
                int index = value.getIndex();
                if (index < mapBtnForKeyViewUI.size() - 1) {
                    arrBtn[index + 1].setEnabled(true);
                }
            } else {
                List<RelatedTblDataBase> childs = Objects.requireNonNull(relatedMdlTbl.getChild());
                for (RelatedTblDataBase child : childs) {
                    KeyForViewUI valueChild = mapBtnForKeyViewUI.values()
                            .stream().filter(kv -> {
                                return kv.getTbl().getClassTableView() == child.getClassMdlTbl();
                            })
                            .findFirst().orElseThrow();
                    arrBtn[valueChild.getIndex()].setEnabled(true);
                }
            }*/
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
        KeyForViewUI valueSelected = mapBtnForKeyViewUI.get(selectedBtn);
        RelatedTblDataBase relatedTblMdl = valueSelected.getTbl().getRelatedMdlTbl();
        if (relatedTblMdl != null) {
            for (RelatedTblDataBase child : relatedTblMdl.getChild()) {
                doDes(child);
            }
        }
        valueSelected.getTbl().getSelectionModel().clearSelection();
    }


    public void deSelectExclude() {
        KeyForViewUI valueSelected = mapBtnForKeyViewUI.get(selectedBtn);
        RelatedTblDataBase relatedTblMdl = valueSelected.getTbl().getRelatedMdlTbl();
        if (relatedTblMdl != null) {
            for (RelatedTblDataBase child : relatedTblMdl.getChild()) {
                doDes(child);
                KeyForViewUI rootValue = mapBtnForKeyViewUI.values()
                        .stream().filter(kv -> kv.getTbl().getClassTableView() == child.getClassMdlTbl())
                        .findFirst().orElseThrow();
                arrBtn[rootValue.getIndex()].setEnabled(true); // next button must be enabled
                arrBtn[rootValue.getIndex()].setBackground(Color.WHITE);
                rootValue.getTbl().getSelectionModel().clearSelection();
            }
        }
    }

    /**
     * This method same #deSelectExecute. However, this method used for setEnable(false) for
     * all related buttons, then selected more two items in table
     */
    public void deEnabledExclude() {
        KeyForViewUI valueSelected = mapBtnForKeyViewUI.get(selectedBtn);
        RelatedTblDataBase relatedTblMdl = valueSelected.getTbl().getRelatedMdlTbl();
        if (relatedTblMdl != null) {
            for (RelatedTblDataBase child : relatedTblMdl.getChild()) {
                doDes(child);
                KeyForViewUI rootValue = mapBtnForKeyViewUI.values()
                        .stream().filter(kv -> kv.getTbl().getClassTableView() == child.getClassMdlTbl())
                        .findFirst().orElseThrow();
                arrBtn[rootValue.getIndex()].setEnabled(false); // set false for next button
                arrBtn[rootValue.getIndex()].setBackground(Color.WHITE);
                rootValue.getTbl().getSelectionModel().clearSelection();
            }
        }
    }

    private void doDes(RelatedTblDataBase relatedTblDataBase) {
        KeyForViewUI rootValue = mapBtnForKeyViewUI.values()
                .stream().filter(kv -> {
                    return kv.getTbl().getClassTableView() == relatedTblDataBase.getClassMdlTbl();
                })
                .findFirst().orElseThrow();
        var root = rootValue.getTbl().getRelatedMdlTbl();

        if (root == null) {
            arrBtn[rootValue.getIndex()].setEnabled(false);
            arrBtn[rootValue.getIndex()].setBackground(Color.WHITE);
            rootValue.getTbl().getSelectionModel().clearSelection();
            return;
        }

        for (RelatedTblDataBase child : root.getChild()) {
            doDes(child);
            arrBtn[rootValue.getIndex()].setEnabled(false);
            arrBtn[rootValue.getIndex()].setBackground(Color.WHITE);
            rootValue.getTbl().getSelectionModel().clearSelection();
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
