package bntu.fitr.gorbachev.ticketsgenerator.main.util.loc;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.ReadableProperties;

public class Localizer {
    private static ReadableProperties localeProperties;

    static {
        try {
            LocalsConfiguration config = new LocalsConfiguration();
            localeProperties = config.getLocaleProperties();
        } catch (LocalizerException e) {
            throw new RuntimeException(e);
        }
    }


}
