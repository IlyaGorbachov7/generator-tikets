package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper.factory;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper.*;

public interface MapperViewFactory {

    UniversityMapperView universityMapper();

    FacultyMapperView facultyMapper();

    DepartmentMapperView departmentMapper();

    SpecializationMapperView specializationMapper();

    DisciplineMapperView disciplineMapper();

    HeadDepartmentMapperView headDepartmentMapper();

    TeacherMapperView teacherMapper();
}
