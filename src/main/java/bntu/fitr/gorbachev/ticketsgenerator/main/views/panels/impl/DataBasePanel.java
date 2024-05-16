package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.impl.UniversityServiceImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.JListDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.MyListButtons;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.JTableDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.KeyForViewUI;
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
//                if(clazzModelView == UniversityModelTbl.class){
                // Еще нужно указать объект который будет мапить Object класс в нужный объект.
                return MapperViewFactoryImpl.getInstance().universityMapper()
                        .listUniversityDtoToModelTbl(ServiceFactoryImpl.getInstance().universityService().getAll());
//                }
//                return null;
            }
        };

        myListButtons = MyListButtons.builder()
                .modelTableViewSuppliers(Arrays.asList(
                        ModelTableViewSupplier.builder().clazzModelView(UniversityModelTbl.class).supplierData(supplierDataList).build(),
                        ModelTableViewSupplier.builder().clazzModelView(FacultyModelTbl.class).supplierData(supplierDataList).build(),
                        ModelTableViewSupplier.builder().clazzModelView(DepartmentModelTbl.class).supplierData(supplierDataList).build(),
                        ModelTableViewSupplier.builder().clazzModelView(SpecializationModelTbl.class).supplierData(supplierDataList).build(),
                        ModelTableViewSupplier.builder().clazzModelView(DisciplineModelTbl.class).supplierData(supplierDataList).build(),
                        ModelTableViewSupplier.builder().clazzModelView(HeadDepartmentModelTbl.class).supplierData(supplierDataList).build(),
                        ModelTableViewSupplier.builder().clazzModelView(TeacherModelTbl.class).supplierData(supplierDataList).build()
                ).toArray(ModelTableViewSupplier[]::new))
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

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private final class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}