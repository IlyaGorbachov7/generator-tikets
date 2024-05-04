package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.*;

public interface MapperFactory {

    UniversityMapper universityMapper();

    FacultyMapper facultyMapper();

    DepartmentMapper departmentMapper();

    SpecializationMapper specializationMapper();

    DisciplineMapper disciplineMapper();

    HeadDepartmentMapper headDepartmentMapper();

    TeacherMapper teacherMapper();
}
