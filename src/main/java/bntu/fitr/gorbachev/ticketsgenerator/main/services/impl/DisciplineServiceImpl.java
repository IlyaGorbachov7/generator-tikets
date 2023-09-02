package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DisciplineDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Discipline;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.DisciplineService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.displn.DisciplineNoFoundByIdException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.DisciplineMapper;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl.MapperFactoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DisciplineServiceImpl implements DisciplineService {

    private final DisciplineDAO disciplineRepo = RepositoryFactoryImpl.getInstance().repositoryDiscipline();

    private final DisciplineMapper disciplineMapper = MapperFactoryImpl.getInstance().disciplineMapper();

    private final HQueryMaster<Discipline> executor = disciplineRepo.getExecutor();

    @Override
    public DisciplineDto create(DisciplineCreateDto disciplineCreateDto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            Discipline entity = disciplineMapper.disciplineDtoToEntity(disciplineCreateDto);
            disciplineRepo.create(entity);
            return disciplineMapper.disciplineToDto(entity);
        });
    }

    @Override
    public DisciplineDto update(DisciplineDto disciplineDto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            Discipline target = disciplineRepo.findById(disciplineDto.getId())
                    .orElseThrow(DisciplineNoFoundByIdException::new);
            disciplineMapper.update(target, disciplineDto);
            disciplineRepo.update(target);
            return disciplineMapper.disciplineToDto(target);
        });
    }

    @Override
    public void delete(DisciplineDto disciplineDto) throws ServiceException {
        executor.wrapTransactional(() -> {
            Discipline entity = disciplineRepo.findById(disciplineDto.getId())
                    .orElseThrow(DisciplineNoFoundByIdException::new);
            disciplineRepo.delete(entity);
        });
    }

    @Override
    public Optional<DisciplineDto> getAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                disciplineRepo.findAny().map(disciplineMapper::disciplineToDto));
    }

    @Override
    public List<DisciplineDto> getAll() throws ServiceException {
        return executor.wrapTransactionalResultList(() -> disciplineRepo.findAll().stream()
                .map(disciplineMapper::disciplineToDto).toList());
    }

    @Override
    public List<DisciplineDto> getBySpecializationId(UUID specializationId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                disciplineMapper.disciplineToDto(
                        disciplineRepo.findBySpecializationId(specializationId)));
    }

    @Override
    public List<DisciplineDto> getBySpecializationName(String specializationName) throws ServiceException {
        return executor.wrapTransactionalResultList(()->
                disciplineMapper.disciplineToDto(
                        disciplineRepo.finBySpecializationName(specializationName)));
    }

    @Override
    public List<DisciplineDto> getByLikeNameAndSpecializationId(String likeName, UUID speciliazationId) throws ServiceException {
        return executor.wrapTransactionalResultList(()->
                disciplineMapper.disciplineToDto(
                        disciplineRepo.findByLikeNameAndSpecializationId(likeName, speciliazationId)));
    }
}
