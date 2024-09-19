package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.ColumnViewUI;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.TableViewUI;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@TableViewUI(name = "Факультет")
public class FacultyModelTbl {
    @ColumnViewUI(typeView = ColumnViewUI.STRING)
    private UUID id;

    @ColumnViewUI(typeView = ColumnViewUI.STRING)
    private String name;

    @EqualsAndHashCode.Exclude
    private UUID universityId;

    @EqualsAndHashCode.Exclude
    @ColumnViewUI(typeView = ColumnViewUI.STRING)
    private String universityName;
}
