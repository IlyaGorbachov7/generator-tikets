package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.*;

public interface RepositoryFactory {

    UniversityDAO repositoryUniversity();

    FacultyDAO repositoryFaculty();

    DepartmentDAO repositoryDepartment();

    SpecializationDAO repositorySpecialization();

    DisciplineDAO repositoryDiscipline();

    HeadDepartmentDAO repositoryHeadDepartment();

    TeacherDAO repositoryTeacher();

}
