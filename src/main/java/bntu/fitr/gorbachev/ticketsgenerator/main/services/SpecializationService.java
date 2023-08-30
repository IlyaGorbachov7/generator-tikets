package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;

import java.util.List;
import java.util.Optional;

public interface SpecializationService {
    SpecializationDto create(SpecializationCreateDto specializationCreateDto);

    SpecializationDto update(SpecializationDto specializationDto);

    void delete(SpecializationDto specializationDto);

    Optional<SpecializationDto> getAny();

    List<SpecializationDto> getAll();
}
