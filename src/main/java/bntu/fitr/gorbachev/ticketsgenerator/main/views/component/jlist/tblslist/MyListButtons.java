package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist;

import bntu.fitr.gorbachev.ticketsgenerator.main.TicketGeneratorUtil;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.LocalizerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.AppThemeManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ColorManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeApp;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeChangerListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.handers.ChoiceButtonListListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.handers.EventChoiceBtn;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.reflectionapi.ReflectionListDataBaseHelper;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.JTableDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.KeyForViewUI;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.ModelTableViewSupplier;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.RelatedTblDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.PaginationView;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Log4j2
public class MyListButtons extends JPanel implements ThemeChangerListener {

    private final JPanel rootPnlForTable;
    private final Map<JButton, KeyForViewUI> mapBtnForKeyViewUI;
    private final JButton[] arrBtn;
    private final List<ChoiceButtonListListener> handlersOnChoice = new ArrayList<>(4);
    private final JPanel EMPTY = new JPanel();

    private final HandlerButtonActionListener handlerBtnAction;
    private final TableSelectedRowsListener handlerSelection;

    @Builder
    public MyListButtons(ModelTableViewSupplier[] modelTableViewSuppliers,
                         JPanel rootPnl) {
        this.setLayout(new GridBagLayout());

        var classesTableView = Arrays.stream(modelTableViewSuppliers).map(ModelTableViewSupplier::getClazzModelView).toArray(Class[]::new);
        ReflectionListDataBaseHelper.checkClassesOnTheModelViewTable(classesTableView);
        // Farther means that all right
        this.rootPnlForTable = rootPnl;
        this.arrBtn = new JButton[modelTableViewSuppliers.length];
        handlerBtnAction = this.new HandlerButtonActionListener();
        handlerSelection = new HandlerSelectionRowsListener();

        IntIterator iIter = IntIterator.builder().build();
        mapBtnForKeyViewUI = Arrays.stream(modelTableViewSuppliers)
                .map(modelTableViewSupplier -> {
                    JPanel p = new JPanel();
                    String name = Localizer.get(ReflectionListDataBaseHelper.extractTableViewName(modelTableViewSupplier.getClazzModelView()));
                    JButton btn = new JButton(name);
                    btn.setName(name);
                    Class<?> clazzTblView = modelTableViewSupplier.getClazzModelView();
                    Function<Object, List<?>> supplierDataList = modelTableViewSupplier.getSupplierData();
                    Function<Object, Object> supplierCreate = modelTableViewSupplier.getSupplierCreate();
                    Function<Object, Object> supplierUpdate = modelTableViewSupplier.getSupplierUpdate();
                    Function<Object, List<?>> supplierDelete = modelTableViewSupplier.getSupplierDelete();
                    RelatedTblDataBase relatedMdlTbl = modelTableViewSupplier.getRelatedMdlTbl();
                    log.debug("init TableDatabase by name: {}", name);
                    return Map.entry(btn, // key
                            KeyForViewUI.builder() // value
                                    .index(iIter.cur()).pv(PaginationView.builder().build()).btn(btn)
                                    .tbl(JTableDataBase.builder()
                                            .clazz(clazzTblView).p(p)
                                            .btn(btn)
                                            .supplierDataList(supplierDataList)
                                            .supplierCreate(supplierCreate)
                                            .supplierUpdate(supplierUpdate)
                                            .supplierDelete(supplierDelete)
                                            .relatedMdlTbl(relatedMdlTbl).build()).build());
                }).peek(entry -> arrBtn[iIter.cur()] = entry.getKey())
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
        AppThemeManager.addThemeChangerListener(this);
        TicketGeneratorUtil.getLocalsConfiguration().addListener(new LocalizerListener() {
            @Override
            public void onUpdateLocale(Locale selectedLocale) {
                mapBtnForKeyViewUI.entrySet().forEach(entry -> {
                    entry.getKey().setText(Localizer.get(
                            ReflectionListDataBaseHelper.extractTableViewName(
                                    entry.getValue().getTbl().getClassTableView())));
                });
            }
        });
        defaultSelectedBtn();
    }

