package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.fclt.FacultyNoFoundByIdException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = FacultyMapper.class)
public abstract class TeacherMapper {

    private final FacultyDAO facultyRepo = RepositoryFactoryImpl.getInstance().repositoryFaculty();

    public Teacher teacherDtoToEntity(TeacherCreateDto teacherCreateDto) {
        return assembleToEntity(facultyRepo.findById(teacherCreateDto.getFacultyId())
                .orElseThrow(FacultyNoFoundByIdException::new), teacherCreateDto);
    }

    public Teacher teacherDtoToEntity(TeacherDto teacherDto) {
        return assembleToEntity(facultyRepo.findById(teacherDto.getFacultyDto().getId())
                .orElseThrow(FacultyNoFoundByIdException::new), teacherDto);
    }

    private Teacher teacherDtoToEntity(TeacherSimpleDto dto) {
        return assembleToEntity(facultyRepo.findById(dto.getFacultyId()).orElseThrow(FacultyNoFoundByIdException::new),
                dto);
    }

    @Mapping(target = "facultyDto", source = "faculty")
    public abstract TeacherDto teacherToDto(Teacher teacher);

    @Mapping(target = "facultyId", source = "faculty.id")
    @Mapping(target = "facultyName", source = "faculty.name")
    public abstract TeacherSimpleDto teacherToSimpleDto(Teacher teacher);

    public void update(Teacher target, TeacherDto source) {
        Teacher sourceEntity = teacherDtoToEntity(source);
        update(target, sourceEntity);
    }

    public void update(Teacher entity, TeacherSimpleDto dto) {
        Teacher entitySource = teacherDtoToEntity(dto);
        update(entity, entitySource);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    protected abstract Teacher assembleToEntity(Faculty faculty, TeacherCreateDto dto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "name", source = "dto.name")
    protected abstract Teacher assembleToEntity(Faculty faculty, TeacherDto dto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "name", source = "dto.name")
    protected abstract Teacher assembleToEntity(Faculty faculty, TeacherSimpleDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "faculty", expression = "java(source.getFaculty())")
    protected abstract void update(@MappingTarget Teacher target, Teacher source);
}
