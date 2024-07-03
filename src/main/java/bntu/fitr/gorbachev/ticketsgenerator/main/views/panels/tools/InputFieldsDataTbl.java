package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InputFieldsDataTbl {

    private UniversityModelTbl university;

    private FacultyModelTbl faculty;

    private DepartmentModelTbl department;

    private SpecializationModelTbl specialization;

    private DisciplineModelTbl discipline;

    private HeadDepartmentModelTbl headDepartment;

    private TeacherModelTbl teacher;

}
