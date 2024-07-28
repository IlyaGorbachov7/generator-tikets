package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineSimpledDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.other.PaginationParam;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DisciplineService {

    DisciplineDto create(DisciplineCreateDto disciplineCreateDto) throws ServiceException;

    DisciplineSimpledDto createSmpl(DisciplineCreateDto disciplineCreateDto) throws ServiceException;

    DisciplineDto update(DisciplineDto disciplineDto) throws ServiceException;

    DisciplineSimpledDto update(DisciplineSimpledDto dto) throws ServiceException;

    void delete(DisciplineDto disciplineDto) throws ServiceException;

    void deleteSmpl(DisciplineSimpledDto dto) throws ServiceException;

    void deleteSmpl(List<DisciplineSimpledDto> dto) throws ServiceException;

    Optional<DisciplineDto> getAny() throws ServiceException;

    Optional<DisciplineSimpledDto> getSmplAny() throws ServiceException;

    List<DisciplineDto> getAll() throws ServiceException;

    Optional<DisciplineDto> getByName(String name) throws ServiceException;

    Optional<DisciplineSimpledDto> getSmplByName(String name) throws ServiceException;

    List<DisciplineDto> getBySpecializationId(UUID specializationId) throws ServiceException;

    List<DisciplineDto> getBySpecializationId(UUID specializationId, int page, int itemsOnPage) throws ServiceException;

    List<DisciplineSimpledDto> getSmplBySpecializationId(UUID specializationId, int page, int itemsOnPage) throws ServiceException;

    long countBySpecializationId(UUID specializationId) throws ServiceException;

    List<DisciplineDto> getBySpecializationName(String specializationName) throws ServiceException;

    long countBySpecializationName(String specializationName) throws ServiceException;

    List<DisciplineDto> getByLikeNameAndSpecializationId(String likeName, UUID specializationId) throws ServiceException;

    List<DisciplineDto> getByLikeNameAndSpecializationId(String likeName, UUID specializationId, int page, int itemsOnPage) throws ServiceException;

    List<DisciplineSimpledDto> getSmplByLikeNameAndSpecializationId(String likeName, UUID specializationId, int page, int itemsOnPage) throws ServiceException;

    long countByLikeNameAndSpecializationId(String likeName, UUID specializationId) throws ServiceException;

    List<DisciplineSimpledDto> getSmplBySpecializationId(UUID id);

    PaginationParam calculatePageParam(int itemsOnPage, int currentPage, String filterText, UUID specializationId);
}