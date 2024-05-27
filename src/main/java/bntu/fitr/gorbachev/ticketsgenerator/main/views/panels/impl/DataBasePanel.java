package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.MyListButtons;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.JTableDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.ModelTableViewSupplier;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.RelatedTblDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.TransmissionObject;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper.factory.MapperViewFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.textfield.HintTextField;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.InputFieldsDataTbl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

// TODO: necessary added new column for each table of database for the purpose of short description naming any name
// TODO: added sorting functional for "name" field

// TODO: ВСЕ запросы в базу данных дложны осуществляться В ОТДЕЛЬНОМ потоке, чтобы графика отдельно НЕ ЗАВИСАЛА
// ответа из бд, а продолжала функционировать
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

    private MyListButtons myListButtons;

    private JTableDataBase tblUniversity;
    private JTableDataBase tblFaculty;
    private JTableDataBase tblDepartment;
    private JTableDataBase tblSpecialization;
    private JTableDataBase tblDiscipline;
    private JTableDataBase tblHeadDepartment;
    private JTableDataBase tblTeacher;

    private final InputFieldsDataTbl inputSearchFieldsData = InputFieldsDataTbl.builder().build();

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
        Function<Object, List<?>> supplierDataList = o -> {
            Class<?> clazzModelView = (Class<?>) o;
            if (clazzModelView == UniversityModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().universityMapper()
                        .listUniversityDtoToModelTbl(ServiceFactoryImpl.getInstance().universityService().getAll());
            } else if (clazzModelView == FacultyModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().facultyMapper()
                        .listFacultyDtoDtoModelTbl(ServiceFactoryImpl.getInstance()
                                .facultyService().getSmplByUniversityId(inputSearchFieldsData.getUniversity().getId()));
            } else if (clazzModelView == DepartmentModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().departmentMapper()
                        .listDepartmentDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .departmentService().getSmplByFacultyId(inputSearchFieldsData.getFaculty().getId()));
            } else if (clazzModelView == SpecializationModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().specializationMapper()
                        .listSpecializationDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .specializationService().getSmplByDepartmentId(inputSearchFieldsData.getDepartment().getId()));
            } else if (clazzModelView == DisciplineModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().disciplineMapper()
                        .listDisciplineDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .disciplineService().getSmplBySpecializationId(inputSearchFieldsData.getSpecialization().getId()));
            } else if (clazzModelView == HeadDepartmentModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().headDepartmentMapper()
                        .listHeadDepartmentDtoModelTbl(ServiceFactoryImpl.getInstance()
                                .headDepartmentService().getSmplByDepartmentId(inputSearchFieldsData.getDepartment().getId()));
            } else if (clazzModelView == TeacherModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().teacherMapper()
                        .listTeacherDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .teacherService().getSmplByFacultyId(inputSearchFieldsData.getFaculty().getId()));
            }
            return null;
        };

        Function<Object, Object> supplierCreate = o -> {
            TransmissionObject transmissionObject = (TransmissionObject) o;
            Class<?> clazzModelView = transmissionObject.getClazzMdlTbl();
            String value = (String) transmissionObject.getDataValue()[0];
            if (clazzModelView == UniversityModelTbl.class) {

                return MapperViewFactoryImpl.getInstance().universityMapper()
                        .universityDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .universityService().create(UniversityCreateDto.builder().name(value)
                                        .build()));
            } else if (clazzModelView == FacultyModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().facultyMapper()
                        .facultyDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .facultyService().createSmpl(FacultyCreateDto.builder().name(value)
                                        .universityId(inputSearchFieldsData.getUniversity().getId()).build()));
            } else if (clazzModelView == DepartmentModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().departmentMapper()
                        .departmentDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .departmentService().createSmpl(DepartmentCreateDto.builder().name(value)
                                        .facultyId(inputSearchFieldsData.getFaculty().getId()).build()));
            } else if (clazzModelView == SpecializationModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().specializationMapper()
                        .specializationDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .specializationService().createSmpl(SpecializationCreateDto.builder().name(value)
                                        .departmentId(inputSearchFieldsData.getDepartment().getId()).build()));
            } else if (clazzModelView == DisciplineModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().disciplineMapper()
                        .disciplineDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .disciplineService().createSmpl(DisciplineCreateDto.builder().name(value)
                                        .specializationId(inputSearchFieldsData.getSpecialization().getId()).build()));
            } else if (clazzModelView == HeadDepartmentModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().headDepartmentMapper()
                        .headDepartmentDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .headDepartmentService().createSmpl(HeadDepartmentCreateDto.builder().name(value)
                                        .departmentId(inputSearchFieldsData.getDepartment().getId()).build()));
            } else if (clazzModelView == TeacherModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().teacherMapper()
                        .teacherDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .teacherService().createSmpl(TeacherCreateDto.builder().name(value)
                                        .facultyId(inputSearchFieldsData.getFaculty().getId()).build()));
            }
            return null;
        };

