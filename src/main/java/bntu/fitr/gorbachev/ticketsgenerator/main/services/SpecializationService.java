package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.other.PaginationParam;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationSimpleDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpecializationService {
    SpecializationDto create(SpecializationCreateDto specializationCreateDto);

    SpecializationSimpleDto createSmpl(SpecializationCreateDto specializationCreateDto);

    SpecializationDto update(SpecializationDto specializationDto);

    SpecializationSimpleDto update(SpecializationSimpleDto dto);

    void delete(SpecializationDto specializationDto);

    void deleteSmpl(SpecializationSimpleDto elem);

    void deleteSmpl(List<SpecializationSimpleDto> list);

    Optional<SpecializationDto> getAny();

    Optional<SpecializationSimpleDto> getSmplAny();

    List<SpecializationDto> getAll();

    Optional<SpecializationDto> getByName(String name);

    Optional<SpecializationSimpleDto> getSmplByName(String name);

    List<SpecializationDto> getByDepartmentId(UUID departmentId);

    List<SpecializationDto> getByDepartmentId(UUID departmentId, int page, int itemsOnPage);

    List<SpecializationSimpleDto> getSmplByDepartmentId(UUID departmentId, int page, int itemsOnPage);

    long countByDepartmentId(UUID departmentId);

    List<SpecializationDto> getByDepartmentName(String departmentName);

    long countByDepartmentName(String departmentName);

    List<SpecializationDto> getByLikeNameAndDepartmentId(String likeName, UUID departmentId);

    List<SpecializationDto> getByLikeNameAndDepartmentId(String likeName, UUID departmentId, int page, int itemsOnPage);

    List<SpecializationSimpleDto> getSmplByLikeNameAndDepartmentId(String likeName, UUID departmentId, int page, int itemsOnPage);

    long countByLikeNameAndDepartmentId(String likeName, UUID departmentId);

    List<SpecializationSimpleDto> getSmplByDepartmentId(UUID id);

    PaginationParam calculatePageParam(int itemsOnPage, int currentPage, String filterText, UUID departmentId);
}
