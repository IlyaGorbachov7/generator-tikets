package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.SpecializationDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Specialization;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.SpecializationService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.specl.SpecializationNoFoundByIdException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.SpecializationMapper;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl.MapperFactoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpecializationServiceImpl implements SpecializationService {
    private final SpecializationDAO specializationRepo = RepositoryFactoryImpl.getInstance().repositorySpecialization();
    private final SpecializationMapper specializationMapper = MapperFactoryImpl.getInstance().specializationMapper();
    private final HQueryMaster<Specialization> executor = specializationRepo.getExecutor();

    @Override
    public SpecializationDto create(SpecializationCreateDto specializationCreateDto) {
        return executor.wrapTransactionalEntitySingle(() -> {
            Specialization entity = specializationMapper.specializationDtoToEntity(specializationCreateDto);
            specializationRepo.create(entity);
            return specializationMapper.specializationToDto(entity);
        });
    }

    @Override
    public SpecializationDto update(SpecializationDto specializationDto) {
        return executor.wrapTransactionalEntitySingle(() -> {
            Specialization entityTarget = specializationRepo.findById(specializationDto.getId())
                    .orElseThrow(SpecializationNoFoundByIdException::new);
            specializationMapper.update(entityTarget, specializationDto);
            specializationRepo.update(entityTarget);
            return specializationMapper.specializationToDto(entityTarget);
        });
    }

    @Override
    public void delete(SpecializationDto specializationDto) {
        executor.wrapTransactional(() -> {
            Specialization entity = specializationRepo.findById(specializationDto.getId())
                    .orElseThrow(SpecializationNoFoundByIdException::new);
            specializationRepo.delete(entity);
        });
    }

    @Override
    public Optional<SpecializationDto> getAny() {
        return executor.wrapTransactionalEntitySingle(() ->
                specializationRepo.findAny().map(specializationMapper::specializationToDto));
    }

    @Override
    public List<SpecializationDto> getAll() {
        return executor.wrapTransactionalResultList(() -> specializationRepo.findAll()
                .stream().map(specializationMapper::specializationToDto).toList());
    }

    @Override
    public Optional<SpecializationDto> getByName(String name) {
        return executor.wrapTransactionalEntitySingle(()->
                specializationRepo.findByName(name).map(specializationMapper::specializationToDto));
    }

    @Override
    public List<SpecializationDto> getByDepartmentId(UUID departmentId) {
        return executor.wrapTransactionalResultList(() ->
                specializationMapper.specializationToDto(
                        specializationRepo.findByDepartmentId(departmentId)));
    }

    @Override
    public List<SpecializationDto> getByDepartmentName(String departmentName) {
        return executor.wrapTransactionalResultList(() ->
                specializationMapper.specializationToDto(
                        specializationRepo.findByDepartmentName(departmentName)));
    }

    @Override
    public List<SpecializationDto> getByLikeNameAndDepartmentId(String likeName, UUID departmentId) {
        return executor.wrapTransactionalResultList(() ->
                specializationMapper.specializationToDto(
                        specializationRepo.findByLikeNameAndDepartmentId(likeName, departmentId)));
    }

    @Override
    public int getCountByName(String name) {
        return 0;
    }

    @Override
    public int getCountByLikeNameAndDepartmentId(String likeName, UUID departmentId) {
        return 0;
    }
}
