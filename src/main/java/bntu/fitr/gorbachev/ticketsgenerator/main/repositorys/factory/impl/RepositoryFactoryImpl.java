package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.RepositoryFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepositoryFactoryImpl implements RepositoryFactory {
    private static final UniversityDAO repoUniversity = new UniversityDAOImpl();
    private static final FacultyDAO repoFaculty = new FacultyDAOImpl();
    private static final DepartmentDAO repoDepartment = new DepartmentDAOImpl();
    private static final SpecializationDAO repoSpecialization = new SpecializationDAOImpl();
    private static final DisciplineDAO repoDiscipline = new DisciplineDAOImpl();
    private static final HeadDepartmentDAO repoHeadDepartment = new HeadDepartmentDAOImpl();
    private static final TeacherDAO repoTeacher = new TeacherDAOImpl();

    private static RepositoryFactory repositories;

    public static RepositoryFactory getInstance() {
        if (repositories == null) {
            synchronized (RepositoryFactory.class) {
                if (repositories == null) {
                    repositories = new RepositoryFactoryImpl();
                }
            }
        }
        return repositories;
    }

    @Override
    public UniversityDAO repositoryUniversity() {
        return repoUniversity;
    }

    @Override
    public FacultyDAO repositoryFaculty() {
        return repoFaculty;
    }

    @Override
    public DepartmentDAO repositoryDepartment() {
        return repoDepartment;
    }

    @Override
    public SpecializationDAO repositorySpecialization() {
        return repoSpecialization;
    }

    @Override
    public DisciplineDAO repositoryDiscipline() {
        return repoDiscipline;
    }

    @Override
    public HeadDepartmentDAO repositoryHeadDepartment() {
        return repoHeadDepartment;
    }

    @Override
    public TeacherDAO repositoryTeacher() {
        return repoTeacher;
    }
}
