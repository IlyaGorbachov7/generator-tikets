package bntu.fitr.gorbachev.ticketsgenerator.main.util.loc;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Locale;

@Data
class LocData implements Serializable {

    @Serial
    private final static long serialVersionUID = 2342534654754643543L;

    private Locale selectedLocale;
}
