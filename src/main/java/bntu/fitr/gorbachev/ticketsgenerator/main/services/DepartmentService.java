package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.other.PaginationParam;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentService {

    DepartmentDto create(DepartmentCreateDto departmentCreateDto) throws ServiceException;

    DepartmentSimpleDto createSmpl(DepartmentCreateDto departmentCreateDto) throws ServiceException;

    DepartmentDto update(DepartmentDto departmentDto) throws ServiceException;

    DepartmentSimpleDto updateSmpl(DepartmentSimpleDto dto) throws ServiceException;

    void delete(DepartmentDto facultyDto) throws ServiceException;

    void deleteSmpl(DepartmentSimpleDto facultyDto) throws ServiceException;

    void deleteSmpl(List<DepartmentSimpleDto> list) throws ServiceException;

    Optional<DepartmentDto> getByName(String name) throws ServiceException;

    Optional<DepartmentSimpleDto> getSmplDtoByName(String name) throws ServiceException;

    Optional<DepartmentDto> getAny() throws ServiceException;

    Optional<DepartmentSimpleDto> getSmplAny() throws ServiceException;

    List<DepartmentDto> getAll() throws ServiceException;

    List<DepartmentDto> getByFacultyId(UUID facultyId) throws ServiceException;

    List<DepartmentDto> getByFacultyId(UUID facultyId, int page, int itemsOnPage) throws ServiceException;

    List<DepartmentSimpleDto> getSmplByFacultyId(UUID facultyId, int page, int itemsOnPage) throws ServiceException;

    long countByFacultyId(UUID facultyId) throws ServiceException;

    List<DepartmentDto> getByFacultyName(String facultyName) throws ServiceException;

    long countByFacultyName(String facultyName) throws ServiceException;

    List<DepartmentDto> getByLikeNameAndFacultyId(String likeName, UUID facultyId) throws ServiceException;

    List<DepartmentDto> getByLikeNameAndFacultyId(String likeName, UUID facultyId, int page, int itemsOnPage) throws ServiceException;

    List<DepartmentSimpleDto> getSmplByLikeNameAndFacultyId(String likeName, UUID facultyId, int page, int itemsOnPage) throws ServiceException;

    long countByLikeNameAndFacultyId(String likeName, UUID facultyId) throws ServiceException;

    List<DepartmentSimpleDto> getSmplByFacultyId(UUID id);

    PaginationParam calculatePageParam(int itemsOnPage, int currentPage, String filterText, UUID facultyId);
}
