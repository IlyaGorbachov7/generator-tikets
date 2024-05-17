package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.JListDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.MyListButtons;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.JTableDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper.factory.MapperViewFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.textfield.HintTextField;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.InputSearchFieldsData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

// TODO: necessary added new column for each table of database for the purpose of short description naming any name
// TODO: added sorting functional for "name" field
public class DataBasePanel extends BasePanel {
    private JPanel rootPanel;

    private JButton btnAllDeselect;
    private JButton btnDeselect;
    private JButton btnDelete;
    private JButton btnUpdate;
    private JButton btnCreate;

    private JPanel pnlList;
    private JList<String> listTables;

    private JPanel rootPnlTbls;
    private HintTextField tfFilter;
    private JTable tableData;
    private JLabel lbCurrentPage;
    private JLabel lbTotalNumberPage;
    private JComboBox<Integer> cmbCountView;
    private JButton btnNext;
    private JButton btnBack;

    private JListDataBase jListTables;
    private MyListButtons myListButtons;

    private JTableDataBase tblUniversity;
    private JTableDataBase tblFaculty;
    private JTableDataBase tblDepartment;
    private JTableDataBase tblSpecialization;
    private JTableDataBase tblDiscipline;
    private JTableDataBase tblHeadDepartment;
    private JTableDataBase tblTeacher;

    private final InputSearchFieldsData inputSearchFieldsData = InputSearchFieldsData.builder().build();

    public DataBasePanel(Window frame) {
        super(frame);
        initPanel();
    }

    @Override
    public void initPanel() {
        // Methods particular for this panel
        initUIFormComponents();
        initCustomComponents();
        addingCustomComponents();

        // default methods
        setConfigComponents();
        setComponentsListeners();
    }

    protected void initUIFormComponents() {
        this.setLayout(new BorderLayout());
        this.add(rootPanel, BorderLayout.CENTER);
    }

    protected void initCustomComponents() {
        Function<Object, List<?>> supplierDataList = new Function<Object, List<?>>() {
            @Override
            public List<?> apply(Object o) {
                Class<?> clazzModelView = (Class<?>) o;
                if (clazzModelView == UniversityModelTbl.class) {
//                 Еще нужно указать объект который будет мапить Object класс в нужный объект.
                    return MapperViewFactoryImpl.getInstance().universityMapper()
                            .listUniversityDtoToModelTbl(ServiceFactoryImpl.getInstance().universityService().getAll());
                } else if (clazzModelView == FacultyModelTbl.class) {
//                    return MapperViewFactoryImpl.getInstance().facultyMapper()
//                            .listFacultyDtoDtoModelTbl(
//                                    MapperViewFactoryImpl.getInstance().facultyMapper()
//                                            .listFacultyDtoDtoModelTbl(ServiceFactoryImpl.getInstance().facultyService().getByUniversityId(inputSearchFieldsData.getUniversity().getId())));
                }
                return null;
            }
        };

        myListButtons = MyListButtons.builder()
                .modelTableViewSuppliers(Arrays.asList(
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(UniversityModelTbl.class).supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(FacultyModelTbl.class).supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(DepartmentModelTbl.class).supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(SpecializationModelTbl.class).supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(DisciplineModelTbl.class).supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(HeadDepartmentModelTbl.class).supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(TeacherModelTbl.class).supplierData(supplierDataList).build())
                        .toArray(ModelTableViewSupplier[]::new))
                .rootPnl(rootPnlTbls).build();

        tblUniversity = Objects.requireNonNull(myListButtons.getMapBtnForKeyViewUI().get(myListButtons.getArrBtn()[0]).getTbl());
        tblFaculty = Objects.requireNonNull(myListButtons.getMapBtnForKeyViewUI().get(myListButtons.getArrBtn()[1]).getTbl());
        tblDepartment = Objects.requireNonNull(myListButtons.getMapBtnForKeyViewUI().get(myListButtons.getArrBtn()[2]).getTbl());
        tblSpecialization = Objects.requireNonNull(myListButtons.getMapBtnForKeyViewUI().get(myListButtons.getArrBtn()[3]).getTbl());
        tblDiscipline = Objects.requireNonNull(myListButtons.getMapBtnForKeyViewUI().get(myListButtons.getArrBtn()[4]).getTbl());
        tblHeadDepartment = Objects.requireNonNull(myListButtons.getMapBtnForKeyViewUI().get(myListButtons.getArrBtn()[5]).getTbl());
        tblTeacher = Objects.requireNonNull(myListButtons.getMapBtnForKeyViewUI().get(myListButtons.getArrBtn()[6]).getTbl());

    }

    protected void addingCustomComponents() {
        pnlList.add(myListButtons);
    }


    @Override
    public void setConfigComponents() {
    }

    @Override
    public void setComponentsListeners() {
        ActionHandler handler = new ActionHandler();
        TableSelectedRowsListener handlerSelection = new HandlerSelectionRows();

        btnDeselect.addActionListener(handler);
        btnCreate.addActionListener(handler);
        btnUpdate.addActionListener(handler);
        btnDelete.addActionListener(handler);
        btnNext.addActionListener(handler);
        btnBack.addActionListener(handler);

        myListButtons.getMapBtnForKeyViewUI()
                .forEach((btn, keyView) -> {
                    keyView.getTbl().addTableSelectedRowsListener(handlerSelection);
                });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private final class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source == btnDeselect) {
                //  обязательно 2 раза нужно вызвать. Я не заню почему не работает если вызвать только один раз
                myListButtons.deSelect();
                myListButtons.deSelect();
            }
        }
    }

    private final class HandlerSelectionRows implements TableSelectedRowsListener {

        @Override
        public void perform(TableSelectedRowsEvent event) {
            Object[] elemSelected = event.getSelectedItems();
            if (elemSelected.length == 1) {
                if (event.getClassTableView() == UniversityModelTbl.class) {
                    inputSearchFieldsData.setUniversity((UniversityModelTbl) elemSelected[0]);
                } else if (event.getClassTableView() == FacultyModelTbl.class) {
                    inputSearchFieldsData.setFaculty((FacultyModelTbl) elemSelected[0]);
                } else if (event.getClassTableView() == DepartmentModelTbl.class) {
                    inputSearchFieldsData.setDepartment((DepartmentModelTbl) elemSelected[0]);
                } else if (event.getClassTableView() == SpecializationModelTbl.class) {
                    inputSearchFieldsData.setSpecialization((SpecializationModelTbl) elemSelected[0]);
                } else if (event.getClassTableView() == HeadDepartmentModelTbl.class) {
                    inputSearchFieldsData.setHeadDepartment((HeadDepartmentModelTbl) elemSelected[0]);
                } else if (event.getClassTableView() == TeacherModelTbl.class) {
                    inputSearchFieldsData.setTeacher((TeacherModelTbl) elemSelected[0]);
                } else if (event.getClassTableView() == DisciplineModelTbl.class) {
                    inputSearchFieldsData.setDiscipline((DisciplineModelTbl) elemSelected[0]);
                }
            } else System.out.println("Selected > 1 element rows");
        }
    }
}