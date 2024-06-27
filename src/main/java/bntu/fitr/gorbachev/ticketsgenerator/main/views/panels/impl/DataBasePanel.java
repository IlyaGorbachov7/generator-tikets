package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.other.PaginationParam;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.CombaBoxSupplierView;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.EventChoiceBtn;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.MyListButtons;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper.factory.MapperViewFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.textfield.HintTextField;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.InputFieldsDataTbl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.PaginationView;
import lombok.Builder;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

    private final InputFieldsDataTbl inputSearchFieldsData = InputFieldsDataTbl.builder().build();

    private Function<Object, List<?>> supplierDataList;

    private Function<Object, String> mapperView;
    private Function<Object, String> mapperFind;
    private BiFunction<Object, InputUpdateDataProxy, Object> mapperUpdateItem;
    private Supplier<PaginationParam> supplierPaginationParam;

    private KeyForViewUI selectedKeyForViewUI;
    private KeyForViewUI subSelectedKeyForViewUI;
    private PaginationView paginationView;

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

        supplierDataList = o -> {
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

        mapperView = o -> {
            String map = null;
            if (o == null) return null;
            if (o instanceof UniversityModelTbl univ) {
                map = univ.getName();
            } else if (o instanceof FacultyModelTbl fac) {
                map = fac.getName();
            } else if (o instanceof DepartmentModelTbl dep) {
                map = dep.getName();
            } else if (o instanceof SpecializationModelTbl sp) {
                map = sp.getName();
            } else if (o instanceof DisciplineModelTbl dis) {
                map = dis.getName();
            } else if (o instanceof HeadDepartmentModelTbl heddep) {
                map = heddep.getName();
            } else if (o instanceof TeacherModelTbl teach) {
                map = teach.getName();
            }
            return map;
        };

        mapperFind = o -> {
            String map = null;
            if (o == null) return null;
            if (o instanceof UniversityModelTbl univ) {
                map = univ.getId().toString();
            } else if (o instanceof FacultyModelTbl fac) {
                map = fac.getId().toString();
            } else if (o instanceof DepartmentModelTbl dep) {
                map = dep.getId().toString();
            } else if (o instanceof SpecializationModelTbl sp) {
                map = sp.getId().toString();
            } else if (o instanceof DisciplineModelTbl dis) {
                map = dis.getId().toString();
            } else if (o instanceof HeadDepartmentModelTbl heddep) {
                map = heddep.getId().toString();
            } else if (o instanceof TeacherModelTbl teach) {
                map = teach.getId().toString();
            }
            return map;
        };

        mapperUpdateItem = (item, inputUpdateData) -> {
            if (item == null) return null;
            if (inputUpdateData == null) throw new NullPointerException("inputUpdateData is null");
            if (item instanceof UniversityModelTbl univ) {
                univ.setName(inputUpdateData.getTextFieldData());
            } else if (item instanceof FacultyModelTbl fac) {
                fac.setName(inputUpdateData.getTextFieldData());
                fac.setUniversityId(UUID.fromString(inputUpdateData.getComboboxData()));
            } else if (item instanceof DepartmentModelTbl dep) {
                dep.setName(inputUpdateData.getTextFieldData());
                dep.setFacultyId(UUID.fromString(inputUpdateData.getComboboxData()));
            } else if (item instanceof SpecializationModelTbl sp) {
                sp.setName(inputUpdateData.getTextFieldData());
                sp.setDepartmentId(UUID.fromString(inputUpdateData.getComboboxData()));
            } else if (item instanceof DisciplineModelTbl dis) {
                dis.setName(inputUpdateData.getTextFieldData());
                dis.setSpecializationId(UUID.fromString(inputUpdateData.getComboboxData()));
            } else if (item instanceof HeadDepartmentModelTbl heddep) {
                heddep.setName(inputUpdateData.getTextFieldData());
                heddep.setDepartmentId(UUID.fromString(inputUpdateData.getComboboxData()));
            } else if (item instanceof TeacherModelTbl teach) {
                teach.setName(inputUpdateData.getTextFieldData());
                teach.setFacultyId(UUID.fromString(inputUpdateData.getComboboxData()));
            }
            return item;
        };

        supplierPaginationParam = () -> {
            Class<?> clazzModelView = selectedKeyForViewUI.getTbl().getClassTableView();
            String strUUID = mapperFind.apply(subSelectedKeyForViewUI.getTbl().getSelectedItem());
            UUID subId = strUUID == null ? null : UUID.fromString(strUUID);
            int currentPage = paginationView.getCurrentPage();
            int itemsOnPage = paginationView.getItemsOnPage();
            String filterText = paginationView.getFilterText();

            if (clazzModelView == UniversityModelTbl.class) {
                return ServiceFactoryImpl.getInstance().universityService().calculatePageParam(itemsOnPage, currentPage, filterText);
            } else if (clazzModelView == FacultyModelTbl.class) {
                return ServiceFactoryImpl.getInstance().facultyService().calculatePageParam(itemsOnPage, currentPage, filterText, subId);
            } else if (clazzModelView == DepartmentModelTbl.class) {
                return ServiceFactoryImpl.getInstance().departmentService().calculatePageParam(itemsOnPage, currentPage, filterText, subId);
            } else if (clazzModelView == SpecializationModelTbl.class) {
                return ServiceFactoryImpl.getInstance().specializationService().calculatePageParam(itemsOnPage, currentPage, filterText, subId);
            } else if (clazzModelView == DisciplineModelTbl.class) {
                return ServiceFactoryImpl.getInstance().disciplineService().calculatePageParam(itemsOnPage, currentPage, filterText, subId);
            } else if (clazzModelView == HeadDepartmentModelTbl.class) {
                return ServiceFactoryImpl.getInstance().headDepartmentService().calculatePageParam(itemsOnPage, currentPage, filterText, subId);
            } else if (clazzModelView == TeacherModelTbl.class) {
                return ServiceFactoryImpl.getInstance().teacherService().calculatePageParam(itemsOnPage, currentPage, filterText, subId);
            }
            throw new RuntimeException("Unified class model: " + clazzModelView);
        };

        Function<Object, List<?>> supplierPaginationDataList = o -> {
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

        Function<Object, Object> supplierUpdate = item -> {
            if (item instanceof UniversityModelTbl univ) {
                return ServiceFactoryImpl.getInstance().universityService()
                        .update(MapperViewFactoryImpl.getInstance().universityMapper().universityMdlTblToDto(univ));

            } else if (item instanceof FacultyModelTbl fac) {
                return ServiceFactoryImpl.getInstance().facultyService()
                        .update(MapperViewFactoryImpl.getInstance().facultyMapper().facultyMdlTblToSmpl(fac));
            } else if (item instanceof DepartmentModelTbl dep) {
                return ServiceFactoryImpl.getInstance().departmentService()
                        .updateSmpl(MapperViewFactoryImpl.getInstance().departmentMapper().departmentMdlTblToSmpl(dep));
            } else if (item instanceof SpecializationModelTbl spec) {
                return ServiceFactoryImpl.getInstance().specializationService()
                        .update(MapperViewFactoryImpl.getInstance().specializationMapper().specializationMdlTblToSmpl(spec));
            } else if (item instanceof DisciplineModelTbl disc) {
                return ServiceFactoryImpl.getInstance().disciplineService()
                        .update(MapperViewFactoryImpl.getInstance().disciplineMapper().disciplineMdlTblToSmpl(disc));
            } else if (item instanceof HeadDepartmentModelTbl headdep) {
                return ServiceFactoryImpl.getInstance().headDepartmentService()
                        .update(MapperViewFactoryImpl.getInstance().headDepartmentMapper().headDepartmentMdlTblToSmpl(headdep));
            } else if (item instanceof TeacherModelTbl teach) {
                return ServiceFactoryImpl.getInstance().teacherService()
                        .update(MapperViewFactoryImpl.getInstance().teacherMapper().teacherMdlTblToSmpl(teach));
            }
            return null;
        };

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
                                        .supplierData(supplierPaginationDataList)
                                        .supplierCreate(supplierCreate)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(UniversityModelTbl.class)
                                                .child(Collections.singletonList(
                                                        RelatedTblDataBase.builder().classMdlTbl(FacultyModelTbl.class).build()
                                                )).build())
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(FacultyModelTbl.class)
                                        .supplierData(supplierPaginationDataList)
                                        .supplierCreate(supplierCreate)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(FacultyModelTbl.class)
                                                .child(Arrays.asList(
                                                        RelatedTblDataBase.builder().classMdlTbl(DepartmentModelTbl.class).build(),
                                                        RelatedTblDataBase.builder().classMdlTbl(TeacherModelTbl.class).build()
                                                )).build())
                                        .supplierUpdate(supplierUpdate)
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
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierPaginationDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(SpecializationModelTbl.class)
                                        .supplierCreate(supplierCreate)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(SpecializationModelTbl.class)
                                                .child(Collections.singletonList(
                                                        RelatedTblDataBase.builder().classMdlTbl(DisciplineModelTbl.class).build()
                                                )).build())
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierPaginationDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(DisciplineModelTbl.class)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(DisciplineModelTbl.class).build())
                                        .supplierCreate(supplierCreate)
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierPaginationDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(HeadDepartmentModelTbl.class)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(HeadDepartmentModelTbl.class).build())
                                        .supplierCreate(supplierCreate)
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierPaginationDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(TeacherModelTbl.class)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(TeacherModelTbl.class).build())
                                        .supplierCreate(supplierCreate)
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierPaginationDataList).build())
                        .toArray(ModelTableViewSupplier[]::new))
                .rootPnl(rootPnlTbls).build();
    }

    protected void addingCustomComponents() {
        pnlList.add(myListButtons);
    }


    @Override
    public void setConfigComponents() {
        myListButtons.getMapBtnForKeyViewUI().values().parallelStream()
                .map(KeyForViewUI::getPv).forEach(pagination -> {
                    pagination.setItemsOnPage((Integer.parseInt(Objects.requireNonNull(cmbCountView.getSelectedItem()).toString())));
                });
    }

    @Override
    public void setComponentsListeners() {
        ActionHandler handler = new ActionHandler();
        TableSelectedRowsListener handlerSelection = new HandlerSelectionRows();
        ActionListener handlerChoice = new HandlerChoiceButtonList();
        KeyListener fieldEnterHandler = new HandlerEnterField();
        PropertyChangeListener propertyChangeListener = new HandlerPropertyChangePagination();
        btnAllDeselect.addActionListener(handler);
        btnDeselect.addActionListener(handler);
        btnCreate.addActionListener(handler);
        btnUpdate.addActionListener(handler);
        btnDelete.addActionListener(handler);
        btnNext.addActionListener(handler);
        btnBack.addActionListener(handler);
        tfFilter.addKeyListener(fieldEnterHandler);
        cmbCountView.addActionListener((e) -> {
            paginationView.setItemsOnPage(Integer.parseInt(Objects.requireNonNull(cmbCountView.getSelectedItem()).toString()));
            SwingUtilities.invokeLater(this::setPaginationViewChanged);
            SwingUtilities.invokeLater(() -> selectedKeyForViewUI.getTbl().performSetData());
        });

        myListButtons.addChoiceListener(handlerChoice);
        myListButtons.getMapBtnForKeyViewUI()
                .forEach((btn, keyView) -> {
                    keyView.getTbl().addTableSelectedRowsListener(handlerSelection);
                    keyView.getPv().addPropertyChangeListener(propertyChangeListener);
                });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private final class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source == btnAllDeselect) {
                myListButtons.deSelectAll();
            } else if (source == btnDeselect) {
                myListButtons.deSelectInclude();
            } else if (source == btnCreate) {
                JTableDataBase tbl = myListButtons.getMapBtnForKeyViewUI()
                        .get(myListButtons.getSelectedBtn())
                        .getTbl();
                String value = JOptionPane.showInternalInputDialog(DataBasePanel.this, "Введите название: ",
                        "Input Dialog", JOptionPane.INFORMATION_MESSAGE);
                if (value != null && !value.isBlank()) {
                    tbl.createItem(value);
                    tbl.performSetData();
                    setPaginationViewChanged();
                }
            } else if (source == btnDelete) {
                if (JOptionPane.showInternalConfirmDialog(DataBasePanel.this, "Вы уверены ?",
                        "Delete Dialog", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                    CompletableFuture.runAsync(() -> {
                        JTableDataBase tbl = myListButtons.getMapBtnForKeyViewUI()
                                .get(myListButtons.getSelectedBtn())
                                .getTbl();
                        tbl.deleteItem();
                        tbl.performSetData();
                        setPaginationViewChanged();
                        myListButtons.deSelectInclude();
                    });
                }
            } else if (source == btnUpdate) {
                SwingUtilities.invokeLater(() -> {
                    UpdatePanel panel = new UpdatePanel();
                    if (JOptionPane.showConfirmDialog(DataBasePanel.this, panel,
                            "Update Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                        panel.updateRequest();
                        KeyForViewUI keyForView = myListButtons.getMapBtnForKeyViewUI().get(myListButtons.getSelectedBtn());
                        var tbl = keyForView.getTbl();
                        myListButtons.deSelectInclude();
                        tbl.performSetData();
                        setPaginationViewChanged();
                    }
                });
            } else if (source == btnNext) {
                if (paginationView.getCurrentPage() < paginationView.getTotalPage()) {
                    paginationView.setCurrentPage(paginationView.getCurrentPage() + 1);
                }
            } else if (source == btnBack) {
                if (paginationView.getCurrentPage() > 1) {
                    paginationView.setCurrentPage(paginationView.getCurrentPage() - 1);
                }
            }
        }
    }

    private void setEnableCRUDbtn(boolean enableCreate, boolean enableDelete, boolean enableUpdate) {
        btnCreate.setEnabled(enableCreate);
        btnDelete.setEnabled(enableDelete);
        btnUpdate.setEnabled(enableUpdate);
    }

    private void setEnablePaginationElem(boolean enableBack, boolean enableNext) {
        btnBack.setEnabled(enableBack);
        btnNext.setEnabled(enableNext);
    }


    private void setPaginationViewChanged() {
        PaginationParam pp = supplierPaginationParam.get();
        paginationView.setCurrentPage(pp.getCurrentPage());
        paginationView.setTotalPage(pp.getTotalPage());
    }

    private void initPagination() {
        PaginationParam paginationDto = supplierPaginationParam.get();
        paginationView.setTotalPage(paginationDto.getTotalPage());
        paginationView.setCurrentPage(paginationDto.getCurrentPage());
        paginationView.setItemsOnPage(paginationDto.getItemsOnPage()); // repaint to combobox
        paginationView.setFilterText(paginationView.getFilterText()); // repaint to JTextField
        // itemsOnPage don't change
    }

    private final class HandlerSelectionRows implements TableSelectedRowsListener {
        @Override
        public void perform(TableSelectedRowsEvent event) {
            Object[] elemSelected = event.getSelectedItems();
            if (elemSelected.length == 1) {
                setEnableCRUDbtn(true, true, true);
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
                myListButtons.deSelectExclude(); // чтобы изменить выбор, если выбор уже был сделан
            } else {
                System.out.println("Selected > 1 element rows");
                setEnableCRUDbtn(true, false, false);
                myListButtons.deEnabledExclude();
            }
        }
    }

    private final class HandlerChoiceButtonList implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                EventChoiceBtn eventChoiceBtn = (EventChoiceBtn) e.getSource();
                selectedKeyForViewUI = eventChoiceBtn.getCurrent();
                subSelectedKeyForViewUI = eventChoiceBtn.getPrevious();
                paginationView = selectedKeyForViewUI.getPv();

                // don't choose this sequence
                SwingUtilities.invokeLater(DataBasePanel.this::initPagination);

                boolean isSelectedRows = selectedKeyForViewUI.getTbl().getSelectedRowCount() > 0;
                if (isSelectedRows) {
                    setEnableCRUDbtn(true, true, true);
                } else setEnableCRUDbtn(true, false, false);
            });
        }
    }

    private class UpdatePanel extends Panel {
        private final JTextField field;

        private CombaBoxSupplierView box;

        private final Object selectedItem;

        public UpdatePanel() {
            KeyForViewUI selectedTblView = selectedKeyForViewUI;
            KeyForViewUI subSelectedTblView = subSelectedKeyForViewUI;


            JPanel rootPnl = new JPanel(new GridLayout(2, 2, 10, 10));

            JLabel sublbl = null;
            selectedItem = selectedTblView.getTbl().getSelectedItem();
            Object subselectedItem = subSelectedTblView.getTbl().getSelectedItem();
            if (subSelectedTblView != selectedTblView) {
                sublbl = new JLabel(subSelectedTblView.getBtn().getText());
                List<?> dataList = supplierDataList.apply(subSelectedTblView.getTbl().getClassTableView());
                box = new CombaBoxSupplierView(mapperView, dataList);
                box.setSelectedItem(subselectedItem);
            }
            JLabel curlbl = new JLabel(selectedTblView.getBtn().getText());
            field = new JTextField(mapperView.apply(selectedItem));
            if (subSelectedTblView != selectedTblView) {
                rootPnl.add(sublbl);
                rootPnl.add(box);
            }
            rootPnl.add(curlbl);
            rootPnl.add(field);
            rootPnl.setPreferredSize(new Dimension(500, 50));
            this.add(rootPnl);
        }

        public void updateRequest() {
            InputUpdateDataProxy inputupdateData = InputUpdateDataProxy.builder()
                    .comboboxData(mapperFind.apply((box != null) ? Objects.requireNonNull(box).getSelectedItem() : ""))
                    .textFieldData(field.getText()).build();
            KeyForViewUI selectedTblView = myListButtons.getMapBtnForKeyViewUI().get(myListButtons.getSelectedBtn());
            var tbl = selectedTblView.getTbl();
            tbl.updateItem(mapperUpdateItem.apply(selectedItem, inputupdateData));
        }
    }

    @Getter
    @Builder
    private static final class InputUpdateDataProxy {
        @Builder.Default
        private String comboboxData = "";
        @Builder.Default
        private String textFieldData = "";
    }

    private class HandlerPropertyChangePagination implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            switch (evt.getPropertyName()) {
                case PaginationView.CURRENTPAGE -> {
                    lbCurrentPage.setText(String.valueOf(evt.getNewValue()));

                    if (paginationView.getCurrentPage() == paginationView.getTotalPage()) {
                        if (paginationView.getTotalPage() == 1) {
                            setEnablePaginationElem(false, false);
                        } else {
                            setEnablePaginationElem(true, false);
                        }
                    } else {
                        if (paginationView.getCurrentPage() == 1) {
                            setEnablePaginationElem(false, true);
                        } else {
                            setEnablePaginationElem(true, true);
                        }
                    }
                }
                case PaginationView.TOTALPAGE -> {
                    lbTotalNumberPage.setText(String.valueOf(evt.getNewValue()));
                    selectedKeyForViewUI.getTbl().performSetData();
                }
                case PaginationView.ITEMSPAGE -> {
                    cmbCountView.setSelectedItem(String.valueOf(evt.getNewValue()));
                }
                case PaginationView.FILTERTEXT -> {
                    tfFilter.setText((String) evt.getNewValue());
                }
                default -> throw new RuntimeException("Undefined property: " + evt.getPropertyName());
            }
        }
    }

    private class HandlerEnterField implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            System.out.println("keyTyped : ");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("keyPressed");
        }

        @Override
        public void keyReleased(KeyEvent e) {
            System.out.println("keyReleased");
        }
    }
}