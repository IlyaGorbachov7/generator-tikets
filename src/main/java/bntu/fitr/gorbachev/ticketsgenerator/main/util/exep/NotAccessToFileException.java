package bntu.fitr.gorbachev.ticketsgenerator.main.util.exep;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Path;

public class NotAccessToFileException extends IOException {
    @Getter
    private Path filePath;

    public NotAccessToFileException(Path filePath) {
        this.filePath = filePath;
    }

    public NotAccessToFileException(String message, Path filePath) {
        super(message);
        this.filePath = filePath;
    }

    public NotAccessToFileException(String message, Throwable cause, Path filePath) {
        super(message, cause);
        this.filePath = filePath;
    }

    public NotAccessToFileException(Throwable cause, Path filePath) {
        super(cause);
        this.filePath = filePath;
    }
}
