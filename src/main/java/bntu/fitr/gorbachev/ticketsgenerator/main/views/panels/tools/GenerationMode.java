package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools;


import bntu.fitr.gorbachev.ticketsgenerator.main.models.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.impl.generatway.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.impl.generatway.impl.TicketsGeneratorWayImpl1;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.impl.generatway.impl.TicketsGeneratorWayImpl2;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.impl.generatway.impl.TicketsGeneratorWayImpl3;

/**
 * Mode enum will define generation tickets algorithm.
 */
public enum GenerationMode {
    MODE_3("Сплошным списком", TicketsGeneratorWayImpl3.class),
    MODE_1("Группировать по темам", TicketsGeneratorWayImpl1.class),
    MODE_2("Группировать по сложности", TicketsGeneratorWayImpl2.class);
    String name;

    Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generateWay;

    GenerationMode(String name, Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generatorWay) {
        this.generateWay = generatorWay;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> getGenerateWay() {
        return generateWay;
    }

    @Override
    public String toString() {
        return name;
    }
}