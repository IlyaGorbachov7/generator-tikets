package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import lombok.Data;

import java.util.UUID;

@Data
public class FacultyDto {
    private UUID id;

    private String name;

    private UniversityDTO universityDto;
}
