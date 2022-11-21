package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.WrapperList;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.FindsNonMatchingLevel;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class TicketsGeneratorWayImpl1 extends TicketsGeneratorWayImpl2 {
    protected Map<String, WrapperList<Question2>> mapWrapListQuestBySection;
    protected Map<String, WrapperList<Question2>> mapWrapListRepeatedQuestBySection;

    @Override
    protected void initFields(List<Question2> questions, GenerationProperty property) {
        prop = (GenerationPropertyImpl) property;
        rangeQuest = IntStream.rangeClosed(1, prop.getQuantityQTickets()).boxed().collect(Collectors.toList());

        mapWrapListQuestBySection = questions.stream().collect(Collectors.groupingBy(Question2::getSection,
                Collectors.collectingAndThen(Collectors.toList(), WrapperList::of)));

        mapWrapListRepeatedQuestBySection = questions.stream()
                .collect(Collectors.groupingBy(Question2::getSection, Collectors.filtering(q -> q.getRepeat() > 0,
                        Collectors.collectingAndThen(Collectors.toList(), WrapperList::of))));

    }

    @Override
    public void conditionGeneration(List<Question2> questions, GenerationProperty property) throws GenerationConditionException {
        initFields(questions, property);
        int notEnough = mapWrapListQuestBySection.size() - prop.getQuantityQTickets();
        if (notEnough > 0) {
            throw new FindsNonMatchingLevel("Вы указали N вопровос в билете. При считывании " +
                                            "были темы :" +
                                            "" +
                                            "" +
                                            "А так же темы :::" +
                                            "Выборка вопросов будет производится по вермым N переченичленных " +
                                            "тем, остальные темы не будут учтены");
        } else if (notEnough < 0) {
            throw new GenerationConditionException("Не хватет еще " + notEnough + " тем");
        }

        mapWrapListQuestGroupByLevel = new LinkedHashMap<>(prop.getQuantityQTickets());
        int level = 0;
        for (var entry :
                mapWrapListQuestBySection.entrySet()) {
            mapWrapListQuestGroupByLevel.put(++level, entry.getValue());
        }

        mapWrapListQuestRepeatedGroupByLevel = new LinkedHashMap<>(prop.getQuantityQTickets());
        level= 0;
        for (var entry :
                mapWrapListRepeatedQuestBySection.entrySet()) {
            mapWrapListQuestRepeatedGroupByLevel.put(++level, entry.getValue());
        }
    }

    @Override
    public List<Ticket<Question2>> generate(Ticket<Question2> templateTicket, List<Question2> questions, GenerationProperty property) {
        return super.generate(templateTicket, questions, property);
    }
}