/*
        Function<Object, List<?>> supplierUpdate = o -> {
            Class<?> clazzModelView = (Class<?>) o;
            if (clazzModelView == UniversityModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().universityMapper()
                        .listUniversityDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .universityService().update());
            } else if (clazzModelView == FacultyModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().facultyMapper()
                        .listFacultyDtoDtoModelTbl(ServiceFactoryImpl.getInstance()
                                .facultyService().update());
            } else if (clazzModelView == DepartmentModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().departmentMapper()
                        .listDepartmentDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .departmentService().update());
            } else if (clazzModelView == SpecializationModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().specializationMapper()
                        .listSpecializationDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .specializationService().update());
            } else if (clazzModelView == DisciplineModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().disciplineMapper()
                        .listDisciplineDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .disciplineService().update());
            } else if (clazzModelView == HeadDepartmentModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().headDepartmentMapper()
                        .listHeadDepartmentDtoModelTbl(ServiceFactoryImpl.getInstance()
                                .headDepartmentService().update());
            } else if (clazzModelView == TeacherModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().teacherMapper()
                        .listTeacherDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .teacherService().update());
            }
            return null;
        };

*/
        Function<Object, List<?>> supplierDelete = o -> {
            TransmissionObject transmissionObject = (TransmissionObject) o;
            Class<?> clazzModelView = transmissionObject.getClazzMdlTbl();

            if (clazzModelView == UniversityModelTbl.class) {
                ServiceFactoryImpl.getInstance()
                        .universityService().delete(
                                MapperViewFactoryImpl.getInstance().universityMapper()
                                        .listUniversityMdlTblToDto(
                                                Arrays.stream(transmissionObject.getDataValue())
                                                        .map(obj -> (UniversityModelTbl) obj).toList()));
            } else if (clazzModelView == FacultyModelTbl.class) {
                ServiceFactoryImpl.getInstance().facultyService().deleteSmpl(
                        MapperViewFactoryImpl.getInstance().facultyMapper()
                                .listFacultyMdlTblToSmpl(
                                        Arrays.stream(transmissionObject.getDataValue())
                                                .map(obj -> (FacultyModelTbl) obj).toList()));
            } else if (clazzModelView == DepartmentModelTbl.class) {
                ServiceFactoryImpl.getInstance()
                        .departmentService().deleteSmpl(
                                MapperViewFactoryImpl.getInstance().departmentMapper()
                                        .listDepartmentMdlTblToSmpl(
                                                Arrays.stream(transmissionObject.getDataValue())
                                                        .map(obj -> (DepartmentModelTbl) obj).toList()));
            } else if (clazzModelView == SpecializationModelTbl.class) {
                ServiceFactoryImpl.getInstance()
                        .specializationService().deleteSmpl(
                                MapperViewFactoryImpl.getInstance().specializationMapper()
                                        .listSpecializationMdlTblToSmpl(
                                                Arrays.stream(transmissionObject.getDataValue())
                                                        .map(obj -> (SpecializationModelTbl) obj).toList()));
            } else if (clazzModelView == DisciplineModelTbl.class) {
                ServiceFactoryImpl.getInstance()
                        .disciplineService().deleteSmpl(
                                MapperViewFactoryImpl.getInstance().disciplineMapper()
                                        .listDisciplineMdlTblToSmpl(
                                                Arrays.stream(transmissionObject.getDataValue())
                                                        .map(obj -> (DisciplineModelTbl) obj).toList()));
            } else if (clazzModelView == HeadDepartmentModelTbl.class) {
                ServiceFactoryImpl.getInstance()
                        .headDepartmentService().deleteSmpl(
                                MapperViewFactoryImpl.getInstance().headDepartmentMapper()
                                        .listHeadDepartmentMdlTblToSmpl(
                                                Arrays.stream(transmissionObject.getDataValue())
                                                        .map(obj -> (HeadDepartmentModelTbl) obj).toList()));
            } else if (clazzModelView == TeacherModelTbl.class) {
                ServiceFactoryImpl.getInstance()
                        .teacherService().deleteSmpl(
                                MapperViewFactoryImpl.getInstance().teacherMapper()
                                        .listTeacherMdlTblToSmpl(
                                                Arrays.stream(transmissionObject.getDataValue())
                                                        .map(obj -> (TeacherModelTbl) obj).toList()));
            }
            return null;
        };

        myListButtons = MyListButtons.builder()
                .modelTableViewSuppliers(Arrays.asList(
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(UniversityModelTbl.class)
                                        .supplierData(supplierDataList)
                                        .supplierCreate(supplierCreate)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(UniversityModelTbl.class)
                                                .child(Arrays.asList(
                                                        RelatedTblDataBase.builder().classMdlTbl(FacultyModelTbl.class).build()
                                                )).build())
//                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(FacultyModelTbl.class)
                                        .supplierData(supplierDataList)
                                        .supplierCreate(supplierCreate)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(FacultyModelTbl.class)
                                                .child(Arrays.asList(
                                                        RelatedTblDataBase.builder().classMdlTbl(DepartmentModelTbl.class).build(),
                                                        RelatedTblDataBase.builder().classMdlTbl(TeacherModelTbl.class).build()
                                                )).build())
