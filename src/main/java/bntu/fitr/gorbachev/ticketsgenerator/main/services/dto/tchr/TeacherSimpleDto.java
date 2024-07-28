package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TeacherSimpleDto {
    private UUID id;

    private String name;

    private UUID facultyId;

    private String facultyName;
}
