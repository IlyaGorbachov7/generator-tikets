package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.ColumnViewUI;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.TableViewUI;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@TableViewUI(name = "lbl.model.head-department")
public class HeadDepartmentModelTbl {
    @ColumnViewUI(typeView = ColumnViewUI.STRING, name="tbl.model.field.id")
    private UUID id;

    @ColumnViewUI(typeView = ColumnViewUI.STRING, name="tbl.model.field.name")
    private String name;

    @EqualsAndHashCode.Exclude
    private UUID departmentId;

    @EqualsAndHashCode.Exclude
    @ColumnViewUI(typeView = ColumnViewUI.STRING, name="lbl.model.department.foreign")
    private String departmentName;
}
