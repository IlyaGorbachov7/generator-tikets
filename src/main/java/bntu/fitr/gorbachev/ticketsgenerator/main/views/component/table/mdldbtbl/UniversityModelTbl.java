package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.ColumnViewUI;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann.TableViewUI;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@TableViewUI(name = "Университет")
public class UniversityModelTbl {

    private UUID id;

    @ColumnViewUI
    private String name;
}
