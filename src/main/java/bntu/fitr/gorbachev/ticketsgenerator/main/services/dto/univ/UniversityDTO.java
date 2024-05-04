package bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Entity;
import lombok.*;

import java.util.UUID;

@ToString
@Builder
@Getter
@Setter
public class UniversityDTO extends Entity {

    private UUID id;

    private String name;

}
