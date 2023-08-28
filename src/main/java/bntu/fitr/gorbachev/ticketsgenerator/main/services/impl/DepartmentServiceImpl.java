package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DepartmentDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.DepartmentService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.DepartmentMapper;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl.MapperFactoryImpl;

import java.util.List;
import java.util.Optional;

public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentDAO departmentRepo = RepositoryFactoryImpl.getInstance().repositoryDepartment();
    private final DepartmentMapper departmentMapper = MapperFactoryImpl.getInstance().departmentMapper();
    private final HQueryMaster<Department> executor = departmentRepo.getExecutor();

    @Override
    public DepartmentDto create(DepartmentCreateDto departmentCreateDto) throws ServiceException {
        Department entity = departmentMapper.departmentDtoToDepartment(departmentCreateDto);
        return null;
    }

    @Override
    public DepartmentDto update(DepartmentDto departmentDto) throws ServiceException {
        return null;
    }

    @Override
    public void delete(DepartmentDto facultyDto) throws ServiceException {

    }

    @Override
    public Optional<DepartmentDto> getAny() throws ServiceException {
        return Optional.empty();
    }

    @Override
    public List<DepartmentDto> getAll() throws ServiceException {
        return null;
    }
}
