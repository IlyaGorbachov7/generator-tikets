package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

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

    public boolean isTicketOnSinglePage() {
        return isTicketOnSinglePage;
    }

    public void setTicketOnSinglePage(boolean ticketOnSinglePage) {
        isTicketOnSinglePage = ticketOnSinglePage;
    }

    public int getQuantityOnSinglePage() {
        return quantityOnSinglePage;
    }

    public void setQuantityOnSinglePage(int quantityOnSinglePage) {
        this.quantityOnSinglePage = quantityOnSinglePage;
    }
}
