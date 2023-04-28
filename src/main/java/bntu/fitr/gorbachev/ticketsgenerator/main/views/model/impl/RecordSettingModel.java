package bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.AbstractModel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.exceptions.IllegalInputValueException;
import lombok.Getter;

import java.util.Map;

import static bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.perconst.RecordSettingFieldNameConst.FONT_SIZE;
import static bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.perconst.RecordSettingFieldNameConst.QUANTITY_TICKET_ON_SINGLEPAGE;

@Getter
public class RecordSettingModel extends AbstractModel {
    private final int minVQuantityTicketSinglePage = 1;
    private final int maxVQuantityTicketOnSinglePage = 5;

    private final int minVFontSize = 1;
    private final int maxVFontSize = 20;

    private Integer quantityTicketOnSinglePage = 1;
    private Integer fontSize = 14;

    @Override
    protected void checkConditionsValueField(String fieldName, Object value) {
        String msgWarning = "";
        switch (fieldName) {
            case QUANTITY_TICKET_ON_SINGLEPAGE -> {
                Integer newValue = (Integer) value;
                if (newValue < minVQuantityTicketSinglePage || newValue > maxVQuantityTicketOnSinglePage) {
                    msgWarning = String.format("newValue quantityTicketPageSize=%d outside required boundary: [%d, %d]",
                            newValue, minVQuantityTicketSinglePage, maxVQuantityTicketOnSinglePage);
                }
            }
            case FONT_SIZE -> {
                Integer newValue = (Integer) value;
                if (newValue < minVFontSize || newValue > maxVFontSize) {
                    msgWarning = String.format("newValue fonSize=%d outside required boundary: [%d, %d]",
                            newValue, minVFontSize, maxVFontSize);
                }
                if (!msgWarning.isEmpty()) {
                    throw new IllegalInputValueException(msgWarning);
                }
            }

        }
    }

    public void setQuantityTicketOnSinglePage(Integer quantityTicketOnSinglePage) {
        checkConditionsValueField(QUANTITY_TICKET_ON_SINGLEPAGE, quantityTicketOnSinglePage);
        Integer oldValue = this.quantityTicketOnSinglePage;
        this.quantityTicketOnSinglePage = quantityTicketOnSinglePage;
        fireChangeFiledEvent(QUANTITY_TICKET_ON_SINGLEPAGE, oldValue, quantityTicketOnSinglePage);
    }

    public void setFontSize(Integer fontSize) {
        checkConditionsValueField(FONT_SIZE, quantityTicketOnSinglePage);
        Integer oldValue = this.fontSize;
        this.fontSize = fontSize;
        fireChangeFiledEvent(FONT_SIZE, oldValue, fontSize);
    }

    @Override
    public void triggeringInitView() {
        fireInitViewEvent(Map.of(
                QUANTITY_TICKET_ON_SINGLEPAGE, quantityTicketOnSinglePage,
                FONT_SIZE, fontSize
        ));
    }
}
