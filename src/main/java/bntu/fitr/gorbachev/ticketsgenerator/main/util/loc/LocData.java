package bntu.fitr.gorbachev.ticketsgenerator.main.util.loc;

import lombok.Data;

import java.io.Serializable;
import java.util.Locale;

@Data
class LocData implements Serializable {
    private Locale selectedLocale;
}
