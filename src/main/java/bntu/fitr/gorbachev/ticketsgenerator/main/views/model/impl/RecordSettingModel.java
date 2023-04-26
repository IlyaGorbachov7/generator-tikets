package bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.AbstractModel;
import lombok.Getter;

import static bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.perconst.RecordSettingFieldNameConst.FONT_SIZE;
import static bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.perconst.RecordSettingFieldNameConst.QUANTITY_TICKET_ON_SINGLEPAGE;

@Getter
public class RecordSettingModel extends AbstractModel {
    private Integer quantityTicketOnSinglePage = 1;
    private Integer fontSize = 14;

    public void setQuantityTicketOnSinglePage(Integer quantityTicketOnSinglePage) {
        Integer oldValue = this.quantityTicketOnSinglePage;
        this.quantityTicketOnSinglePage = quantityTicketOnSinglePage;
        fireChangeFiledEvent(QUANTITY_TICKET_ON_SINGLEPAGE, oldValue, quantityTicketOnSinglePage);
    }

    public void setFontSize(Integer fontSize) {
        Integer oldValue = this.fontSize;
        this.fontSize = fontSize;
        fireChangeFiledEvent(FONT_SIZE, oldValue, fontSize);
    }
}
