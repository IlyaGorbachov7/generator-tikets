package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.Objects;

@Getter
@Setter
@Builder
public final class KeyForViewUI {

    private Class<?> clazzModelTbl;

    private JPanel pnlTbl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyForViewUI that = (KeyForViewUI) o;
        return clazzModelTbl.equals(that.clazzModelTbl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazzModelTbl);
    }
}
