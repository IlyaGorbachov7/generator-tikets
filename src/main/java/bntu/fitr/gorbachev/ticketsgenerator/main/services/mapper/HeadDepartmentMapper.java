package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DepartmentDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.HeadDepartment;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.deptm.DepartmentNoFoundByIdException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = FacultyMapper.class)
public abstract class HeadDepartmentMapper {

    private final DepartmentDAO departmentRepo = RepositoryFactoryImpl.getInstance().repositoryDepartment();

    public HeadDepartment headDepartmentDtoToEntity(HeadDepartmentCreateDto headDepartmentCreateDto) {
        return assembleToEntity(departmentRepo.findById(headDepartmentCreateDto.getDepartmentId())
                .orElseThrow(DepartmentNoFoundByIdException::new), headDepartmentCreateDto);
    }

    public HeadDepartment headDepartmentDtoToEntity(HeadDepartmentDto headDepartmentDto) {
        return assembleToEntity(departmentRepo.findById(headDepartmentDto.getDepartmentDto().getId())
                .orElseThrow(DepartmentNoFoundByIdException::new), headDepartmentDto);
    }

    private HeadDepartment headDepartmentDtoToEntity(HeadDepartmentSimpleDto dto) {
        return assembleToEntity(departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(DepartmentNoFoundByIdException::new), dto);
    }

    @Mapping(target = "departmentDto", source = "department")
    public abstract HeadDepartmentDto headDepartmentToDto(HeadDepartment headDepartment);

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    public abstract HeadDepartmentSimpleDto headDepartmentToSimpleDto(HeadDepartment headDepartment);

    public abstract List<HeadDepartmentDto> headDepartmentToDto(List<HeadDepartment> headDepartments);

    public abstract List<HeadDepartmentSimpleDto> headDepartmentToSimpleDto(List<HeadDepartment> headDepartmentSimpleDtos);

    public void update(HeadDepartment target, HeadDepartmentDto source) {
        HeadDepartment sourceEntity = headDepartmentDtoToEntity(source);
        update(target, sourceEntity);
    }

    public void update(HeadDepartment entity, HeadDepartmentSimpleDto dto) {
        HeadDepartment entitySource = headDepartmentDtoToEntity(dto);
        update(entity, entitySource);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    protected abstract HeadDepartment assembleToEntity(Department department, HeadDepartmentCreateDto dto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "name", source = "dto.name")
    protected abstract HeadDepartment assembleToEntity(Department department, HeadDepartmentDto dto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "name", source = "dto.name")
    protected abstract HeadDepartment assembleToEntity(Department department, HeadDepartmentSimpleDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", expression = "java(source.getDepartment())")
    protected abstract void update(@MappingTarget HeadDepartment target, HeadDepartment source);
}
