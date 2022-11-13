package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;

import java.util.List;

public class TicketsGeneratorWayImpl2 implements TicketsGeneratorWay<Question2, Ticket<Question2>> {

    @Override
    public void conditionGeneration(List<Question2> questions, GenerationProperty property) throws GenerationConditionException {

    }

    @Override
    public List<Ticket<Question2>> generate(Ticket<Question2> templateTicket, List<Question2> questions, GenerationProperty property) {
        return null;
    }
}
