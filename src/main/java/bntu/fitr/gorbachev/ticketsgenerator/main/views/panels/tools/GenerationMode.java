package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools;


import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.generatway.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.generatway.impl.TicketsGeneratorWayImpl1;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.generatway.impl.TicketsGeneratorWayImpl2;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.generatway.impl.TicketsGeneratorWayImpl3;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;

/**
 * Mode enum will define generation tickets algorithm.
 */
public enum GenerationMode {
    //panel.main.generation.mode...
    MODE_3("panel.main.generation.mode.level", TicketsGeneratorWayImpl3.class),
    MODE_1("panel.main.generation.mode.section", TicketsGeneratorWayImpl1.class),
    MODE_2("panel.main.generation.mode.sequence", TicketsGeneratorWayImpl2.class);
    String name;

    Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generateWay;

    GenerationMode(String name, Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> generatorWay) {
        this.generateWay = generatorWay;
        this.name = name;
    }

    public String getKeyName() {
        return name;
    }

    public String getName() {
        return Localizer.get(name);
    }

    public Class<? extends TicketsGeneratorWay<Question2, Ticket<Question2>>> getGenerateWay() {
        return generateWay;
    }

    @Override
    public String toString() {
        return Localizer.get(name);
    }
}