package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransmissionObject {
    private Class<?> clazzMdlTbl;

    private Object[] dataValue;

}
