package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DisciplineDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Discipline;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.DisciplineService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineSimpledDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.other.PaginationParam;
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
    public DisciplineSimpledDto createSmpl(DisciplineCreateDto disciplineCreateDto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            Discipline entity = disciplineMapper.disciplineDtoToEntity(disciplineCreateDto);
            disciplineRepo.create(entity);
            return disciplineMapper.disciplineToSimpleDto(entity);
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
    public DisciplineSimpledDto update(DisciplineSimpledDto dto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(()->{
            Discipline entity = disciplineRepo.findById(dto.getId()).orElseThrow(DisciplineNoFoundByIdException::new);
            disciplineMapper.update(entity, dto);
            disciplineRepo.update(entity);
            return disciplineMapper.disciplineToSimpleDto(entity);
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
    public void deleteSmpl(DisciplineSimpledDto dto) throws ServiceException {
        executor.wrapTransactional(() -> disciplineRepo.delete(disciplineRepo.findById(dto.getId())
                .orElseThrow(DisciplineNoFoundByIdException::new)));
    }

    @Override
    public void deleteSmpl(List<DisciplineSimpledDto> list) throws ServiceException {
        executor.wrapTransactional(() -> list.forEach(this::deleteSmpl));
    }

    @Override
    public Optional<DisciplineDto> getAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                disciplineRepo.findAny().map(disciplineMapper::disciplineToDto));
    }

    @Override
    public Optional<DisciplineSimpledDto> getSmplAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                disciplineRepo.findAny().map(disciplineMapper::disciplineToSimpleDto));
    }

    @Override
    public List<DisciplineDto> getAll() throws ServiceException {
        return executor.wrapTransactionalResultList(() -> disciplineRepo.findAll().stream()
                .map(disciplineMapper::disciplineToDto).toList());
    }

    @Override
    public Optional<DisciplineDto> getByName(String name) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                disciplineRepo.findByName(name).map(disciplineMapper::disciplineToDto));
    }

    @Override
    public Optional<DisciplineSimpledDto> getSmplByName(String name) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                disciplineRepo.findByName(name).map(disciplineMapper::disciplineToSimpleDto));
    }

    @Override
    public List<DisciplineDto> getBySpecializationId(UUID specializationId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                disciplineMapper.disciplineToDto(
                        disciplineRepo.findBySpecializationId(specializationId)));
    }

    @Override
    public long countBySpecializationId(UUID specializationId) throws ServiceException {
        return disciplineRepo.countBySpecializationId(specializationId);
    }

    @Override
    public List<DisciplineDto> getBySpecializationName(String specializationName) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                disciplineMapper.disciplineToDto(
                        disciplineRepo.findBySpecializationName(specializationName)));
    }

    @Override
    public long countBySpecializationName(String specializationName) throws ServiceException {
        return disciplineRepo.countBySpecializationName(specializationName);
    }

    @Override
    public List<DisciplineDto> getByLikeNameAndSpecializationId(String likeName, UUID specializationId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                disciplineMapper.disciplineToDto(
                        disciplineRepo.findByLikeNameAndSpecializationId(likeName, specializationId)));
    }

    @Override
    public long countByLikeNameAndSpecializationId(String likeName, UUID specializationId) throws ServiceException {
        return disciplineRepo.ByLikeNameAndSpecializationId(likeName, specializationId);
    }

    @Override
    public List<DisciplineSimpledDto> getSmplBySpecializationId(UUID id) {
        return executor.wrapTransactionalResultList(() ->
                disciplineMapper.disciplineToSimpleDto(
                        disciplineRepo.findBySpecializationId(id)));
    }

    @Override
    public PaginationParam calculatePageParam(int itemsOnPage, int currentPage, String filterText, UUID specializationId) {
        long totalItems = filterText.isBlank() ? disciplineRepo.countBySpecializationId(specializationId) :
                disciplineRepo.count(filterText, specializationId);
        int totalPage = (int) (((totalItems % itemsOnPage) == 0.0) ? (totalItems / itemsOnPage) : (totalItems / itemsOnPage) + 1);
        return PaginationParam.builder()
                .currentPage((currentPage > totalPage) ? 1 : currentPage)
                .totalPage(totalPage)
                .itemsOnPage(itemsOnPage)
                .build();
    }
}
