package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.fclt.FacultyNoFoundByIdException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = FacultyMapper.class)
public abstract class DepartmentMapper {
    private final FacultyDAO facultyRepo = RepositoryFactoryImpl.getInstance().repositoryFaculty();

    public Department departmentDtoToDepartment(DepartmentCreateDto departmentCreateDto) {
        return assembleEntity(
                facultyRepo.findById(departmentCreateDto.getFacultyId()).orElseThrow(FacultyNoFoundByIdException::new),
                departmentCreateDto);
    }

    public Department departmentDtoToDepartment(DepartmentDto departmentDto) {
        return assembleEntity(
                facultyRepo.findById(departmentDto.getFacultyDto().getId()).orElseThrow(FacultyNoFoundByIdException::new),
                departmentDto);
    }

    public Department departmentSmplDtoToDepartment(DepartmentSimpleDto dto) {
        return assembleEntity(facultyRepo.findById(dto.getFacultyId()).orElseThrow(FacultyNoFoundByIdException::new),
                dto);
    }


    @Mapping(target = "facultyDto", source = "faculty")
    public abstract DepartmentDto departmentToDepartmentDto(Department department);

    @Mapping(target = "facultyId", source = "faculty.id")
    @Mapping(target = "facultyName", source = "faculty.name")
    public abstract DepartmentSimpleDto departmentToDepartmentSimpleDto(Department department);

    public void update(Department department, DepartmentDto departmentDto) {
        Department departmentSource = departmentDtoToDepartment(departmentDto);
        update(department, departmentSource);
    }

    public void update(Department department, DepartmentSimpleDto dto) {
        Department departmentSource = departmentSmplDtoToDepartment(dto);
        update(department, departmentSource);

    }

    public abstract List<DepartmentDto> departmentToDepartmentDto(List<Department> departmentList);

    public abstract List<DepartmentSimpleDto> departmentToDepartmentSimpleDto(List<Department> departmentList);

    public abstract List<Department> departmentDtoToDepartment(List<DepartmentDto> departmentDtoList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "departmentCreateDto.name")
    protected abstract Department assembleEntity(Faculty faculty, DepartmentCreateDto departmentCreateDto);

    @Mapping(target = "id", source = "departmentDto.id")
    @Mapping(target = "name", source = "departmentDto.name")
    protected abstract Department assembleEntity(Faculty faculty, DepartmentDto departmentDto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "name", source = "dto.name")
    protected abstract Department assembleEntity(Faculty faculty, DepartmentSimpleDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "specializations", ignore = true)
    @Mapping(target = "faculty", expression = "java(source.getFaculty())")
    protected abstract void update(@MappingTarget Department target, Department source);
}
