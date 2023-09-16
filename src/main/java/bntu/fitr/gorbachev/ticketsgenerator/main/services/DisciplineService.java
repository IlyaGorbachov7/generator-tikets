package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DisciplineService {

    DisciplineDto create(DisciplineCreateDto disciplineCreateDto) throws ServiceException;

    DisciplineDto update(DisciplineDto disciplineDto) throws ServiceException;

    void delete(DisciplineDto disciplineDto) throws ServiceException;

    Optional<DisciplineDto> getAny() throws ServiceException;

    List<DisciplineDto> getAll() throws ServiceException;

    Optional<DisciplineDto> getByName(String name) throws ServiceException;

    List<DisciplineDto> getBySpecializationId(UUID specializationId) throws ServiceException;

    List<DisciplineDto> getBySpecializationName(String specializationName) throws ServiceException;

    List<DisciplineDto> getByLikeNameAndSpecializationId(String likeName, UUID specializationId) throws ServiceException;

    int getCountByName(String name) throws ServiceException;

    int getCountByLikeNameAndSpecializationId(String likeName, UUID specializationId) throws ServiceException;
}