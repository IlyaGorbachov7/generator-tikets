package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.MyJCompoBox;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.JTableDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.textfield.HintTextField;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.InputSearchFieldsData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

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

    JTableDataBase tblUniversity;
    JTableDataBase tblFaculty;
    JTableDataBase tblDepartment;

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

        listTables.setFixedCellHeight(36);
        this.addComponentListener(new ComponentAdapter() {
            // this handler needed for correct rendering UI components related with list and his stretching by height
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle rect = pnlList.getVisibleRect();
                int quantityElems = listTables.getModel().getSize();
                int heightElem = rect.height / quantityElems;
                listTables.setFixedCellHeight(heightElem);
            }
        });
    }

    protected void initCustomComponents() {
        tfFilter = new HintTextField("Searching");
        tblUniversity = JTableDataBase.builder().build();
        tblFaculty = JTableDataBase.builder().build();
        tblDepartment = JTableDataBase.builder().build();

    }

    protected void addingCustomComponents() {
        pnlTbls.add(tfFilter, BorderLayout.NORTH);
        pnlTbls.add(tblUniversity, BorderLayout.CENTER);
    }


    @Override
    public void setConfigComponents() {

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