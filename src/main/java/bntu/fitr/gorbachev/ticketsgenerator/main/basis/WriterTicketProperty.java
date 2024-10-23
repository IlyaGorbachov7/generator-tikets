package bntu.fitr.gorbachev.ticketsgenerator.main.basis;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class WriterTicketProperty implements Serializable {
    @Serial
    private final static long serialVersionUID = 34646677666L;

    private boolean isTicketOnSinglePage;
    private int quantityOnSinglePage;

    private int sizeFont;

    private boolean includeUniversity;
    private boolean includeFaculty;
    private boolean includeDepartment;
    private boolean includeSpecialization;
    private boolean includeDiscipline;

    {
        quantityOnSinglePage = 1;
        sizeFont = 14;
        includeUniversity = includeDepartment = includeFaculty = includeSpecialization = includeDiscipline = true;
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
