package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DepartmentDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.DepartmentService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.deptm.DepartmentNoFoundByIdException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.DepartmentMapper;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl.MapperFactoryImpl;

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
    public DepartmentDto update(DepartmentDto departmentDto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            Department entity = departmentRepo.findById(departmentDto.getId()).orElseThrow(DepartmentNoFoundByIdException::new);
            departmentMapper.update(entity, departmentDto);
            return departmentMapper.departmentToDepartmentDto(entity);
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
    public Optional<DepartmentDto> getAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                departmentRepo.findAny().map(departmentMapper::departmentToDepartmentDto)
        );
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
    public List<DepartmentDto> getByFacultyName(String facultyName) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                departmentMapper.departmentToDepartmentDto(
                        departmentRepo.findByFacultyName(facultyName)));
    }

    @Override
    public List<DepartmentDto> getByLikeNameAndFacultyId(String likeName, UUID facultyId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                departmentMapper.departmentToDepartmentDto(
                        departmentRepo.findByLikeNameAndFacultyId(likeName, facultyId)));
    }
}
