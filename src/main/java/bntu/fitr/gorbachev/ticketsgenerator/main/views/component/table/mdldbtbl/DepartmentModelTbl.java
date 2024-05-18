package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.ColumnViewUI;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.TableViewUI;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@TableViewUI(name = "Кафедра")
public class DepartmentModelTbl {
    @ColumnViewUI(typeView = ColumnViewUI.STRING)
    private UUID id;

    @ColumnViewUI(typeView = ColumnViewUI.STRING)
    private String name;

    private UUID facultyId;

    @ColumnViewUI(typeView = ColumnViewUI.STRING)
    private String facultyName;

}
