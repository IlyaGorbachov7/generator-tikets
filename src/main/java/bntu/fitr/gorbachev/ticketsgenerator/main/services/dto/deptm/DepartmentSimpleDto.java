package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DepartmentSimpleDto {
    private UUID id;

    private String name;

    private String facultyName;

    private UUID facultyId;
}
