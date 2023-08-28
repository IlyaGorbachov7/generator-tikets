package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import lombok.Data;

import java.util.UUID;

@Data
public class DepartmentDto {
    private UUID id;

    private String name;

    private FacultyDto facultyDto;
}
