package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.mdldbtbl;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SpecializationModelTbl {
    private UUID id;

    private String name;

    private UUID departmentId;

    private String departmentName;
}
