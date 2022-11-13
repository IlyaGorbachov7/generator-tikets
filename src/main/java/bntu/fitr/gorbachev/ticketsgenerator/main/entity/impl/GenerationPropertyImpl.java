package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl.TicketsGeneratorWayImpl1;

/**
 * Class Implementation.
 * <p>
 * Contains property: <b>rating</b> - define two value
 */
public class GenerationPropertyImpl extends GenerationProperty {
    private Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generationWay;

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

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets,
                                  Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generationWay) {
        super(quantityTickets, quantityQTickets);
        this.generationWay = generationWay;
    }

    public Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> getGenerationWay() {
        return generationWay;
    }

    public void setGenerationWay(Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generationWay) {
        this.generationWay = generationWay;
    }
}
