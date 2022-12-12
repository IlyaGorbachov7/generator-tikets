package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WriterTicketProperty {
    private boolean isTicketOnSinglePage;
    private int quantityOnSinglePage;

    {
        quantityOnSinglePage = 1;
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
