package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.MyJCompoBox;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.JListDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.JTableDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.textfield.HintTextField;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.InputSearchFieldsData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.stream.Stream;

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

    private JPanel pnlTbls;
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
        tfFilter = new HintTextField("Searching");

        jListTables = JListDataBase.builder().listData(
                Arrays.asList("Университеты", "Факультеты", "Кафедры", "Специальности",
                        "Заведующий кафедрой", "Преподаватели", "Илья Горбачёв 03052024").toArray(String[]::new)).build();

        tblUniversity = JTableDataBase.builder().build();
        tblFaculty = JTableDataBase.builder().build();
        tblDepartment = JTableDataBase.builder().build();
        tblSpecialization = JTableDataBase.builder().build();
        tblDiscipline = JTableDataBase.builder().build();
        tblHeadDepartment = JTableDataBase.builder().build();
        tblTeacher = JTableDataBase.builder().build();

    }

    protected void addingCustomComponents() {
        pnlTbls.add(tblTeacher, BorderLayout.CENTER);
        pnlTbls.add(tblHeadDepartment, BorderLayout.CENTER);
        pnlTbls.add(tblDiscipline, BorderLayout.CENTER);
        pnlTbls.add(tblSpecialization, BorderLayout.CENTER);
        pnlTbls.add(tblDepartment, BorderLayout.CENTER);
        pnlTbls.add(tfFilter, BorderLayout.CENTER);
        pnlTbls.add(tblUniversity, BorderLayout.CENTER);

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

    private final class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}