package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.WrapperList;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.NumberQuestionsRequireException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketsGeneratorWayImpl3 implements TicketsGeneratorWay<Question2, Ticket<Question2>> {
    protected GenerationPropertyImpl prop;
    protected List<Integer> rangeQuest;
    protected WrapperList<Question2> wrapListQuest;
    protected WrapperList<Question2> wrapListRepeatQuest;

    protected void initFields(List<Question2> questions, GenerationProperty property) {
        prop = (GenerationPropertyImpl) property;
        rangeQuest = IntStream.rangeClosed(1, prop.getQuantityQTickets()).boxed().collect(Collectors.toList());
        wrapListQuest = WrapperList.of(questions);
        wrapListRepeatQuest = WrapperList.of(questions.stream().filter(q -> q.getRepeat() > 0).toList());
    }

    @Override
    public void conditionGeneration(List<Question2> questions, GenerationProperty property) throws GenerationConditionException {
        initFields(questions, property);

        if (questions.size() < prop.getQuantityQTickets()) {
            throw new GenerationConditionException(
                    String.format("""
                                    Вы указали: %d вопросов в билете.
                                    Недостаточно вопросов, чтобы сгенерировать билет без повторяющихся вопросов
                                    """,
                            prop.getQuantityQTickets())
            );
        }

        int totalQuantity = questions.size() + wrapListRepeatQuest.stream()
                .mapToInt(Question2::getRepeat).sum();
        totalQuantity = prop.getQuantityTickets() * prop.getQuantityQTickets() - totalQuantity;
        if (totalQuantity > 0) {
            if (prop.isUnique()) {
                throw new NumberQuestionsRequireException(
                        String.format("""
                                        Вы указали: %d вопросов в билете.
                                        Требуется, чтобы количество вопросов суммарно с учётом указанного Вами
                                        количество повторения был равен как минимум:
                                        КоличествоБилетов * КоличествоВопросовБилета = %d * %d = %d
                                        Недостаточно вопросов, в количестве: %d
                                        Среди вопросов, у которых указано число повторений будет
                                        произвольно увеличено недостающее число повторений, если таковые имеются,
                                        иначе вопросы будут выбраны рандомно.""",
                                prop.getQuantityQTickets(), prop.getQuantityTickets(), prop.getQuantityQTickets(),
                                prop.getQuantityTickets() * prop.getQuantityQTickets(), totalQuantity));
            } else if (!wrapListRepeatQuest.isEmpty()) {
                int fullPass = totalQuantity / wrapListRepeatQuest.size();
                int quantityQuestForIncrease = totalQuantity - fullPass * wrapListRepeatQuest.size();
                for (int i = 0; i < wrapListRepeatQuest.size(); i++) {
                    Question2 repQuest = wrapListRepeatQuest.get(i);

                    int rep = fullPass + repQuest.getRepeat();
                    if (i < quantityQuestForIncrease) {
                        ++rep;
                    }
                    repQuest.setRepeat(rep);
                }
            }
        }
    }

    @Override
    public List<Ticket<Question2>> generate(Ticket<Question2> templateTicket, List<Question2> questions, GenerationProperty property) {
        if (prop.isFlagRandomOrderReading()) {
            Collections.shuffle(wrapListQuest.getList());
            Collections.shuffle(wrapListRepeatQuest.getList());
        }
//        int possibleQuantityTickets = wrapListQuest.size()/

//        int minQuantityTickets = Math.min();

        return null;
    }

    private void generateTicketsWithNumber(final int minQuantityTickets, final List<Ticket<Question2>> listTickets,
                                           Ticket<Question2> tmpT) {
        tmpT.clearQuestions();

        for (int i = 0; i < minQuantityTickets; i++) {
            Ticket<Question2> ticket = tmpT.clone();
            if (prop.isFlagRandomOrderQuestInTicket()) Collections.shuffle(rangeQuest);

            tmpT.setQuestions(new ArrayList<>(rangeQuest.size()));
            for (var level :
                    rangeQuest) {

            }
        }


    }
}
