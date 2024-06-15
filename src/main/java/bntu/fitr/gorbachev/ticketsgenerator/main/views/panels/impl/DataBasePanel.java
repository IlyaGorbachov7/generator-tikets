package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.CombaBoxSupplierView;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.MyJCompoBox;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.MyListButtons;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.abservers.TableSelectedRowsListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper.factory.MapperViewFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.textfield.HintTextField;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.InputFieldsDataTbl;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
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

    private Function<Object, String> mapper;

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

        mapper = o -> {
            String map = null;
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
                                                .child(Collections.singletonList(
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
                                                .child(Collections.singletonList(
                                                        RelatedTblDataBase.builder().classMdlTbl(DisciplineModelTbl.class).build()
                                                )).build())
//                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(DisciplineModelTbl.class)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(DisciplineModelTbl.class).build())
                                        .supplierCreate(supplierCreate)
//                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(HeadDepartmentModelTbl.class)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(HeadDepartmentModelTbl.class).build())
                                        .supplierCreate(supplierCreate)
//                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierDataList).build(),
                                ModelTableViewSupplier.builder()
                                        .clazzModelView(TeacherModelTbl.class)
                                        .relatedMdlTbl(RelatedTblDataBase.builder().classMdlTbl(TeacherModelTbl.class).build())
                                        .supplierCreate(supplierCreate)
//                                        .supplierUpdate(supplierUpdate)
                                        .supplierDelete(supplierDelete)
                                        .supplierData(supplierDataList).build())
                        .toArray(ModelTableViewSupplier[]::new))
                .rootPnl(rootPnlTbls).build();
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
        ActionListener handlerChoice = new HandlerChoiceButtonList();

        btnDeselect.addActionListener(handler);
        btnCreate.addActionListener(handler);
        btnUpdate.addActionListener(handler);
        btnDelete.addActionListener(handler);
        btnNext.addActionListener(handler);
        btnBack.addActionListener(handler);

        myListButtons.addChoiceListener(handlerChoice);
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
            }
            if (source == btnCreate) {
                JTableDataBase tbl = myListButtons.getMapBtnForKeyViewUI()
                        .get(myListButtons.getSelectedBtn())
                        .getTbl();
                String value = JOptionPane.showInternalInputDialog(DataBasePanel.this, "Введите название: ",
                        "Input Dialog", JOptionPane.INFORMATION_MESSAGE);
                if (value != null && !value.isBlank()) {
                    tbl.createItem(value);
                    tbl.performSetData();
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
                        myListButtons.deSelectInclude();
                    });
                }
            } else if (source == btnUpdate) {
                CompletableFuture.runAsync(() -> {
                    UpdatePanel panel = new UpdatePanel();
                    if (JOptionPane.showConfirmDialog(DataBasePanel.this, panel,
                            "Update Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                        panel.updateRequest();
                    }
                });
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
            JButton seletedBtn = (JButton) e.getSource();
            boolean isSelectedRows = myListButtons.getMapBtnForKeyViewUI()
                                             .get(seletedBtn).getTbl().getSelectedRowCount() > 0;
            if (isSelectedRows) {
                setEnableCRUDbtn(true, true, true);
            } else setEnableCRUDbtn(true, false, false);
        }
    }

    private class UpdatePanel extends Panel {
        private JTextField field;

        private CombaBoxSupplierView box;

        private Object selectedItem;

        private Object subselectedItem;

        public UpdatePanel() {
            KeyForViewUI selectedTblView = myListButtons.getMapBtnForKeyViewUI().get(myListButtons.getSelectedBtn());
            KeyForViewUI subSelectedTblView = myListButtons.getMapBtnForKeyViewUI().values()
                    .stream().collect(Collectors.collectingAndThen(Collectors.filtering(keyForView -> {
                        var tbl = keyForView.getTbl();
                        var node = tbl.getRelatedMdlTbl();
                        if (node == null || node == selectedTblView.getTbl().getRelatedMdlTbl()) return false;
                        return node.getChild().contains(selectedTblView.getTbl().getRelatedMdlTbl());
                    }, Collectors.toUnmodifiableList()), downList -> downList.isEmpty()
                            ? selectedTblView : downList.get(0)));


            JPanel rootPnl = new JPanel(new GridLayout(2, 2, 10, 10));

            JLabel sublbl = null;
            selectedItem = selectedTblView.getTbl().getSelectedItem();
            subselectedItem = subSelectedTblView.getTbl().getSelectedItem();
            if (subSelectedTblView != selectedTblView) {
                sublbl = new JLabel(subSelectedTblView.getBtn().getText());
                List<?> dataList = subSelectedTblView.getTbl()
                        .getSupplierDataList().apply(subSelectedTblView.getTbl().getClassTableView());
                box = new CombaBoxSupplierView(mapper, dataList);
                box.setSelectedItem(subselectedItem);
            }
            JLabel curlbl = new JLabel(selectedTblView.getBtn().getText());
            field = new JTextField(mapper.apply(selectedItem));
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
            String subValueOldBox = mapper.apply(subselectedItem);
            String subValueSelectedUserBox = mapper.apply(box.getSelectedItem());
            System.out.println("oldValue : "+ subValueOldBox + " ::: newValue : "+ subValueSelectedUserBox);


            String valueOldText = mapper.apply(selectedItem);
            String valueUserText =  field.getText();
            System.out.println("oldTextValue : "+ valueOldText + " ::: newTextValue : "+ valueUserText);


            myListButtons.deSelectInclude();

        }
    }

}