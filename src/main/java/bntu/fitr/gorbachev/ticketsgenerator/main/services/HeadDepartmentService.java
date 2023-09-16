package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HeadDepartmentService {
    HeadDepartmentDto create(HeadDepartmentCreateDto headDepartmentCreateDto) throws ServiceException;

    HeadDepartmentDto update(HeadDepartmentDto headDepartmentDto) throws ServiceException;

    void delete(HeadDepartmentDto headDepartmentDto) throws ServiceException;

    Optional<HeadDepartmentDto> getAny() throws ServiceException;

    List<HeadDepartmentDto> getAll() throws ServiceException;

    Optional<HeadDepartmentDto> getByName(String name) throws ServiceException;

    List<HeadDepartmentDto> getByDepartmentId(UUID DepartmentId) throws ServiceException;

    List<HeadDepartmentDto> getByDepartmentName(String departmentName) throws ServiceException;

    List<HeadDepartmentDto> getByLikeNameAndDepartmentId(String likeName, UUID departmentId) throws ServiceException;

    int getCountByName() throws ServiceException;

    int getCountByLikeNameAndDepartmentId(String likeName, UUID departmentId) throws ServiceException;
}
