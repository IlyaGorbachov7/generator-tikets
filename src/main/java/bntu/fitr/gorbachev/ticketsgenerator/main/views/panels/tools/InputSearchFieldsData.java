package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InputSearchFieldsData {

    private UniversityModelTbl university;

    private FacultyModelTbl faculty;

    private DepartmentModelTbl department;

    private SpecializationModelTbl specialization;

    private DisciplineModelTbl discipline;

    private HeadDepartmentModelTbl headDepartment;

    private TeacherModelTbl teacher;
}
