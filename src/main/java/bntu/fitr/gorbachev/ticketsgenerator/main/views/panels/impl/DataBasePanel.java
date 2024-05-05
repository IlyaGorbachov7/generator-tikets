package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.JListDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.JTableDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.KeyForViewUI;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.textfield.HintTextField;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.InputSearchFieldsData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Objects;

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

        jListTables = JListDataBase.builder().classesTableView(
                Arrays.asList(UniversityModelTbl.class, FacultyModelTbl.class, DepartmentModelTbl.class,
                                SpecializationModelTbl.class, DisciplineModelTbl.class, HeadDepartmentModelTbl.class,
                                TeacherModelTbl.class)
                        .toArray(Class<?>[]::new)).rootPnl(rootPnlTbls).build();

        tblUniversity = Objects.requireNonNull(jListTables.getJTblsDataTable().get(KeyForViewUI.builder().clazzModelTbl(UniversityModelTbl.class).build()));
        tblFaculty = Objects.requireNonNull(jListTables.getJTblsDataTable().get(KeyForViewUI.builder().clazzModelTbl(FacultyModelTbl.class).build()));
        tblDepartment = Objects.requireNonNull(jListTables.getJTblsDataTable().get(KeyForViewUI.builder().clazzModelTbl(DepartmentModelTbl.class).build()));
        tblSpecialization = Objects.requireNonNull(jListTables.getJTblsDataTable().get(KeyForViewUI.builder().clazzModelTbl(SpecializationModelTbl.class).build()));
        tblDiscipline = Objects.requireNonNull(jListTables.getJTblsDataTable().get(KeyForViewUI.builder().clazzModelTbl(DisciplineModelTbl.class).build()));
        tblHeadDepartment =Objects.requireNonNull(jListTables.getJTblsDataTable().get(KeyForViewUI.builder().clazzModelTbl(HeadDepartmentModelTbl.class).build()));
        tblTeacher = Objects.requireNonNull(jListTables.getJTblsDataTable().get(KeyForViewUI.builder().clazzModelTbl(TeacherModelTbl.class).build()));

    }

    protected void addingCustomComponents() {

        pnlList.add(jListTables);
    }


    @Override
    public void setConfigComponents() {
        jListTables.setFixedCellHeight(36);
        this.addComponentListener(new ComponentAdapter() {
            // this handler needed for correct rendering UI components related with list and his stretching by height
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle rect = pnlList.getVisibleRect();
                int quantityElems = jListTables.getModel().getSize();
                int heightElem = rect.height / quantityElems;
                jListTables.setFixedCellHeight(heightElem);
            }
        });

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