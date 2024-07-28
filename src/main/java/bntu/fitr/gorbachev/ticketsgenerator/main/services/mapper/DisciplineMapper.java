package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.SpecializationDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Discipline;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Specialization;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineSimpledDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.displn.DisciplineNoFoundByIdException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.specl.SpecializationNoFoundByIdException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = SpecializationMapper.class)
public abstract class DisciplineMapper {
    private final SpecializationDAO specRepo = RepositoryFactoryImpl.getInstance().repositorySpecialization();

    public Discipline disciplineDtoToEntity(DisciplineCreateDto disciplineCreateDto) {
        return assembleToEntity(
                specRepo.findById(disciplineCreateDto.getSpecializationId()).orElseThrow(SpecializationNoFoundByIdException::new),
                disciplineCreateDto);
    }

    public Discipline disciplineDtoToEntity(DisciplineDto disciplineDto) {
        return assembleToEntity(
                specRepo.findById(disciplineDto.getSpecializationDto().getId()).orElseThrow(SpecializationNoFoundByIdException::new),
                disciplineDto);
    }

    public Discipline disciplineDtoToEntity(DisciplineSimpledDto dto) {
        return assembleToEntity(
                specRepo.findById(dto.getSpecializationId()).orElseThrow(SpecializationNoFoundByIdException::new),
                dto);
    }

    @Mapping(target = "specializationDto", source = "specialization")
    public abstract DisciplineDto disciplineToDto(Discipline discipline);

    @Mapping(target = "specializationId", source = "specialization.id")
    @Mapping(target = "specializationName", source = "specialization.name")
    public abstract DisciplineSimpledDto disciplineToSimpleDto(Discipline discipline);

    public abstract List<DisciplineDto> disciplineToDto(List<Discipline> disciplines);

    public abstract List<DisciplineSimpledDto> disciplineToSimpleDto(List<Discipline> disciplines);

    public void update(Discipline target, DisciplineDto sourceDto) {
        Discipline source = disciplineDtoToEntity(sourceDto);
        update(target, source);
    }

    public void update(Discipline entity, DisciplineSimpledDto dto) {
        Discipline entitySource = disciplineDtoToEntity(dto);
        update(entity, entitySource);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "disciplineCreateDto.name")
    protected abstract Discipline assembleToEntity(Specialization specialization, DisciplineCreateDto disciplineCreateDto);

    @Mapping(target = "id", source = "disciplineDto.id")
    @Mapping(target = "name", source = "disciplineDto.name")
    protected abstract Discipline assembleToEntity(Specialization specialization, DisciplineDto disciplineDto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "name", source = "dto.name")
    protected abstract Discipline assembleToEntity(Specialization specialization, DisciplineSimpledDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "specialization", expression = "java(source.getSpecialization())")
    protected abstract void update(@MappingTarget Discipline target, Discipline source);
}
