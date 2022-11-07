package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

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

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }
}
