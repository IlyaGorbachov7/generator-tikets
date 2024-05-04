package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentService {

    DepartmentDto create(DepartmentCreateDto departmentCreateDto) throws ServiceException;

    DepartmentDto update(DepartmentDto departmentDto) throws ServiceException;

    void delete(DepartmentDto facultyDto) throws ServiceException;

    Optional<DepartmentDto> getByName(String name) throws ServiceException;

    Optional<DepartmentSimpleDto> getSmplDtoByName(String name) throws ServiceException;

    Optional<DepartmentDto> getAny() throws ServiceException;

    Optional<DepartmentSimpleDto> getSmplAny() throws ServiceException;

    List<DepartmentDto> getAll() throws ServiceException;

    List<DepartmentDto> getByFacultyId(UUID facultyId) throws ServiceException;

    long countByFacultyId(UUID facultyId) throws ServiceException;

    List<DepartmentDto> getByFacultyName(String facultyName) throws ServiceException;

    long countByFacultyName(String facultyName) throws ServiceException;

    List<DepartmentDto> getByLikeNameAndFacultyId(String likeName, UUID facultyId) throws ServiceException;

    long countByLikeNameAndFacultyId(String likeName, UUID facultyId) throws ServiceException;
}
