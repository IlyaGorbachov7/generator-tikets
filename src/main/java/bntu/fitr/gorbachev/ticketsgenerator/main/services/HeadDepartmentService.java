package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface HeadDepartmentService {
    HeadDepartmentDto create(HeadDepartmentCreateDto headDepartmentCreateDto) throws ServiceException;

    HeadDepartmentDto update(HeadDepartmentDto headDepartmentDto) throws ServiceException;

    void delete(HeadDepartmentDto headDepartmentDto) throws ServiceException;

    Optional<HeadDepartmentDto> getAny() throws ServiceException;

    List<HeadDepartmentDto> getAll() throws ServiceException;
}
