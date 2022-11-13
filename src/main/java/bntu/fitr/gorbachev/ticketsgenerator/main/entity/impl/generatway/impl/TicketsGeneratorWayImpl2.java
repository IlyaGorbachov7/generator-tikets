package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class TicketsGeneratorWayImpl2 implements TicketsGeneratorWay<Question2, Ticket<Question2>> {
    private Map<Integer, List<Question2>> mapGroupByLevel;
    private List<Integer> rangeQuest;


    @Override
    public void conditionGeneration(List<Question2> questions, GenerationProperty property)
            throws GenerationConditionException {
        GenerationPropertyImpl prop = (GenerationPropertyImpl) property;

        rangeQuest = IntStream.rangeClosed(1, prop.getQuantityQTickets()).boxed().toList();
        mapGroupByLevel = questions.stream().collect(Collectors.groupingBy(Question2::getLevel));


        List<Integer> range = rangeQuest;
        Map<Boolean, List<Integer>> boolListMap = mapGroupByLevel.keySet().stream()
                .collect(Collectors.partitioningBy(range::contains));

        boolean isNonMatchLevelNumber = boolListMap.get(false).isEmpty();

        if (!isNonMatchLevelNumber) {
            System.out.println(boolListMap.get(true));
            throw new GenerationConditionException("Вы указали " + prop.getQuantityQTickets() + " вопросов в билете.\n" +
                                                   "Данный режим генерации требует указать в файле уровень сложности\n" +
                                                   "вопроса в приделах [1; " + prop.getQuantityQTickets() + "]\n" +
                                                   "Среди вопросов были найдены вопросы со сложностью:"
                                                   + boolListMap.get(false) + ", которые не позволительны \n");
        }

        isNonMatchLevelNumber = range.size() == boolListMap.get(true).size();
        if (!isNonMatchLevelNumber) {
            throw new GenerationConditionException("Вы указали " + prop.getQuantityQTickets() + " вопросов в билете.\n" +
                                                   "Данный режим генерации требует указать в файле сложности\n" +
                                                   "вопросов в приделах [1; " + prop.getQuantityQTickets() + "]\n" +
                                                   "Не достает вопросов со сложностью:" +
                                                   range.stream().dropWhile(boolListMap.get(true)::contains).toList());
        }
    }

    @Override
    public List<Ticket<Question2>> generate(Ticket<Question2> templateTicket, List<Question2> questions, GenerationProperty property) {
        List<Ticket<Question2>> list = new ArrayList<>();


        return list;
    }


}
