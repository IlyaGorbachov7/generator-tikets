package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DisciplineCreateDto {

    private String name;

    private UUID specializationId;
}
