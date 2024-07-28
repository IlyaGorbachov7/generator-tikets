package bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.HeadDepartment;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.ServiceFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.impl.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceFactoryImpl implements ServiceFactory {

    private static ServiceFactory serviceFactory;

    private final UniversityService universityService = new UniversityServiceImpl();

    private final FacultyService facultyService = new FacultyServiceImpl();

    private final DepartmentService departmentService = new DepartmentServiceImpl();

    private final SpecializationService specializationService = new SpecializationServiceImpl();

    private final DisciplineService disciplineService = new DisciplineServiceImpl();

    private final HeadDepartmentService headDepartmentService = new HeadDepartmentServiceImpl();

    private final TeacherService teacherService = new TeacherServiceImpl();

    public static ServiceFactory getInstance() {
        if (serviceFactory == null) {
            synchronized (ServiceFactory.class) {
                if (serviceFactory == null) {
                    serviceFactory = new ServiceFactoryImpl();
                }
            }
        }
        return serviceFactory;
    }

    @Override
    public UniversityService universityService() {
        return universityService;
    }

    @Override
    public FacultyService facultyService() {
        return facultyService;
    }

    @Override
    public DepartmentService departmentService() {
        return departmentService;
    }

    @Override
    public SpecializationService specializationService() {
        return specializationService;
    }

    @Override
    public DisciplineService disciplineService() {
        return disciplineService;
    }

    @Override
    public HeadDepartmentService headDepartmentService() {
        return headDepartmentService;
    }

    @Override
    public TeacherService teacherService() {
        return teacherService;
    }
}
