package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.ColumnViewUI;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.TableViewUI;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@TableViewUI(name = "Дисциплина")
public class DisciplineModelTbl {
    private UUID id;

    @ColumnViewUI
    private String name;

    private UUID specializationId;

    @ColumnViewUI
    private String specializationName;
}
