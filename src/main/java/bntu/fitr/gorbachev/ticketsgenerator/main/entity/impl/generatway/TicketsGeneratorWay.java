package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;

import java.util.List;

public interface TicketsGeneratorWay<Q extends QuestionExt, E extends Ticket<? super Q>> {

    void conditionGeneration(List<Q> questions, GenerationProperty property) throws GenerationConditionException;

    List<E> generate(E templateTicket, List<Q> questions, GenerationProperty property);
}
