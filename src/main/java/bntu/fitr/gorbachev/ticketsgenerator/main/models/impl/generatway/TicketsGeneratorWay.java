package bntu.fitr.gorbachev.ticketsgenerator.main.models.impl.generatway;

import bntu.fitr.gorbachev.ticketsgenerator.main.models.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.exceptions.GenerationConditionException;

import java.util.List;

public interface TicketsGeneratorWay<Q extends QuestionExt, E extends Ticket<? super Q>> {

    void conditionGeneration(List<Q> questions, GenerationProperty property) throws GenerationConditionException;

    List<E> generate(E templateTicket, List<Q> questions, GenerationProperty property);
}
