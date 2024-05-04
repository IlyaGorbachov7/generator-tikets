package bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.generatway;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.GenerationConditionException;

import java.util.List;

public interface TicketsGeneratorWay<Q extends QuestionExt, E extends Ticket<? super Q>> {

    void conditionGeneration(List<Q> questions, GenerationProperty property) throws GenerationConditionException;

    List<E> generate(E templateTicket, List<Q> questions, GenerationProperty property);
}