//                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(DepartmentModelTbl.class)
                                        .supplierCreate(supplierCreate)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(DepartmentModelTbl.class)
                                                .child(Arrays.asList(
                                                        RelatedTblDataBase.builder().classMdlTbl(SpecializationModelTbl.class).build(),
                                                        RelatedTblDataBase.builder().classMdlTbl(HeadDepartmentModelTbl.class).build()
                                                )).build())
//                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(SpecializationModelTbl.class)
                                        .supplierCreate(supplierCreate)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(SpecializationModelTbl.class)
                                                .child(Arrays.asList(
                                                        RelatedTblDataBase.builder().classMdlTbl(DisciplineModelTbl.class).build()
                                                )).build())
//                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(DisciplineModelTbl.class)
                                        .supplierCreate(supplierCreate)
//                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(HeadDepartmentModelTbl.class)
                                        .supplierCreate(supplierCreate)
//                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(TeacherModelTbl.class)
                                        .supplierCreate(supplierCreate)
//                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierDataList).build())
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
                myListButtons.deSelectInclude();
                myListButtons.deSelectInclude();

            }
            if (source == btnCreate) {
                JTableDataBase tbl = myListButtons.getMapBtnForKeyViewUI()
                        .get(myListButtons.getSelectedBtn())
                        .getTbl();
                String value = JOptionPane.showInternalInputDialog(DataBasePanel.this, "Введите название: ", "Input Dialog", JOptionPane.INFORMATION_MESSAGE);
                if (value != null && !value.isBlank()) {
                    tbl.createItem(value);
                    tbl.performSetData();
                }
            } else if (source == btnDelete) {
                if (JOptionPane.showInternalConfirmDialog(DataBasePanel.this, "Вы уверены ?", "Delete Dialog", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                    JTableDataBase tbl = myListButtons.getMapBtnForKeyViewUI()
                            .get(myListButtons.getSelectedBtn())
                            .getTbl();
                    tbl.deleteItem();
                    tbl.performSetData();
                    myListButtons.deSelectInclude();
                    myListButtons.deSelectInclude();
                }
            }
        }
    }

    private void setEnableCRUDbtn(boolean enableCreate, boolean enableDelete, boolean enableUpdate) {
        btnCreate.setEnabled(enableCreate);
        btnDelete.setEnabled(enableDelete);
        btnUpdate.setEnabled(enableUpdate);
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

            myListButtons.deSelectExclude(); // чтобы изменить выбор, если выбор уже был сделан
        }
    }
}