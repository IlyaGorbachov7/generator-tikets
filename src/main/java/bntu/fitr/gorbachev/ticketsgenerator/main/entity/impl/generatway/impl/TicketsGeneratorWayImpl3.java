package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.WrapperList;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.NumberQuestionsRequireException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketsGeneratorWayImpl3 extends TicketsGeneratorWayImpl2 {
    protected GenerationPropertyImpl prop;
    protected List<Integer> rangeQuest;
    protected WrapperList<Question2> wrapListQuest;
    protected WrapperList<Question2> wrapListRepeatQuest;
    protected WrapperList<Question2> bufferQuestCurrentTickets;

    protected void initFields(List<Question2> questions, GenerationProperty property) {
        prop = (GenerationPropertyImpl) property;
        rangeQuest = IntStream.range(0, prop.getQuantityQTickets()).boxed().collect(Collectors.toList());
        wrapListQuest = WrapperList.of(questions);
        wrapListRepeatQuest = WrapperList.of(questions.stream().filter(q -> q.getRepeat() > 0).toList());
        bufferQuestCurrentTickets = WrapperList.of();
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
    protected int getMinQuantityPossibleTickets() {
        int possibleQuantityTickets = wrapListQuest.size() / prop.getQuantityQTickets();
        return Math.min(possibleQuantityTickets, prop.getQuantityTickets());
    }

    @Override
    protected void generateTicketsWithMinNumber(int minQuantityTickets, List<Ticket<Question2>> listTickets, Ticket<Question2> tmpT) {
        tmpT.clearQuestions();
        IntStream.range(0, minQuantityTickets)
                .forEach(i -> {
                    Ticket<Question2> ticket = tmpT.clone();
                    rangeQuest.forEach(indexQ -> ticket.add(wrapListQuest.next()));
                    if (prop.isFlagRandomOrderQuestInTicket()) Collections.shuffle(ticket.getQuestions());
                    listTickets.add(ticket);
                });
    }


    @Override
    public void generateTicketsWithRemainingNumber(List<Ticket<Question2>> listTickets) {
        int remainingQuantity = prop.getQuantityTickets() - listTickets.size();
        Ticket<Question2> tmpT = listTickets.get(0);
        IntStream.range(0, remainingQuantity)
                .mapToObj(i -> tmpT.clone())
                .peek(this::changeQuestionsTicket)
                .forEach(listTickets::add);
    }

    @Override
    public void changeQuestionsTicket(Ticket<Question2> ticket) {
        List<Question2> quests = ticket.getQuestions();
        bufferQuestCurrentTickets.clear();

        for (var index : rangeQuest) {
            Question2 q;
            if (wrapListQuest.hasNext()) {
                q = tryReplaceThisQuest(null, null);
            } else {
                q = giveRepeatedQuest(-1);

                if (q == null && !prop.isUnique()) {
                    q = tryReplaceThisQuest(null, null);
                } else {
                    throw new RuntimeException("Compose unique questions non possible");
                }
            }
            quests.set(index, q);
            bufferQuestCurrentTickets.add(q);
        }

        if (prop.isFlagRandomOrderQuestInTicket()) Collections.shuffle(quests);
    }

    @Override
    protected Question2 tryReplaceThisQuest(Question2 nullQiest, WrapperList<Question2> nullWrapperList) {
        if (wrapListQuest.isEmpty()) throw new RuntimeException("list is empty, by try replace question");

        if (!wrapListQuest.hasNext()) {
            Collections.shuffle(wrapListQuest.getList());
            wrapListQuest.resetCurIndex();
        }
        while (bufferQuestCurrentTickets.contains(wrapListQuest.current())) {
            wrapListQuest.next();
        }
        return wrapListQuest.next();
    }

    @Override
    protected Question2 giveRepeatedQuest(int level) {
        if (wrapListRepeatQuest.isEmpty()) {
            return null;
        }
        if (!wrapListRepeatQuest.hasNext()) {
            wrapListRepeatQuest.resetCurIndex();
        }

        while (bufferQuestCurrentTickets.contains(wrapListRepeatQuest.current())) {
            wrapListRepeatQuest.next();
            if (!wrapListRepeatQuest.hasNext()) {
                return null;
            }
        }

        Question2 q = wrapListRepeatQuest.current();
        q.setRepeat(q.getRepeat() - 1);

        if (q.getRepeat() == 0) {
            wrapListRepeatQuest.remove(); // removed quest with prop:repeat == 0
        } else {
            wrapListRepeatQuest.next();
        }
        return q;
    }
}
