package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay;

/**
 * Class Implementation.
 * <p>
 * Contains property: <b>rating</b> - define two value
 */
public class GenerationPropertyImpl extends GenerationProperty {
    private GenerationMode generationMode;
    private Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generationWay;

    {
        generationMode = GenerationMode.MODE_1;
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets, Boolean unique) {
        super(quantityTickets, quantityQTickets, unique);
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets, Boolean unique,
                                  GenerationMode generationMode) {
        super(quantityTickets, quantityQTickets, unique);
        this.generationMode = generationMode;
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets,
                                  GenerationMode generationMode) {
        super(quantityTickets, quantityQTickets);
        this.generationMode = generationMode;
    }

    public GenerationMode getGenerationMode() {
        return generationMode;
    }

    public void setGenerationMode(GenerationMode generationMode) {
        this.generationMode = generationMode;
    }

    public Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> getGenerationWay() {
        return generationWay;
    }

    public void setGenerationWay(Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generationWay) {
        this.generationWay = generationWay;
    }
}
