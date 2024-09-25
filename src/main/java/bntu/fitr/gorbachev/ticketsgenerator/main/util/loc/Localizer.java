package bntu.fitr.gorbachev.ticketsgenerator.main.util.loc;

import java.io.IOException;

public class Localizer {
    static {
        try {
            LocalsConfiguration config = new LocalsConfiguration();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
