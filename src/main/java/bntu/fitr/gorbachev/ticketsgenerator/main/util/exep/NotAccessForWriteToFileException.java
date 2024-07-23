package bntu.fitr.gorbachev.ticketsgenerator.main.util.exep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class NotAccessForWriteToFileException extends IOException {
    public NotAccessForWriteToFileException(Path file) {
        super(String.format("Don't access to write data into the %s", file));
    }
}
