package bntu.fitr.gorbachev.ticketsgenerator.main.panels.tools;


import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl.TicketsGeneratorWayImpl1;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl.TicketsGeneratorWayImpl2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl.TicketsGeneratorWayImpl3;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl.TicketsGeneratorWayImpl4;

/**
 * Mode enum will define generation tickets algorithm.
 */
public enum GenerationMode {
    MODE_1("Группировать по темам", TicketsGeneratorWayImpl1.class),
    MODE_2("Группировать по сложности", TicketsGeneratorWayImpl2.class),
    MODE_3("Рандомно", TicketsGeneratorWayImpl3.class),
    MODE_4("Последовательно", TicketsGeneratorWayImpl4.class);
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