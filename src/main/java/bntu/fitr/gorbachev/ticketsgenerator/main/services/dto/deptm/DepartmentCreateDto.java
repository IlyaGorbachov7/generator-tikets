package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DepartmentCreateDto {

    private String name;

    private UUID facultyId;
}