    public void addChoiceListener(ChoiceButtonListListener listener) {
        handlersOnChoice.add(listener);
    }

    public void removeChoiceListener(ChoiceButtonListListener listener) {
        handlersOnChoice.remove(listener);
    }


    private JButton selectedBtn;

    @Override
    public Component getComponent() {
        return null;
    }

    @Override
    public void updateComponent() {
        CompletableFuture.runAsync(() -> {
            SwingUtilities.invokeLater(TicketGeneratorUtil.handlerExceptionUIAlert(() -> {
                mapBtnForKeyViewUI.forEach((btn, keyForView) -> {
                    AppThemeManager.updateComponentTreeUI(keyForView.getTbl());
                    if (btn == selectedBtn) {
                        btn.setBackground(ColorsListBtn.ACTIVE.getColor());
                        return;
                    }
                    boolean isSelectedItemOnTable = mapBtnForKeyViewUI.get(btn).getTbl().getSelectedRowCount() > 0;
                    if (isSelectedItemOnTable) {
                        btn.setBackground(ColorsListBtn.SELECTED.getColor());
                    } else {
                        btn.setBackground(ColorsListBtn.REGULAR.getColor());
                    }
                });
            }));
        });
    }

    @Setter
    @Getter
    private class HandlerButtonActionListener implements ActionListener {
        JButton cur;
        JButton prev;

        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(TicketGeneratorUtil.handlerExceptionUIAlert(() -> {
                JButton btnSource = (JButton) e.getSource();
                if (btnSource == cur) return;
                if (cur == null) cur = arrBtn[0]; // initialization 1 раз
                KeyForViewUI v = Objects.requireNonNull(mapBtnForKeyViewUI.get(cur));
                KeyForViewUI prevV = mapBtnForKeyViewUI.get(prev);
                KeyForViewUI relatedV = getRelatedKeyForViewIU(v);
                fireChoiceBeforeJTable(EventChoiceBtn.builder().current(v).previous(prevV).relatedFromCurrent(relatedV).build());
                prev = cur;
                cur = btnSource;
                selectedBtn = cur;

                v = Objects.requireNonNull(mapBtnForKeyViewUI.get(cur));
                prevV = mapBtnForKeyViewUI.get(prev);
                relatedV = getRelatedKeyForViewIU(v);
                fireChoiceJTable(EventChoiceBtn.builder().current(v).previous(prevV).relatedFromCurrent(relatedV).build());

                if (prev != null) {
                    if (mapBtnForKeyViewUI.get(prev).getTbl().getSelectedRowCount() > 0) {
                        prev.setBackground(ColorsListBtn.SELECTED.getColor());
                    } else prev.setBackground(ColorsListBtn.REGULAR.getColor());
                }
                setSelectedColorBtn(cur);
                rootPnlForTable.removeAll();
                rootPnlForTable.add(v.getTbl().getPnlTbl());
                rootPnlForTable.repaint();
                rootPnlForTable.validate();
                fireChoiceAfterJTable(EventChoiceBtn.builder().current(v).previous(prevV).relatedFromCurrent(relatedV).build());
            }));
        }

        KeyForViewUI getRelatedKeyForViewIU(KeyForViewUI base) {
            return mapBtnForKeyViewUI.values()
                    .stream().collect(Collectors.collectingAndThen(Collectors.filtering(keyForView -> {
                        var tbl = keyForView.getTbl();
                        var node = tbl.getRelatedMdlTbl();
                        var relatedMbl = base.getTbl().getRelatedMdlTbl();
                        if (node == null || node == relatedMbl) return false;
                        return node.getChild().contains(relatedMbl);
                    }, Collectors.toUnmodifiableList()), downList -> downList.isEmpty()
                            ? base : downList.get(0)));
        }

        void fireChoiceBeforeJTable(EventChoiceBtn event) {
            handlersOnChoice.parallelStream().forEach((h) -> h.beforePerform(event));
        }

