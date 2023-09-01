package bntu.fitr.gorbachev.ticketsgenerator.main.services.factory;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.HeadDepartment;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.*;

public interface ServiceFactory {

    UniversityService universityService();

    FacultyService facultyService();

    DepartmentService departmentService();

    SpecializationService specializationService();

    DisciplineService disciplineService();

    HeadDepartmentService headDepartmentService();

    TeacherService teacherService();
}
