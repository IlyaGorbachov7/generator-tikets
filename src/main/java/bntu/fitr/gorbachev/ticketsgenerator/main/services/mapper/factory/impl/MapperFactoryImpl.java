package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.ServiceFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.MapperFactory;
import org.mapstruct.factory.Mappers;

public class MapperFactoryImpl implements MapperFactory {

    private static MapperFactory mapperFactory;

    private final UniversityMapper universityMapper = Mappers.getMapper(UniversityMapper.class);
    private final FacultyMapper facultyMapper = Mappers.getMapper(FacultyMapper.class);
    private final DepartmentMapper departmentMapper = Mappers.getMapper(DepartmentMapper.class);
    private final SpecializationMapper specializationMapper = Mappers.getMapper(SpecializationMapper.class);
    private final HeadDepartmentMapper headDepartmentMapper = Mappers.getMapper(HeadDepartmentMapper.class);
    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);
    private final DisciplineMapper disciplineMapper = Mappers.getMapper(DisciplineMapper.class);

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
        return universityMapper;
    }

    @Override
    public FacultyMapper facultyMapper() {
        return facultyMapper;
    }

    @Override
    public DepartmentMapper departmentMapper() {
        return departmentMapper;
    }

    @Override
    public SpecializationMapper specializationMapper() {
        return specializationMapper;
    }

    @Override
    public DisciplineMapper disciplineMapper() {
        return disciplineMapper;
    }

    @Override
    public HeadDepartmentMapper headDepartmentMapper() {
        return headDepartmentMapper;
    }

    @Override
    public TeacherMapper teacherMapper() {
        return teacherMapper;
    }
}
