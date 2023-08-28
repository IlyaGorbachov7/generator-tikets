package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm;

import lombok.Data;

import java.util.UUID;

@Data
public class DepartmentCreateDto {

    private String name;

    private UUID facultyId;
}
