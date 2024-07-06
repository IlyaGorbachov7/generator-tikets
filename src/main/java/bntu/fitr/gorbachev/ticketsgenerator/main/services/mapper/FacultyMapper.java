package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.UniversityDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultySimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.univ.UniversityNoFoundByIdException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = UniversityMapper.class)
public abstract class FacultyMapper {

    private final UniversityDAO universityRepo = RepositoryFactoryImpl.getInstance().repositoryUniversity();

    public Faculty facultyDtoToFaculty(FacultyDto facultyDto) {
        return assembleToEntity(universityRepo.findById(facultyDto.getUniversityDto().getId())
                .orElseThrow(UniversityNoFoundByIdException::new), facultyDto);
    }

    public Faculty facultyDtoToFaculty(FacultyCreateDto facultyCreateDto) {
        return assembleToEntity(universityRepo.findById(facultyCreateDto.getUniversityId())
                .orElseThrow(UniversityNoFoundByIdException::new), facultyCreateDto);
    }

    public Faculty facultyDtoToFaculty(FacultySimpleDto dto) {
        return assembleToEntity(
                universityRepo.findById(dto.getUniversityId()).orElseThrow(UniversityNoFoundByIdException::new),
                dto);
    }


    @Mapping(target = "universityDto", source = "university")
    public abstract FacultyDto facultyToFacultyDto(Faculty faculty);

    @Mapping(target = "universityId", source = "university.id")
    @Mapping(target = "universityName", source = "university.name")
    public abstract FacultySimpleDto facultyToFacultySmplDto(Faculty faculty);

    public abstract List<FacultySimpleDto> facultyToFacultySimpleDto(List<Faculty> faculties);

    public abstract List<FacultyDto> facultyToFacultyDto(List<Faculty> faculties);

    public abstract List<Faculty> facultyDtoToFaculty(List<FacultyDto> facultyDto);

    @Mapping(target = "id", ignore = true)
    public void update(@MappingTarget Faculty faculty, FacultyDto facultyDto) {
        Faculty entitySource = facultyDtoToFaculty(facultyDto);
        update(faculty, entitySource);
    }

    public void update(Faculty faculty, FacultySimpleDto dto) {
        Faculty facultySource = facultyDtoToFaculty(dto);
        update(faculty, facultySource);
    }

    @Mapping(target = "id", source = "facultyDto.id")
    @Mapping(target = "name", source = "facultyDto.name")
    @Mapping(target = "departments", ignore = true)
    protected abstract Faculty assembleToEntity(University university, FacultyDto facultyDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "facultyCreateDto.name")
    @Mapping(target = "departments", ignore = true)
    protected abstract Faculty assembleToEntity(University university, FacultyCreateDto facultyCreateDto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "name", source = "dto.name")
    protected abstract Faculty assembleToEntity(University university, FacultySimpleDto dto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departments", ignore = true)
    @Mapping(target = "university", expression = "java(source.getUniversity())")
    protected abstract void update(@MappingTarget Faculty target, Faculty source);
}
