package bntu.fitr.gorbachev.ticketsgenerator.main.models;

import lombok.Getter;
import lombok.Setter;

/**
 * This class contains generation property, which will be used
 * for checking condition start generation and during tickets generation
 *
 * @version 01.11.2022
 */
@Getter
@Setter
public abstract class GenerationProperty {
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
}
