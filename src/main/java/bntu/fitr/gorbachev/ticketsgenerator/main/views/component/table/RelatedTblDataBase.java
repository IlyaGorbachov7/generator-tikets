package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class RelatedTblDataBase {
    private Class<?> classMdlTbl;

    @Builder.Default
    private List<RelatedTblDataBase> child = new ArrayList<>();
}
