package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DepartmentDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.DepartmentService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.other.PaginationParam;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.deptm.DepartmentNoFoundByIdException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.DepartmentMapper;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl.MapperFactoryImpl;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentDAO departmentRepo = RepositoryFactoryImpl.getInstance().repositoryDepartment();
    private final DepartmentMapper departmentMapper = MapperFactoryImpl.getInstance().departmentMapper();
    private final HQueryMaster<Department> executor = departmentRepo.getExecutor();

    @Override
    public DepartmentDto create(DepartmentCreateDto departmentCreateDto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            Department entity = departmentMapper.departmentDtoToDepartment(departmentCreateDto);
            departmentRepo.create(entity);
            return departmentMapper.departmentToDepartmentDto(entity);
        });
    }

    @Override
    public DepartmentSimpleDto createSmpl(DepartmentCreateDto departmentCreateDto) throws ServiceException {
        Department entity = departmentMapper.departmentDtoToDepartment(departmentCreateDto);
        departmentRepo.create(entity);
        return departmentMapper.departmentToDepartmentSimpleDto(entity);
    }

    @Override
    public DepartmentDto update(DepartmentDto departmentDto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            Department entity = departmentRepo.findById(departmentDto.getId()).orElseThrow(DepartmentNoFoundByIdException::new);
            departmentMapper.update(entity, departmentDto);
            /*
            I should necessarily perform repo.update, because current transaction still don't committed.
            However you remember, update will be done after commit of the transaction.
            However, here directly entity convert to DTO.
            So I must implicitly perform update of operation, that this reflected on the result mapping.
            */
            departmentRepo.update(entity);
            return departmentMapper.departmentToDepartmentDto(entity);
        });
    }

    @Override
    public DepartmentSimpleDto updateSmpl(DepartmentSimpleDto dto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            Department entity = departmentRepo.findById(dto.getId()).orElseThrow(DepartmentNoFoundByIdException::new);
            departmentMapper.update(entity, dto);
            departmentRepo.update(entity);
            return departmentMapper.departmentToDepartmentSimpleDto(entity);
        });
    }

    @Override
    public void delete(DepartmentDto departmentDto) throws ServiceException {
        executor.wrapTransactional(() -> {
            Department entity = departmentRepo.findById(departmentDto.getId()).orElseThrow(DepartmentNoFoundByIdException::new);
            departmentRepo.delete(entity);
        });
    }

    @Override
    public void deleteSmpl(DepartmentSimpleDto facultyDto) throws ServiceException {
        executor.wrapTransactional(() -> {
            departmentRepo.delete(departmentRepo.findById(facultyDto.getId())
                    .orElseThrow(DepartmentNoFoundByIdException::new));
        });
    }

    @Override
    public void deleteSmpl(List<DepartmentSimpleDto> list) throws ServiceException {
        executor.wrapTransactional(() -> list.forEach(this::deleteSmpl));
    }

    @Override
    public Optional<DepartmentDto> getByName(String name) throws ServiceException {
        // Why we were give operation : findByName inside wrap transaction operation?
        // Answer: When mapper used, mapper invoke method: getFaculty of the database object, this method is LAZY initialization
        // then it does request to database for receive data. But for this, a transaction and session must be opened.
        return executor.wrapTransactionalEntitySingle(() ->
                departmentRepo.findByName(name).map(departmentMapper::departmentToDepartmentDto));
    }

    @Override
    public Optional<DepartmentSimpleDto> getSmplDtoByName(String name) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                departmentRepo.findByName(name).map(departmentMapper::departmentToDepartmentSimpleDto));
    }

    @Override
    public Optional<DepartmentDto> getAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                departmentRepo.findAny().map(departmentMapper::departmentToDepartmentDto)
        );
    }

    @Override
    public Optional<DepartmentSimpleDto> getSmplAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                departmentRepo.findAny().map(departmentMapper::departmentToDepartmentSimpleDto));
    }

    @Override
    public List<DepartmentDto> getAll() throws ServiceException {
        return executor.wrapTransactionalResultList(() -> departmentRepo.findAll()
                .stream().map(departmentMapper::departmentToDepartmentDto).toList());
    }

    @Override
    public List<DepartmentDto> getByFacultyId(UUID facultyId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                departmentMapper.departmentToDepartmentDto(
                        departmentRepo.findByFacultyId(facultyId)));
    }

    @Override
    public List<DepartmentDto> getByFacultyId(UUID facultyId, int page, int itemsOnPage) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                departmentMapper.departmentToDepartmentDto(
                        departmentRepo.findByFacultyId(facultyId, page, itemsOnPage)));
    }

    @Override
    public List<DepartmentSimpleDto> getSmplByFacultyId(UUID facultyId, int page, int itemsOnPage) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                departmentMapper.departmentToDepartmentSimpleDto(
                        departmentRepo.findByFacultyId(facultyId, page, itemsOnPage)));
    }

    @Override
    public long countByFacultyId(UUID facultyId) throws ServiceException {
        return departmentRepo.countByFacultyId(facultyId);
    }

    @Override
    public List<DepartmentDto> getByFacultyName(String facultyName) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                departmentMapper.departmentToDepartmentDto(
                        departmentRepo.findByFacultyName(facultyName)));
    }

    @Override
    public long countByFacultyName(String facultyName) throws ServiceException {
        return departmentRepo.countByFacultyName(facultyName);
    }

    @Override
    public List<DepartmentDto> getByLikeNameAndFacultyId(String likeName, UUID facultyId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                departmentMapper.departmentToDepartmentDto(
                        departmentRepo.findByLikeNameAndFacultyId(likeName, facultyId)));
    }

    @Override
    public List<DepartmentDto> getByLikeNameAndFacultyId(String likeName, UUID facultyId, int page, int itemsOnPage) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                departmentMapper.departmentToDepartmentDto(
                        departmentRepo.findByLikeNameAndFacultyId(likeName, facultyId, page, itemsOnPage)));
    }

    @Override
    public List<DepartmentSimpleDto> getSmplByLikeNameAndFacultyId(String likeName, UUID facultyId, int page, int itemsOnPage) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                departmentMapper.departmentToDepartmentSimpleDto(
                        departmentRepo.findByLikeNameAndFacultyId(likeName, facultyId, page, itemsOnPage)));
    }

    @Override
    public long countByLikeNameAndFacultyId(String likeName, UUID facultyId) throws ServiceException {
        return departmentRepo.countByLikeNameAndFacultyId(likeName, facultyId);
    }

    @Override
    public List<DepartmentSimpleDto> getSmplByFacultyId(UUID id) {
        return executor.wrapTransactionalResultList(() ->
                departmentMapper.departmentToDepartmentSimpleDto(
                        departmentRepo.findByFacultyId(id)));
    }

    @Override
    public PaginationParam calculatePageParam(final int itemsOnPage, final int currentPage,
                                              final @NonNull String filterText, final @NonNull UUID facultyId) {
        long totalItems = filterText.isBlank() ? departmentRepo.countByFacultyId(facultyId) :
                departmentRepo.countByLikeNameAndFacultyId(filterText, facultyId);
        int totalPage = (int) (((totalItems % itemsOnPage) == 0.0) ? (totalItems / itemsOnPage) : (totalItems / itemsOnPage) + 1);
        return PaginationParam.builder()
                .currentPage((currentPage > totalPage) ? 1 : currentPage)
                .totalPage(totalPage)
                .itemsOnPage(itemsOnPage)
                .build();
    }
}
