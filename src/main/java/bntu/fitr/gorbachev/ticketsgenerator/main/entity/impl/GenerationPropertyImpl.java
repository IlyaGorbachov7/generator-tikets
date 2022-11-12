package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationMode;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;

/**
 * Class Implementation.
 * <p>
 * Contains property: <b>rating</b> - define two value {@link Rating}
 */
public class GenerationPropertyImpl extends GenerationProperty {
    private Rating rating;
    private GenerationMode generationMode;

    {
        rating = Rating.FIVE_POINT;
        generationMode = GenerationMode.MODE_1;
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets, Boolean unique, Rating rating) {
        super(quantityTickets, quantityQTickets, unique);
        this.rating = rating;
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets, Boolean unique, Rating rating,
                                  GenerationMode generationMode) {
        super(quantityTickets, quantityQTickets, unique);
        this.rating = rating;
        this.generationMode = generationMode;
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets, Rating rating,
                                  GenerationMode generationMode) {
        super(quantityTickets, quantityQTickets);
        this.rating = rating;
        this.generationMode = generationMode;
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets, Rating rating) {
        super(quantityTickets, quantityQTickets);
        this.rating = rating;
    }

    public GenerationPropertyImpl(int quantityTickets, int quantityQTickets, GenerationMode generationMode) {
        super(quantityTickets, quantityQTickets);
        this.generationMode = generationMode;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public GenerationMode getGenerationMode() {
        return generationMode;
    }

    public void setGenerationMode(GenerationMode generationMode) {
        this.generationMode = generationMode;
    }

    public enum Rating {
        /**
         * Five point ration
         */
        FIVE_POINT,
        /**
         * Ten point ration
         */
        TEN_POINT
    }
}
