package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpecializationService {
    SpecializationDto create(SpecializationCreateDto specializationCreateDto);

    SpecializationDto update(SpecializationDto specializationDto);

    void delete(SpecializationDto specializationDto);

    Optional<SpecializationDto> getAny();

    List<SpecializationDto> getAll();

    Optional<SpecializationDto> getByName(String name);

    List<SpecializationDto> getByDepartmentId(UUID departmentId);

    long countByDepartmentId(UUID departmentId);

    List<SpecializationDto> getByDepartmentName(String departmentName);

    long countByDepartmentName(String departmentName);

    List<SpecializationDto> getByLikeNameAndDepartmentId(String likeName, UUID departmentId);

    long countByLikeNameAndDepartmentId(String likeName, UUID departmentId);
}
