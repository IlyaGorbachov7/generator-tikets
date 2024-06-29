package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.handers;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.KeyForViewUI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EventChoiceBtn {
    private KeyForViewUI current;

    private KeyForViewUI previous;

    private KeyForViewUI relatedFromCurrent;
}
