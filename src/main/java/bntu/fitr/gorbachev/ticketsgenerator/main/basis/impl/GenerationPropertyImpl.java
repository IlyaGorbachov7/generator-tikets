package bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.WriterTicketProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.generatway.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.generatway.impl.TicketsGeneratorWayImpl1;
import lombok.Getter;
import lombok.Setter;

/**
 * Class Implementation.
 * <p>
 * Contains property: <b>rating</b> - define two value
 */
@Getter
@Setter
public class GenerationPropertyImpl extends GenerationProperty {
    /* using
    TicketGeneratorWayImpl1
    TicketGeneratorWayImpl2
    * */
    private Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generationWay;
    private boolean flagContinGenWithDepriveLev;
    private boolean flagRandomOrderReading;
    private boolean flagRandomOrderQuestInTicket;
    /* using TicketGeneratorWayImpl1 */
    private boolean flagContinGenWithChapterWithoutSection;
    private WriterTicketProperty writerTicketProperty;

    {
        generationWay = TicketsGeneratorWayImpl1.class;
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets, Boolean unique) {
        super(quantityTickets, quantityQTickets, unique);
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets, Boolean unique,
                                  Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generationWay) {
        super(quantityTickets, quantityQTickets, unique);
        this.generationWay = generationWay;
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets, Boolean unique,
                                  Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generationWay,
                                  boolean flagRandomizeOrderReading) {
        super(quantityTickets, quantityQTickets, unique);
        this.generationWay = generationWay;
        this.flagRandomOrderReading = flagRandomizeOrderReading;
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets, Boolean unique,
                                  Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generationWay,
                                  boolean flagRandomizeOrderReading, boolean flatRandomOrderQuestInTicket) {
        super(quantityTickets, quantityQTickets, unique);
        this.flagRandomOrderReading = flagRandomizeOrderReading;
        this.flagRandomOrderQuestInTicket = flatRandomOrderQuestInTicket;
        this.generationWay = generationWay;
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets,
                                  Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generationWay) {
        super(quantityTickets, quantityQTickets);
        this.generationWay = generationWay;
    }
}
