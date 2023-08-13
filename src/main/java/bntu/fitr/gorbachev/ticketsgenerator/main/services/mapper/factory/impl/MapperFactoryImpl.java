package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.ServiceFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.MapperFactory;

public class MapperFactoryImpl implements MapperFactory {

    private static MapperFactory mapperFactory;

    public static MapperFactory getInstance() {
        if (mapperFactory == null) {
            synchronized (ServiceFactory.class) {
                if (mapperFactory == null) {
                    mapperFactory = new MapperFactoryImpl();
                }
            }
        }
        return mapperFactory;
    }

    @Override
    public UniversityMapper universityMapper() {
        return null;
    }

    @Override
    public FacultyMapper facultyMapper() {
        return null;
    }

    @Override
    public DepartmentMapper departmentMapper() {
        return null;
    }

    @Override
    public SpecializationMapper specializationMapper() {
        return null;
    }

    @Override
    public DisciplineMapper disciplineMapper() {
        return null;
    }

    @Override
    public HeadDepartmentMapper headDepartmentMapper() {
        return null;
    }

    @Override
    public TeacherMapper teacherMapper() {
        return null;
    }
}
