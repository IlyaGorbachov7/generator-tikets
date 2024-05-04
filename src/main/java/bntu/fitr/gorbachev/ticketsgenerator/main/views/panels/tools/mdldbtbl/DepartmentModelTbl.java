package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.mdldbtbl;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DepartmentModelTbl {
    private UUID id;

    private String name;

    private String facultyName;

    private UUID facultyId;
}
