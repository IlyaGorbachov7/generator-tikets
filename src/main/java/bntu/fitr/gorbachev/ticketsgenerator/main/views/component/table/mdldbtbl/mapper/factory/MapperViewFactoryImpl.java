package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper.factory;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mapstruct.factory.Mappers;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class MapperViewFactoryImpl implements MapperViewFactory {

    private static MapperViewFactory instance;

    private final UniversityMapperView universityMapperView = Mappers.getMapper(UniversityMapperView.class);
    private final FacultyMapperView facultyMapperView = Mappers.getMapper(FacultyMapperView.class);
    private final DepartmentMapperView departmentMapperView = Mappers.getMapper(DepartmentMapperView.class);
    private final SpecializationMapperView specializationMapperView = Mappers.getMapper(SpecializationMapperView.class);
    private final DisciplineMapperView disciplineMapperView = Mappers.getMapper(DisciplineMapperView.class);
    private final HeadDepartmentMapperView headDepartmentMapperView = Mappers.getMapper(HeadDepartmentMapperView.class);
    private final TeacherMapperView teacherMapperView = Mappers.getMapper(TeacherMapperView.class);

    public static MapperViewFactory getInstance() {
        if (instance == null) {
            synchronized (MapperViewFactory.class) {
                if (instance == null) {
                    instance = new MapperViewFactoryImpl();
                }
            }
        }
        return instance;
    }

    @Override

    public UniversityMapperView universityMapper() {
        return universityMapperView;
    }

    @Override
    public FacultyMapperView facultyMapper() {
        return facultyMapperView;
    }

    @Override
    public DepartmentMapperView departmentMapper() {
        return departmentMapperView;
    }

    @Override
    public SpecializationMapperView specializationMapper() {
        return specializationMapperView;
    }

    @Override
    public DisciplineMapperView disciplineMapper() {
        return disciplineMapperView;
    }

    @Override
    public HeadDepartmentMapperView headDepartmentMapper() {
        return headDepartmentMapperView;
    }

    @Override
    public TeacherMapperView teacherMapper() {
        return teacherMapperView;
    }
}
