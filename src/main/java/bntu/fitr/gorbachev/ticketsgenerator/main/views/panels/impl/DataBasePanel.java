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
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.CombaBoxSupplierView;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.MyListButtons;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.handers.ChoiceButtonListListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.handers.EventChoiceBtn;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper.factory.MapperViewFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.textfield.HintTextField;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.InputFieldsDataTbl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.PaginationView;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO: necessary added new column for each table of database for the purpose of short description naming any name
// TODO: added sorting functional for "name" field

// TODO: ВСЕ запросы в базу данных дложны осуществляться В ОТДЕЛЬНОМ потоке, чтобы графика отдельно НЕ ЗАВИСАЛА
// ответа из бд, а продолжала функционировать
@Log4j2
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
    private JLabel lbCurrentPage;
    private JLabel lbTotalNumberPage;
    private JComboBox<Integer> cmbCountView;
    private JButton btnNext;
    private JButton btnBack;
    private JLabel tablesLbl;

    private MyListButtons myListButtons;

    private final InputFieldsDataTbl inputSearchFieldsData = InputFieldsDataTbl.builder().build();

    private Function<Object, List<?>> supplierDataList;

    private Function<Class, Object> supplierInputSearchFieldData;
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
        updateLocaleComponents();
        initCustomComponents();
        addingCustomComponents();

        // default methods
        setConfigComponents();
        setComponentsListeners();
    }

    private void updateLocaleComponents() {
        btnAllDeselect.setText(Localizer.get("panel.db.all-deselect"));
        btnDeselect.setText(Localizer.get("panel.db.deselect"));
        btnDelete.setText(Localizer.get("panel.db.delete"));
        btnUpdate.setText(Localizer.get("panel.db.update"));
        btnCreate.setText(Localizer.get("panel.db.create"));
        tablesLbl.setText(Localizer.get("panel.db.tables"));
        tfFilter.setToolTipText(Localizer.get("panel.db.tf-filter.tool-tip-text"));
        tfFilter.setText(Localizer.get("panel.db.tf-filter.text"));
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
                                .specializationService().getSmplByDepartmentId(inputSearchFieldsData.getDepartment()
                                        .getId()));
            } else if (clazzModelView == DisciplineModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().disciplineMapper()
                        .listDisciplineDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .disciplineService().getSmplBySpecializationId(inputSearchFieldsData.getSpecialization()
                                        .getId()));
            } else if (clazzModelView == HeadDepartmentModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().headDepartmentMapper()
                        .listHeadDepartmentDtoModelTbl(ServiceFactoryImpl.getInstance()
                                .headDepartmentService().getSmplByDepartmentId(inputSearchFieldsData.getDepartment()
                                        .getId()));
            } else if (clazzModelView == TeacherModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().teacherMapper()
                        .listTeacherDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .teacherService().getSmplByFacultyId(inputSearchFieldsData.getFaculty().getId()));
            }
            return null;
        };

        supplierInputSearchFieldData = clazz -> {
            if (clazz == UniversityModelTbl.class) {
                return inputSearchFieldsData.getUniversity();
            } else if (clazz == FacultyModelTbl.class) {
                return inputSearchFieldsData.getFaculty();
            } else if (clazz == DepartmentModelTbl.class) {
                return inputSearchFieldsData.getDepartment();
            } else if (clazz == SpecializationModelTbl.class) {
                return inputSearchFieldsData.getSpecialization();
            } else if (clazz == DisciplineModelTbl.class) {
                return inputSearchFieldsData.getDiscipline();
            } else if (clazz == HeadDepartmentModelTbl.class) {
                return inputSearchFieldsData.getHeadDepartment();
            } else if (clazz == TeacherModelTbl.class) {
                return inputSearchFieldsData.getTeacher();
            } else {
                throw new RuntimeException("undefield");
            }
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
            int currentPage = paginationView.getCurrentPage();
            int itemsOnPage = paginationView.getItemsOnPage();
            String filterText = paginationView.getFilterText();

            if (clazzModelView == UniversityModelTbl.class) {
                return ServiceFactoryImpl.getInstance().universityService().calculatePageParam(itemsOnPage, currentPage,
                        filterText);
            } else if (clazzModelView == FacultyModelTbl.class) {
                return ServiceFactoryImpl.getInstance().facultyService().calculatePageParam(itemsOnPage, currentPage,
                        filterText,
                        inputSearchFieldsData.getUniversity().getId());
            } else if (clazzModelView == DepartmentModelTbl.class) {
                return ServiceFactoryImpl.getInstance().departmentService().calculatePageParam(itemsOnPage, currentPage,
                        filterText,
                        inputSearchFieldsData.getFaculty().getId());
            } else if (clazzModelView == SpecializationModelTbl.class) {
                return ServiceFactoryImpl.getInstance().specializationService().calculatePageParam(itemsOnPage, currentPage,
                        filterText,
                        inputSearchFieldsData.getDepartment().getId());
            } else if (clazzModelView == DisciplineModelTbl.class) {
                return ServiceFactoryImpl.getInstance().disciplineService().calculatePageParam(itemsOnPage, currentPage,
                        filterText,
                        inputSearchFieldsData.getDepartment().getId());
            } else if (clazzModelView == HeadDepartmentModelTbl.class) {
                return ServiceFactoryImpl.getInstance().headDepartmentService().calculatePageParam(itemsOnPage, currentPage,
                        filterText,
                        inputSearchFieldsData.getDepartment().getId());
            } else if (clazzModelView == TeacherModelTbl.class) {
                return ServiceFactoryImpl.getInstance().teacherService().calculatePageParam(itemsOnPage, currentPage,
                        filterText,
                        inputSearchFieldsData.getFaculty().getId());
            }
            throw new RuntimeException("Unified class model: " + clazzModelView);
        };

        Function<Object, List<?>> supplierPaginationDataList = o -> {
            Class<?> clazzModelView = selectedKeyForViewUI.getTbl().getClassTableView();
            int currentPage = paginationView.getCurrentPage();
            int itemsOnPage = paginationView.getItemsOnPage();
            String filterText = paginationView.getFilterText();

            if (clazzModelView == UniversityModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().universityMapper()
                        .listUniversityDtoToModelTbl(ServiceFactoryImpl.getInstance().universityService().
                                getByLikeName(filterText, currentPage, itemsOnPage));
            } else if (clazzModelView == FacultyModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().facultyMapper()
                        .listFacultyDtoDtoModelTbl(ServiceFactoryImpl.getInstance()
                                .facultyService().getSmplByLikeNameAndUniversityId(filterText,
                                        inputSearchFieldsData.getUniversity().getId(), currentPage, itemsOnPage));
            } else if (clazzModelView == DepartmentModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().departmentMapper()
                        .listDepartmentDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .departmentService().getSmplByLikeNameAndFacultyId(filterText,
                                        inputSearchFieldsData.getFaculty().getId(), currentPage, itemsOnPage));
            } else if (clazzModelView == SpecializationModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().specializationMapper()
                        .listSpecializationDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .specializationService().getSmplByLikeNameAndDepartmentId(filterText,
                                        inputSearchFieldsData.getDepartment().getId(), currentPage, itemsOnPage));
            } else if (clazzModelView == DisciplineModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().disciplineMapper()
                        .listDisciplineDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .disciplineService().getSmplByLikeNameAndSpecializationId(filterText,
                                        inputSearchFieldsData.getSpecialization().getId(), currentPage, itemsOnPage));
            } else if (clazzModelView == HeadDepartmentModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().headDepartmentMapper()
                        .listHeadDepartmentDtoModelTbl(ServiceFactoryImpl.getInstance()
                                .headDepartmentService().getSmplByLikeNameAndDepartmentId(filterText,
                                        inputSearchFieldsData.getDepartment().getId(), currentPage, itemsOnPage));
            } else if (clazzModelView == TeacherModelTbl.class) {
                return MapperViewFactoryImpl.getInstance().teacherMapper()
                        .listTeacherDtoToModelTbl(ServiceFactoryImpl.getInstance()
                                .teacherService().getSmplByLikeNameAndFacultyId(filterText,
                                        inputSearchFieldsData.getFaculty().getId(), currentPage, itemsOnPage));
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
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(UniversityModelTbl.class)
                                                .child(Collections.singletonList(
                                                        RelatedTblDataBase.builder().classMdlTbl(FacultyModelTbl.class)
                                                                .build()
                                                )).build())
                                        .supplierCreate(supplierCreate)
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierPaginationDataList)
                                        .build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(FacultyModelTbl.class)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(FacultyModelTbl.class)
                                                .child(Arrays.asList(
                                                        RelatedTblDataBase.builder().classMdlTbl(DepartmentModelTbl.class)
                                                                .build(),
                                                        RelatedTblDataBase.builder().classMdlTbl(TeacherModelTbl.class)
                                                                .build()
                                                )).build())
                                        .supplierCreate(supplierCreate)
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierPaginationDataList)
                                        .build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(DepartmentModelTbl.class)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(DepartmentModelTbl.class)
                                                .child(Arrays.asList(
                                                        RelatedTblDataBase.builder().classMdlTbl(SpecializationModelTbl.class)
                                                                .build(),
                                                        RelatedTblDataBase.builder().classMdlTbl(HeadDepartmentModelTbl.class)
                                                                .build()
                                                )).build())
                                        .supplierCreate(supplierCreate)
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierPaginationDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(SpecializationModelTbl.class)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(SpecializationModelTbl.class)
                                                .child(Collections.singletonList(
                                                        RelatedTblDataBase.builder().classMdlTbl(DisciplineModelTbl.class)
                                                                .build()
                                                )).build())
                                        .supplierCreate(supplierCreate)
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierPaginationDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(DisciplineModelTbl.class)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(DisciplineModelTbl.class)
                                                .build())
                                        .supplierCreate(supplierCreate)
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierPaginationDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(HeadDepartmentModelTbl.class)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(HeadDepartmentModelTbl.class)
                                                .build())
                                        .supplierCreate(supplierCreate)
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierPaginationDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(TeacherModelTbl.class)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(TeacherModelTbl.class)
                                                .build())
                                        .supplierCreate(supplierCreate)
                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierPaginationDataList).build())
                        .toArray(ModelTableViewSupplier[]::new))
                .rootPnl(rootPnlTbls).build();
        statesSelectedItemsOnPage = myListButtons.getMapBtnForKeyViewUI().values().stream()
                .collect(Collectors.toMap(Function.identity(), (k) -> new ArrayList<>()));
    }

    protected void addingCustomComponents() {
        pnlList.add(myListButtons);
    }

    private Map<KeyForViewUI, List<StateSelectedItemOnTbl>> statesSelectedItemsOnPage;

    @Override
    public void setConfigComponents() {
        myListButtons.getMapBtnForKeyViewUI().values().parallelStream()
                .forEach(keyForViewUI -> {
                    keyForViewUI.getPv().setItemsOnPage((Integer.parseInt(
                            Objects.requireNonNull(cmbCountView.getSelectedItem()).toString())));
                    /**
                     * When user swap pages then selection don't perform correctly, so i had overridden
                     * base realization and implement myself.
                     * Basic purpose this idea is to when user drag next|previous page selected items saving itself state
                     */
                    keyForViewUI.getTbl().setSupplierCellRender((args) -> new JTableDataBase.RealizedCellRender((Integer) args[0]) {

                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                                       boolean hasFocus, int rowIndex, int columnIndex) {
                            Color fgs = UIManager.getColor("Table.dropCellForeground"),
                                    bgs = UIManager.getColor("Table.dropCellBackground");

                            Color fg = UIManager.getColor("Table.focusCellForeground"),
                                    bg = UIManager.getColor("Table.focusCellBackground");
                            List<String> selectedItemId = statesSelectedItemsOnPage.get(selectedKeyForViewUI).stream()
                                    .map(StateSelectedItemOnTbl::getUuid).toList();

                            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, columnIndex);
                            if (value instanceof UUID uuid) { // column is UUID by type
                                if (!selectedItemId.isEmpty()) {
                                    if (selectedItemId.contains(uuid.toString())) {
                                        statesSelectedItemsOnPage.get(selectedKeyForViewUI).forEach(state -> {
                                            state.setRow(rowIndex);
                                        });
                                        this.setForeground(fgs);
                                        this.setBackground(bgs);
                                    } else {
                                        statesSelectedItemsOnPage.get(selectedKeyForViewUI).forEach(state -> {
                                            state.setRow(-1);
                                        });
                                        this.setForeground(fg);
                                        this.setBackground(bg);
                                    }
                                } else {
                                    this.setForeground(fg);
                                    this.setBackground(bg);
                                }
                            } else { // column don't is UUID by type however may be same index row that have selected index row

                                if (statesSelectedItemsOnPage.get(selectedKeyForViewUI).stream()
                                        .map(StateSelectedItemOnTbl::getRow).anyMatch(index -> index == rowIndex)) {
                                    this.setForeground(fgs);
                                    this.setBackground(bgs);
                                } else {
                                    this.setForeground(fg);
                                    this.setBackground(bg);
                                }
                            }
                            return this;
                        }
                    });
                });
        setEnableCRUDbtn(false, false, false);
        setEnableComponent(false);
    }

    @Override
    public void setComponentsListeners() {
        ActionHandler handler = new ActionHandler();
        TableSelectedRowsListener handlerSelection = new HandlerSelectionRows();
        ChoiceButtonListListener handlerChoice = new HandlerChoiceButtonList();
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
        cmbCountView.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // This Method invokes two time. Firstly this method receive oldValue. After this method is invoking with new selected item
                // old value intend skip
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    paginationView.setItemsOnPage(Integer.parseInt(e.getItem().toString()));
                    initPagination(true, true, false, false);
                    selectedKeyForViewUI.getTbl().performSetData();
                }
            }
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout(0, 0));
        rootPanel.setForeground(new Color(-4520687));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        panel1.setBackground(new Color(-7881946));
        panel1.setForeground(new Color(-12959471));
        rootPanel.add(panel1, BorderLayout.WEST);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(panel2, BorderLayout.NORTH);
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("Таблицы");
        panel2.add(label1);
        pnlList = new JPanel();
        pnlList.setLayout(new BorderLayout(0, 0));
        pnlList.putClientProperty("html.disable", Boolean.FALSE);
        panel1.add(pnlList, BorderLayout.CENTER);
        listTables = new JList();
        listTables.setAlignmentX(1.0f);
        listTables.setAlignmentY(1.0f);
        listTables.setAutoscrolls(false);
        listTables.setDoubleBuffered(false);
        listTables.setEnabled(true);
        listTables.setLayoutOrientation(0);
        listTables.setMaximumSize(new Dimension(0, 300));
        listTables.setMinimumSize(new Dimension(0, 300));
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("Университеты");
        defaultListModel1.addElement("Факультеты");
        defaultListModel1.addElement("Кафедры");
        defaultListModel1.addElement("Специальности");
        defaultListModel1.addElement("Заведующий кафедрой");
        defaultListModel1.addElement("Преподавателии");
        listTables.setModel(defaultListModel1);
        listTables.setOpaque(false);
        listTables.setPreferredSize(new Dimension(0, 300));
        listTables.setVisible(true);
        listTables.setVisibleRowCount(6);
        pnlList.add(listTables, BorderLayout.CENTER);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        panel3.setMinimumSize(new Dimension(0, 0));
        panel1.add(panel3, BorderLayout.SOUTH);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(5, 5, 5, 5), 0, 0, true, false));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 20;
        panel3.add(panel4, gbc);
        btnAllDeselect = new JButton();
        btnAllDeselect.setBackground(new Color(-12697277));
        btnAllDeselect.setForeground(new Color(-3421237));
        btnAllDeselect.setMargin(new Insets(5, 5, 5, 5));
        btnAllDeselect.setText("снять весь выбор");
        panel4.add(btnAllDeselect, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDeselect = new JButton();
        btnDeselect.setBackground(new Color(-12434105));
        btnDeselect.setForeground(new Color(-2894893));
        btnDeselect.setMargin(new Insets(5, 5, 5, 5));
        btnDeselect.setText("снять выбор");
        panel4.add(btnDeselect, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDelete = new JButton();
        btnDelete.setBackground(new Color(-4520687));
        btnDelete.setMargin(new Insets(5, 0, 5, 0));
        btnDelete.setMaximumSize(new Dimension(78, 55));
        btnDelete.setText("Удалить");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel3.add(btnDelete, gbc);
        btnUpdate = new JButton();
        btnUpdate.setBackground(new Color(-2698694));
        btnUpdate.setMargin(new Insets(5, 0, 5, 0));
        btnUpdate.setMaximumSize(new Dimension(78, 55));
        btnUpdate.setText("Обновить");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel3.add(btnUpdate, gbc);
        btnCreate = new JButton();
        btnCreate.setBackground(new Color(-12338337));
        btnCreate.setEnabled(true);
        btnCreate.setMargin(new Insets(5, 0, 5, 0));
        btnCreate.setMaximumSize(new Dimension(78, 55));
        btnCreate.setText("Добавить");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel3.add(btnCreate, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        panel5.setBackground(new Color(-12959471));
        panel5.setForeground(new Color(-7881946));
        rootPanel.add(panel5, BorderLayout.CENTER);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new BorderLayout(0, 0));
        panel6.setBackground(new Color(-12513483));
        panel6.setForeground(new Color(-4511418));
        panel5.add(panel6, BorderLayout.SOUTH);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 8, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, BorderLayout.EAST);
        lbCurrentPage = new JLabel();
        lbCurrentPage.setHorizontalAlignment(0);
        lbCurrentPage.setText("");
        panel7.add(lbCurrentPage, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel7.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(0);
        label2.setText("/");
        panel7.add(label2, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel7.add(spacer2, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        lbTotalNumberPage = new JLabel();
        lbTotalNumberPage.setHorizontalAlignment(0);
        lbTotalNumberPage.setText("");
        panel7.add(lbTotalNumberPage, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel7.add(spacer3, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 1, false));
        final Spacer spacer4 = new Spacer();
        panel7.add(spacer4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        cmbCountView = new JComboBox();
        cmbCountView.setFocusable(false);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("5");
        defaultComboBoxModel1.addElement("10");
        defaultComboBoxModel1.addElement("25");
        defaultComboBoxModel1.addElement("50");
        defaultComboBoxModel1.addElement("100");
        cmbCountView.setModel(defaultComboBoxModel1);
        panel7.add(cmbCountView, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel6.add(panel8, BorderLayout.CENTER);
        btnBack = new JButton();
        btnBack.setBackground(new Color(-14671067));
        btnBack.setForeground(new Color(-1));
        btnBack.setText("<<");
        panel8.add(btnBack);
        final Spacer spacer5 = new Spacer();
        panel8.add(spacer5);
        btnNext = new JButton();
        btnNext.setBackground(new Color(-14342359));
        btnNext.setForeground(new Color(-65794));
        btnNext.setText(">>");
        panel8.add(btnNext);
        tfFilter = new HintTextField();
        panel5.add(tfFilter, BorderLayout.NORTH);
        rootPnlTbls = new JPanel();
        rootPnlTbls.setLayout(new CardLayout(0, 0));
        rootPnlTbls.setForeground(new Color(-2696815));
        panel5.add(rootPnlTbls, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

    private final class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source == btnAllDeselect) {
                log.debug("Click on the btnAllDeselect");
                myListButtons.deSelectAll(keyForView -> {
                    setEnableComponent(false);
                    setEnableCRUDbtn(false, false, false);
                    keyForView.getPv().setFilterTextNotify("");
                    DataBasePanel.this.deSetStateSelectedItemsOnPage(keyForView);
                });
            } else if (source == btnDeselect) {
                log.debug("Click on the btnDeselect");
                myListButtons.deSelectInclude(DataBasePanel.this::deSetStateSelectedItemsOnPage);
            } else if (source == btnCreate) {
                log.debug("Click on the btnCreate");
                JTableDataBase tbl = selectedKeyForViewUI.getTbl();
                String value = JOptionPane.showInternalInputDialog(DataBasePanel.this, Localizer.get("panel.message.enter.name"),
                        Localizer.get("dialog.title.input"), JOptionPane.INFORMATION_MESSAGE);
                if (value != null && !value.isBlank()) {
                    tbl.createItem(value);
                    initPagination(true, true, false, false);
                    tbl.performSetData();
                }
            } else if (source == btnDelete) {
                log.debug("Click on the btnDelete");
                if (JOptionPane.showInternalConfirmDialog(DataBasePanel.this, Localizer.get("panel.message.make-sure"),
                        Localizer.get("dialog.title.delete"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                    CompletableFuture.runAsync(() -> {
                        JTableDataBase tbl = selectedKeyForViewUI.getTbl();
                        tbl.deleteItem();
                        initPagination(true, true, false, false);
                        tbl.performSetData();
                        myListButtons.deSelectInclude(DataBasePanel.this::deSetStateSelectedItemsOnPage);
                    });
                }
            } else if (source == btnUpdate) {
                log.debug("Click on the btnUpdate");
                SwingUtilities.invokeLater(() -> {
                    UpdatePanel panel = new UpdatePanel();
                    if (JOptionPane.showConfirmDialog(DataBasePanel.this, panel,
                            Localizer.get("dialog.title.update"),
                            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                        panel.updateRequest();
                        var tbl = selectedKeyForViewUI.getTbl();
                        myListButtons.deSelectInclude(DataBasePanel.this::deSetStateSelectedItemsOnPage);
                        initPagination(true, true, false, false);
                        tbl.performSetData();
                    }
                });
            } else if (source == btnNext) {
                log.debug("Click on the btnNext");
                if (paginationView.getCurrentPage() < paginationView.getTotalPage()) {
                    paginationView.setCurrentPageNotify(paginationView.getCurrentPage() + 1);
                    selectedKeyForViewUI.getTbl().performSetData();
                }
            } else if (source == btnBack) {
                log.debug("Click on the btnBack");
                if (paginationView.getCurrentPage() > 1) {
                    paginationView.setCurrentPageNotify(paginationView.getCurrentPage() - 1);
                    selectedKeyForViewUI.getTbl().performSetData();
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

    private void initPagination() {
        initPagination(true, true, true, true);
    }

    private void initPagination(boolean notifyTotalPage,
                                boolean notifyCurrentPage,
                                boolean notifyItemOnPage,
                                boolean notifyFilterText) {
        PaginationParam paginationParam = supplierPaginationParam.get();
        if (notifyTotalPage)
            paginationView.setTotalPageNotify(paginationParam.getTotalPage());
        else paginationView.setTotalPage(paginationParam.getTotalPage());
        if (notifyCurrentPage)
            paginationView.setCurrentPageNotify(paginationParam.getCurrentPage());
        else paginationView.setCurrentPage(paginationParam.getCurrentPage());
        if (notifyItemOnPage)
            paginationView.setItemsOnPageNotify(paginationParam.getItemsOnPage());
        else paginationView.setItemsOnPage(paginationParam.getItemsOnPage());
        if (notifyFilterText)
            paginationView.setFilterTextNotify(paginationView.getFilterText()); // repaint to JTextField
    }

    private final class HandlerChoiceButtonList implements ChoiceButtonListListener {
        @Override
        public void perform(EventChoiceBtn event) {
            SwingUtilities.invokeLater(() -> {
                selectedKeyForViewUI = event.getCurrent();
                subSelectedKeyForViewUI = event.getRelatedFromCurrent();
                paginationView = selectedKeyForViewUI.getPv();
                SwingUtilities.invokeLater(() -> {
                    setEnableComponent(true);
                    initPagination();
                    selectedKeyForViewUI.getTbl().performSetData();

                    boolean isSelectedRows = selectedKeyForViewUI.getTbl().getSelectedRowCount() > 0;
                    if (isSelectedRows) {
                        setEnableCRUDbtn(true, true, true);
                    } else setEnableCRUDbtn(true, false, false);
                });
            });
        }
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
                            if (paginationView.getTotalPage() == 0) {
                                setEnablePaginationElem(false, false);
                            } else {
                                setEnablePaginationElem(false, true);
                            }
                        } else {
                            setEnablePaginationElem(true, true);
                        }
                    }

                }
                case PaginationView.TOTALPAGE -> {
                    lbTotalNumberPage.setText(String.valueOf(evt.getNewValue()));

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

    private class HandlerEnterField extends KeyAdapter {
        boolean isUpdated = false;

        @Override
        public void keyReleased(KeyEvent e) {
            String text = tfFilter.getText();
            paginationView.setFilterText(text);
            if (text.length() > 4) {
                myListButtons.deSelectInclude(DataBasePanel.this::deSetStateSelectedItemsOnPage);
                isUpdated = true;
                initPagination(true, true, true, false);
                selectedKeyForViewUI.getTbl().performSetData();
            } else if (text.isBlank() && isUpdated) {
                myListButtons.deSelectInclude(DataBasePanel.this::deSetStateSelectedItemsOnPage);
                isUpdated = false;
                initPagination(true, true, true, false);
                selectedKeyForViewUI.getTbl().performSetData();
            }

        }
    }

    private void setStatesSelectedItemsOnPage(String[] selectedItemsId, int[] selectedRows) {
        if (selectedItemsId.length != selectedRows.length)
            throw new RuntimeException("selectedItemsId.length != selectedRows");
        List<StateSelectedItemOnTbl> list = statesSelectedItemsOnPage.get(selectedKeyForViewUI);
        list.clear();
        List<StateSelectedItemOnTbl> newsel = IntStream.range(0, selectedItemsId.length)
                .mapToObj(i -> StateSelectedItemOnTbl.builder().uuid(selectedItemsId[i]).row(selectedRows[i]).build())
                .toList();
        list.addAll(newsel);
    }

    private void deSetStateSelectedItemsOnPage(KeyForViewUI keyForViewUI) {
        statesSelectedItemsOnPage.get(keyForViewUI).clear();

    }

    private void setEnableComponent(boolean enable) {
        tfFilter.setEnabled(enable);
        btnNext.setEnabled(enable);
        btnBack.setEnabled(enable);
        cmbCountView.setEnabled(enable);
    }

    private final class HandlerSelectionRows implements TableSelectedRowsListener {
        /**
         * IT very important notice!!!!
         * This possibility allowed me to define early  pre-selected item for adjusting selections with desirable result
         * <p>
         * When  user clicked on the item in table. Firstly invoke handler: {@link ListSelectionListener#valueChanged(ListSelectionEvent)}
         * this event contains property: isAdjusting. This property invoked firstly when item on the table
         * is selected. isAdjusting == true. This event already contains new selected rows
         * <p>
         * Further invoke {@link TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)}.
         * </p>
         * then after that it is called again {@link ListSelectionListener#valueChanged(ListSelectionEvent)},
         * however with new value for property: isAdjusting == false.
         * <p>
         * And now again invoked {@link TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)}.
         */
        @Override
        public void perform(TableSelectedRowsEvent event) {
            Object[] elemSelected = event.getSelectedItems();
            if (!event.isAdjusting()) {
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
                    // чтобы изменить выбор, если выбор уже был сделан
                    myListButtons.deSelectExclude(DataBasePanel.this::deSetStateSelectedItemsOnPage);
                } else {
                    if (elemSelected.length > 1) {
                        setEnableCRUDbtn(true, true, false);
                    } else {
                        setEnableCRUDbtn(true, false, false);
                    }
                    myListButtons.deEnabledExclude(DataBasePanel.this::deSetStateSelectedItemsOnPage);
                }
            } else { // isAdjusting() == true => invoked firstly when item on the table is selected
                if (elemSelected.length == 1) {
                    setStatesSelectedItemsOnPage(Arrays.stream(elemSelected).map(mapperFind).toArray(String[]::new),
                            event.getSelectedRows());
                } else if (elemSelected.length > 1) {
                    setStatesSelectedItemsOnPage(Arrays.stream(elemSelected).map(mapperFind).toArray(String[]::new),
                            event.getSelectedRows());
                } else {
                    myListButtons.deSelectInclude((key) -> false, DataBasePanel.this::deSetStateSelectedItemsOnPage);
                }
            }
        }
    }


    private class UpdatePanel extends Panel {
        private final JTextField field;

        private CombaBoxSupplierView box;

        private final Object selectedItem;

        public UpdatePanel() {
            JPanel rootPnl = new JPanel(new GridLayout(2, 2, 10, 10));

            JLabel sublbl = null;
            selectedItem = supplierInputSearchFieldData.apply(selectedKeyForViewUI.getTbl().getClassTableView());
            Object subselectedItem = supplierInputSearchFieldData.apply(subSelectedKeyForViewUI.getTbl().getClassTableView());
            if (subSelectedKeyForViewUI != selectedKeyForViewUI) {
                sublbl = new JLabel(subSelectedKeyForViewUI.getBtn().getText());
                List<?> dataList = supplierDataList.apply(subSelectedKeyForViewUI.getTbl().getClassTableView());
                box = new CombaBoxSupplierView(mapperView, dataList);
                box.setSelectedItem(subselectedItem);
            }
            JLabel curlbl = new JLabel(selectedKeyForViewUI.getBtn().getText());
            field = new JTextField(mapperView.apply(selectedItem));
            if (subSelectedKeyForViewUI != selectedKeyForViewUI) {
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

    @Setter
    @Getter
    @ToString
    @Builder
    private static final class StateSelectedItemOnTbl {
        private String uuid = null;

        private int row = -1;
    }

}