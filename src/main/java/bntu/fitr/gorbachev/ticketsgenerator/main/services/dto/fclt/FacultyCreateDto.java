package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FacultyCreateDto {

    private String name;

    private UUID universityId;
}
