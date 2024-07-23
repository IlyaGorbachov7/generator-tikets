package bntu.fitr.gorbachev.ticketsgenerator.main.basis;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WriterTicketProperty implements Serializable {
    private boolean isTicketOnSinglePage;
    private int quantityOnSinglePage;

    private int sizeFont;
    {
        quantityOnSinglePage = 1;
        sizeFont = 14;
    }

    public WriterTicketProperty() {
    }

    public WriterTicketProperty(boolean isTicketOnSinglePage, int quantityOnSinglePage) {
        this.isTicketOnSinglePage = isTicketOnSinglePage;
        if (quantityOnSinglePage < 1) {
            throw new IllegalArgumentException("quantityOnSinglePage < 1");
        }
        this.quantityOnSinglePage = quantityOnSinglePage;
    }
}
