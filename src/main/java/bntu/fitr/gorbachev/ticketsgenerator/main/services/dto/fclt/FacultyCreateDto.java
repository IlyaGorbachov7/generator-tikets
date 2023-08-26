package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt;

import lombok.Data;

import java.util.UUID;

@Data
public class FacultyCreateDto {

    private String name;

    private UUID universityId;
}
