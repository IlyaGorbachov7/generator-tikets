package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DisciplineDto {

    private UUID id;

    private String name;

    private SpecializationDto specializationDto;
}
