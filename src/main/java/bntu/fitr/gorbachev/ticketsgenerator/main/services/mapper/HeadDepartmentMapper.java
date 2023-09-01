package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DepartmentDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.HeadDepartmentDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.HeadDepartment;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.deptm.DepartmentNoFoundByIdException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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

    @Mapping(target = "departmentDto", source = "department")
    public abstract HeadDepartmentDto headDepartmentToDto(HeadDepartment headDepartment);

    public void update(HeadDepartment target, HeadDepartmentDto source) {
        HeadDepartment sourceEntity = headDepartmentDtoToEntity(source);
        update(target, sourceEntity);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    protected abstract HeadDepartment assembleToEntity(Department department, HeadDepartmentCreateDto dto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "name", source = "dto.name")
    protected abstract HeadDepartment assembleToEntity(Department department, HeadDepartmentDto dto);

    @Mapping(target = "id", ignore = true)
    protected abstract void update(@MappingTarget HeadDepartment target, HeadDepartment source);
}
