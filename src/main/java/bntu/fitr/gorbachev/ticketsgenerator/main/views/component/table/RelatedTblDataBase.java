package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
@Getter
@Builder
public class RelatedTblDataBase {
    private Class<?> classMdlTbl;

    @Builder.Default
    private List<RelatedTblDataBase> child = new ArrayList<>(2);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelatedTblDataBase that = (RelatedTblDataBase) o;
        return classMdlTbl.equals(that.classMdlTbl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classMdlTbl);
    }
}
