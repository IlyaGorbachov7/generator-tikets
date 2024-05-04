package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FacultyDto {
    private UUID id;

    private String name;

    private UniversityDTO universityDto;
}
