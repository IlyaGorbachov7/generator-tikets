package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.HeadDepartmentDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.HeadDepartment;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.HeadDepartmentService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.other.PaginationParam;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.headdep.HeadDepartmentNoFoundByIdException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.HeadDepartmentMapper;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl.MapperFactoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HeadDepartmentServiceImpl implements HeadDepartmentService {

    private final HeadDepartmentDAO headDepartmentRepo = RepositoryFactoryImpl.getInstance().repositoryHeadDepartment();

    private final HeadDepartmentMapper headDepartmentMapper = MapperFactoryImpl.getInstance().headDepartmentMapper();

    private final HQueryMaster<HeadDepartment> executor = headDepartmentRepo.getExecutor();

    @Override
    public HeadDepartmentDto create(HeadDepartmentCreateDto headDepartmentCreateDto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            HeadDepartment entity = headDepartmentMapper.headDepartmentDtoToEntity(headDepartmentCreateDto);
            headDepartmentRepo.create(entity);
            return headDepartmentMapper.headDepartmentToDto(entity);
        });
    }

    @Override
    public HeadDepartmentSimpleDto createSmpl(HeadDepartmentCreateDto headDepartmentCreateDto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            HeadDepartment entity = headDepartmentMapper.headDepartmentDtoToEntity(headDepartmentCreateDto);
            headDepartmentRepo.create(entity);
            return headDepartmentMapper.headDepartmentToSimpleDto(entity);
        });
    }

    @Override
    public HeadDepartmentDto update(HeadDepartmentDto headDepartmentDto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            HeadDepartment target = headDepartmentRepo.findById(headDepartmentDto.getId())
                    .orElseThrow(HeadDepartmentNoFoundByIdException::new);
            headDepartmentMapper.update(target, headDepartmentDto);
            headDepartmentRepo.update(target);
            return headDepartmentMapper.headDepartmentToDto(target);
        });
    }

    @Override
    public HeadDepartmentSimpleDto update(HeadDepartmentSimpleDto dto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            HeadDepartment entity = headDepartmentRepo.findById(dto.getId())
                    .orElseThrow(HeadDepartmentNoFoundByIdException::new);
            headDepartmentMapper.update(entity, dto);
            headDepartmentRepo.update(entity);
            return headDepartmentMapper.headDepartmentToSimpleDto(entity);
        });
    }

    @Override
    public void delete(HeadDepartmentDto headDepartmentDto) throws ServiceException {
        executor.wrapTransactional(() -> {
            HeadDepartment entity = headDepartmentRepo.findById(headDepartmentDto.getId())
                    .orElseThrow(HeadDepartmentNoFoundByIdException::new);
            headDepartmentRepo.delete(entity);
        });
    }

    @Override
    public void deleteSmpl(HeadDepartmentSimpleDto dto) throws ServiceException {
        executor.wrapTransactional(() -> headDepartmentRepo.delete(
                headDepartmentRepo.findById(dto.getId()).orElseThrow(HeadDepartmentNoFoundByIdException::new)));
    }

    @Override
    public void deleteSmpl(List<HeadDepartmentSimpleDto> list) throws ServiceException {
        executor.wrapTransactional(() -> list.forEach(this::deleteSmpl));
    }

    @Override
    public Optional<HeadDepartmentDto> getAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                headDepartmentRepo.findAny().map(headDepartmentMapper::headDepartmentToDto));
    }

    @Override
    public Optional<HeadDepartmentSimpleDto> getSmplAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                headDepartmentRepo.findAny().map(headDepartmentMapper::headDepartmentToSimpleDto));
    }

    @Override
    public List<HeadDepartmentDto> getAll() throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                headDepartmentRepo.findAll().stream().map(headDepartmentMapper::headDepartmentToDto).toList());
    }

    @Override
    public Optional<HeadDepartmentDto> getByName(String name) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                headDepartmentRepo.findByName(name).map(headDepartmentMapper::headDepartmentToDto));
    }

    @Override
    public Optional<HeadDepartmentSimpleDto> getSmplByName(String name) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                headDepartmentRepo.findByName(name).map(headDepartmentMapper::headDepartmentToSimpleDto));
    }

    @Override
    public List<HeadDepartmentDto> getByDepartmentId(UUID departmentId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                headDepartmentRepo.findByDepartmentId(
                        departmentId).stream().map(
                        headDepartmentMapper::headDepartmentToDto).toList());
    }

    @Override
    public List<HeadDepartmentDto> getByDepartmentId(UUID departmentId, int page, int itemsOnPage) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                headDepartmentMapper.headDepartmentToDto(
                        headDepartmentRepo.findByDepartmentId(departmentId, page, itemsOnPage)));
    }

    @Override
    public List<HeadDepartmentSimpleDto> getSmplByDepartmentId(UUID departmentId, int page, int itemsOnPage) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                headDepartmentMapper.headDepartmentToSimpleDto(
                        headDepartmentRepo.findByDepartmentId(departmentId, page, itemsOnPage)));
    }

    @Override
    public long countByDepartmentId(UUID DepartmentId) throws ServiceException {
        return headDepartmentRepo.countByDepartmentId(DepartmentId);
    }

    @Override
    public List<HeadDepartmentDto> getByDepartmentName(String departmentName) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                headDepartmentRepo.findByDepartmentName(
                        departmentName).stream().map(
                        headDepartmentMapper::headDepartmentToDto).toList());
    }

    @Override
    public long countByDepartmentName(String departmentName) throws ServiceException {
        return headDepartmentRepo.countByDepartmentName(departmentName);
    }

    @Override
    public List<HeadDepartmentDto> getByLikeNameAndDepartmentId(String likeName, UUID departmentId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                headDepartmentRepo.findByLikeNameAndDepartmentId(
                        likeName, departmentId).stream().map(
                        headDepartmentMapper::headDepartmentToDto).toList());
    }

    @Override
    public List<HeadDepartmentDto> getByLikeNameAndDepartmentId(String likeName, UUID departmentId, int page, int itemsOnPage) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                headDepartmentMapper.headDepartmentToDto(
                        headDepartmentRepo.findByLikeNameAndDepartmentId(likeName, departmentId, page, itemsOnPage)));
    }

    @Override
    public List<HeadDepartmentSimpleDto> getSmplByLikeNameAndDepartmentId(String likeName, UUID departmentId, int page, int itemsOnPage) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                headDepartmentMapper.headDepartmentToSimpleDto(
                        headDepartmentRepo.findByLikeNameAndDepartmentId(likeName, departmentId, page, itemsOnPage)));
    }

    @Override
    public long countByLikeNameAndDepartmentId(String likeName, UUID departmentId) throws ServiceException {
        return headDepartmentRepo.countByLikeNameAndDepartmentId(likeName, departmentId);
    }

    @Override
    public List<HeadDepartmentSimpleDto> getSmplByDepartmentId(UUID id) {
        return executor.wrapTransactionalResultList(() ->
                headDepartmentRepo.findByDepartmentId(
                        id).stream().map(
                        headDepartmentMapper::headDepartmentToSimpleDto).toList());

    }

    @Override
    public PaginationParam calculatePageParam(int itemsOnPage, int currentPage, String filterText, UUID departmentId) {
        long totalItems = filterText.isBlank() ? headDepartmentRepo.countByDepartmentId(departmentId) :
                headDepartmentRepo.countByLikeNameAndDepartmentId(filterText, departmentId);
        int totalPage = (int) (((totalItems % itemsOnPage) == 0.0) ? (totalItems / itemsOnPage) : (totalItems / itemsOnPage) + 1);
        return PaginationParam.builder()
                .currentPage((currentPage > totalPage) ? 1 : currentPage)
                .totalPage(totalPage)
                .itemsOnPage(itemsOnPage)
                .build();
    }
}
