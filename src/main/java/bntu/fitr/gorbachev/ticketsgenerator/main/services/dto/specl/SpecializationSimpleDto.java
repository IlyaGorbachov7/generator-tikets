package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SpecializationSimpleDto {
    private UUID id;

    private String name;

    private UUID departmentId;

    private String departmentName;
}