        void fireChoiceJTable(EventChoiceBtn event) {
            handlersOnChoice.parallelStream().forEach((h) -> h.perform(event));
        }

        void fireChoiceAfterJTable(EventChoiceBtn event) {
            handlersOnChoice.parallelStream().forEach((h) -> h.afterPerform(event));
        }
    }

    private static class HandlerSelectionRowsListener implements TableSelectedRowsListener {

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
        btn.setBackground(ColorsListBtn.ACTIVE.getColor());
    }


    public void deSelectAll(Consumer<KeyForViewUI> something) {
        deSelectAll((keyForView) -> true, something);
    }

    public void deSelectAll(Function<KeyForViewUI, Boolean> isRunBase, Consumer<KeyForViewUI> something) {
        KeyForViewUI valueSelected = mapBtnForKeyViewUI.get(arrBtn[0]);
        RelatedTblDataBase relatedTblMdl = valueSelected.getTbl().getRelatedMdlTbl();
        if (relatedTblMdl != null) {
            for (RelatedTblDataBase child : relatedTblMdl.getChild()) {
                doDes(child, isRunBase, something);
            }
        }
        if (isRunBase.apply(valueSelected)) {
            valueSelected.getTbl().getSelectionModel().clearSelection();
            valueSelected.getBtn().setBackground(ColorsListBtn.REGULAR.getColor());
            rootPnlForTable.removeAll();
            rootPnlForTable.add(EMPTY);
            handlerBtnAction.setCur(null); // reset current selected table btn
            rootPnlForTable.repaint();
            rootPnlForTable.revalidate();
        }
        something.accept(valueSelected);
    }

    public void deSelectInclude() {
        deSelectInclude((keyForView) -> true, (keyForViewUI) -> {
        });
    }

    public void deSelectInclude(Consumer<KeyForViewUI> something) {
        deSelectInclude((keyForViewUI) -> true, something);
    }

    public void deSelectInclude(Function<KeyForViewUI, Boolean> isRunBase, Consumer<KeyForViewUI> something) {
        KeyForViewUI valueSelected = mapBtnForKeyViewUI.get(selectedBtn);
        RelatedTblDataBase relatedTblMdl = valueSelected.getTbl().getRelatedMdlTbl();
        if (relatedTblMdl != null) {
            for (RelatedTblDataBase child : relatedTblMdl.getChild()) {
                doDes(child, isRunBase, something);
            }
        }
        if (isRunBase.apply(valueSelected)) {
            valueSelected.getTbl().getSelectionModel().clearSelection();
        }
        something.accept(valueSelected);
    }


    public void deSelectExclude() {
        deSelectExclude((keyForView) -> true, (keyForView) -> {
        });
    }

    public void deSelectExclude(Consumer<KeyForViewUI> something) {
        deSelectExclude((keyForViewUI) -> true, something);
    }

    public void deSelectExclude(Function<KeyForViewUI, Boolean> isRunBase, Consumer<KeyForViewUI> something) {
        KeyForViewUI valueSelected = mapBtnForKeyViewUI.get(selectedBtn);
        RelatedTblDataBase relatedTblMdl = valueSelected.getTbl().getRelatedMdlTbl();
        if (relatedTblMdl != null) {
            for (RelatedTblDataBase child : relatedTblMdl.getChild()) {
                doDes(child, isRunBase, something);
                KeyForViewUI rootValue = mapBtnForKeyViewUI.values()
                        .stream().filter(kv -> kv.getTbl().getClassTableView() == child.getClassMdlTbl())
                        .findFirst().orElseThrow();
                if (isRunBase.apply(rootValue)) {
                    arrBtn[rootValue.getIndex()].setEnabled(true); // next button must be enabled
                    arrBtn[rootValue.getIndex()].setBackground(ColorsListBtn.REGULAR.getColor());
                    rootValue.getTbl().getSelectionModel().clearSelection();
                }
                something.accept(rootValue);
            }
        }
    }

    /**
     * This method same #deSelectExecute. However, this method used for setEnable(false) for
     * all related buttons, then selected more two items in table
     */
    public void deEnabledExclude() {
        deEnabledExclude((keyForView) -> true, (keyForView) -> {
        });
    }

    public void deEnabledExclude(Consumer<KeyForViewUI> something) {
        deEnabledExclude((keyForViewUI) -> true, something);
    }

    public void deEnabledExclude(Function<KeyForViewUI, Boolean> isRunBase, Consumer<KeyForViewUI> somethingRun) {
        KeyForViewUI valueSelected = mapBtnForKeyViewUI.get(selectedBtn);
        RelatedTblDataBase relatedTblMdl = valueSelected.getTbl().getRelatedMdlTbl();
        if (relatedTblMdl != null) {
            for (RelatedTblDataBase child : relatedTblMdl.getChild()) {
                doDes(child, isRunBase, somethingRun);
                KeyForViewUI rootValue = mapBtnForKeyViewUI.values()
                        .stream().filter(kv -> kv.getTbl().getClassTableView() == child.getClassMdlTbl())
                        .findFirst().orElseThrow();
                if (isRunBase.apply(rootValue)) {
                    arrBtn[rootValue.getIndex()].setEnabled(false); // set false for next button
                    arrBtn[rootValue.getIndex()].setBackground(ColorsListBtn.REGULAR.getColor());
                    rootValue.getTbl().getSelectionModel().clearSelection();
                }
                somethingRun.accept(rootValue);
            }
        }
    }

    private void doDes(RelatedTblDataBase relatedTblDataBase) {
        doDes(relatedTblDataBase, (keyForView) -> true, (keForView) -> {
        });
    }

    private void doDes(RelatedTblDataBase relatedTblDataBase, Function<KeyForViewUI, Boolean> isRunBase, Consumer<KeyForViewUI> somethingRun) {
        KeyForViewUI rootValue = mapBtnForKeyViewUI.values()
                .stream().filter(kv -> {
                    return kv.getTbl().getClassTableView() == relatedTblDataBase.getClassMdlTbl();
                })
                .findFirst().orElseThrow();
        var root = rootValue.getTbl().getRelatedMdlTbl();

        if (root == null || root.getChild().isEmpty()) {
            if (isRunBase.apply(rootValue)) {
                arrBtn[rootValue.getIndex()].setEnabled(false);
                arrBtn[rootValue.getIndex()].setBackground(ColorsListBtn.REGULAR.getColor());
                rootValue.getTbl().getSelectionModel().clearSelection();
            }
            somethingRun.accept(rootValue);
            return;
        }

        for (RelatedTblDataBase child : root.getChild()) {
            doDes(child, isRunBase, somethingRun);
            if (isRunBase.apply(rootValue)) {
                arrBtn[rootValue.getIndex()].setEnabled(false);
                arrBtn[rootValue.getIndex()].setBackground(ColorsListBtn.REGULAR.getColor());
                rootValue.getTbl().getSelectionModel().clearSelection();
            }
            somethingRun.accept(rootValue);
        }
    }


    @Builder
    private static final class IntIterator {
        @Builder.Default
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

    /**
     * This implementation follows one of the principles SOLID.
     * <p>
     * <b>This decision is  flexible and dynamically for runtime update theme application </b>
     *
     * @see ColorManager
     */
    public enum ColorsListBtn {
        REGULAR(() -> {
            return UIManager.getColor("Button.background");
        }),
        SELECTED(() -> {
            return UIManager.getColor("Button.selectedBackground");
        }),
        ACTIVE(() -> {
            return UIManager.getColor("Component.focusColor");
        }),
        NAVIGATION(() -> (AppThemeManager.getCurrentTheme() == ThemeApp.NIGHT) ?
                new Color(0, 0, 0) :
                new Color(72, 72, 72));

        ColorManager color;

        ColorsListBtn(@NonNull ColorManager color) {
            this.color = color;
        }

        public Color getColor() {
            return color.getColor();
        }
    }
}
