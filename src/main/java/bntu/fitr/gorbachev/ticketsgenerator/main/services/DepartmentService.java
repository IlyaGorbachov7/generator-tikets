package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    DepartmentDto create(DepartmentCreateDto departmentCreateDto) throws ServiceException;

    DepartmentDto update(DepartmentDto departmentDto) throws ServiceException;

    void delete(DepartmentDto facultyDto) throws ServiceException;

    Optional<DepartmentDto> getAny() throws ServiceException;

    List<DepartmentDto> getAll() throws ServiceException;
}
