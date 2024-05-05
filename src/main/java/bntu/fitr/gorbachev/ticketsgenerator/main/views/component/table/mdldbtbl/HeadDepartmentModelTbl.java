package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.ColumnViewUI;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.TableViewUI;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@TableViewUI(name = "Заведующий кафедрой")
public class HeadDepartmentModelTbl {
    private UUID id;

    @ColumnViewUI
    private String name;

    private UUID departmentId;

    @ColumnViewUI
    private String departmentName;
}
