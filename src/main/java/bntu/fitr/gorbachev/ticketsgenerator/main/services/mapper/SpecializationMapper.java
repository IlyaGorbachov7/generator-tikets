package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DepartmentDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Specialization;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.deptm.DepartmentNoFoundByIdException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = DepartmentMapper.class)
public abstract class SpecializationMapper {
    private final DepartmentDAO departmentRepo = RepositoryFactoryImpl.getInstance().repositoryDepartment();

    public Specialization specializationDtoToEntity(SpecializationCreateDto specializationCreateDto) {
        return assembleEntity(
                departmentRepo.findById(specializationCreateDto.getDepartmentId())
                        .orElseThrow(DepartmentNoFoundByIdException::new),
                specializationCreateDto
        );
    }

    public Specialization specializationDtoToEntity(SpecializationDto specializationDto) {
        return assembleEntity(
                departmentRepo.findById(specializationDto.getDepartmentDto().getId())
                        .orElseThrow(DepartmentNoFoundByIdException::new),
                specializationDto
        );
    }

    @Mapping(target = "departmentDto", source = "department")
    public abstract SpecializationDto specializationToDto(Specialization specialization);

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    public abstract SpecializationSimpleDto specializationToSimpleDto(Specialization specialization);

    public abstract List<SpecializationDto> specializationToDto(List<Specialization> specializations);

    public abstract List<SpecializationSimpleDto> specializationToSimpleDto(List<Specialization> specializations);

    public void update(Specialization target, SpecializationDto sourceDto) {
        Specialization sourceEntity = specializationDtoToEntity(sourceDto);
        update(target, sourceEntity);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "specializationCreateDto.name")
    protected abstract Specialization assembleEntity(Department department, SpecializationCreateDto specializationCreateDto);

    @Mapping(target = "id", source = "specializationDto.id")
    @Mapping(target = "name", source = "specializationDto.name")
    protected abstract Specialization assembleEntity(Department department, SpecializationDto specializationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "disciplines", ignore = true)
    protected abstract void update(@MappingTarget Specialization target, Specialization source);

}
