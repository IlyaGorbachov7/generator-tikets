package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.KeyForViewUI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventChoiceBtn {
    private KeyForViewUI current;

    private KeyForViewUI previous;
}
