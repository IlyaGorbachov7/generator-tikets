package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay;

/**
 * This class contains generation property, which will be used
 * for checking condition start generation and during tickets generation
 *
 * @version 01.11.2022
 */
public class GenerationProperty {
    int quantityTickets;
    int quantityQTickets;

    private boolean unique;
    private Class<? extends TicketsGeneratorWay<? extends QuestionExt, ? extends Ticket<?>>> generationWay;

    public GenerationProperty() {
    }

    public GenerationProperty(int quantityTickets, int quantityQTickets, Boolean unique) {
        this.quantityTickets = quantityTickets;
        this.quantityQTickets = quantityQTickets;
        this.unique = unique;
    }

    public GenerationProperty(int quantityTickets, int quantityQTickets) {
        this.quantityTickets = quantityTickets;
        this.quantityQTickets = quantityQTickets;
    }

    public <Q extends QuestionExt, E extends Ticket<? super Q>> GenerationProperty(int quantityTickets, int quantityQTickets,
                                                                                   Class<? extends TicketsGeneratorWay<Q, E>> generationWay) {
        this.quantityTickets = quantityTickets;
        this.quantityQTickets = quantityQTickets;
        this.generationWay = generationWay;
    }

    public int getQuantityTickets() {
        return quantityTickets;
    }

    public void setQuantityTickets(int quantityTickets) {
        this.quantityTickets = quantityTickets;
    }

    public int getQuantityQTickets() {
        return quantityQTickets;
    }

    public void setQuantityQTickets(int quantityQTickets) {
        this.quantityQTickets = quantityQTickets;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public <Q extends QuestionExt, E extends Ticket<? super Q>> Class<? extends TicketsGeneratorWay<Q, E>> getGenerationWay() {
        return (Class<? extends TicketsGeneratorWay<Q, E>>) generationWay;
    }

    public void setGenerationWay(Class<TicketsGeneratorWay<?, ?>> generationWay) {
        this.generationWay = generationWay;
    }
}
