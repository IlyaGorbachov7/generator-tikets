package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.Objects;

@Getter
@Builder
public final class KeyForViewUI {

    @Setter(value = AccessLevel.PRIVATE)
    private int index;

    private JButton btn;

    private JTableDataBase tbl;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyForViewUI that = (KeyForViewUI) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
