package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.SpecializationDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Discipline;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Specialization;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineDto;
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

    @Mapping(target = "specializationDto", source = "specialization")
    public abstract DisciplineDto disciplineToDto(Discipline discipline);

    public abstract List<DisciplineDto> disciplineToDto(List<Discipline> disciplines);

    public void update(Discipline target, DisciplineDto sourceDto) {
        Discipline source = disciplineDtoToEntity(sourceDto);
        update(target, source);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "disciplineCreateDto.name")
    protected abstract Discipline assembleToEntity(Specialization specialization, DisciplineCreateDto disciplineCreateDto);

    @Mapping(target = "id", source = "disciplineDto.id")
    @Mapping(target = "name", source = "disciplineDto.name")
    protected abstract Discipline assembleToEntity(Specialization specialization, DisciplineDto disciplineDto);

    @Mapping(target = "id", ignore = true)
    protected abstract void update(@MappingTarget Discipline target, Discipline source);
}
