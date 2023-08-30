package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SpecializationDto {
    private UUID id;

    private String name;

    private DepartmentDto departmentDto;
}
