package bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.generatway.WrapperList;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.NumberQuestionsRequireException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * I want node you attention, that ticket can't contain 2 same questions.
 * <p>
 * So those questions, who have property <b>repeat > 0</b> this  is  no means, that
 * this questions will must repeat specified number of times.
 * <p>
 * Quantity question in list should be at least equals quantity questions in one ticket
 */
public class TicketsGeneratorWayImpl3 extends TicketsGeneratorWayImpl2 {
    protected WrapperList<Question2> wrapListQuest;
    protected WrapperList<Question2> wrapListRepeatQuest;
    protected WrapperList<Question2> wrapListQuestRandom;
    protected List<Question2> bufferQuestCurrentTickets;

    protected void initFields(List<Question2> questions, GenerationProperty property) {
        prop = (GenerationPropertyImpl) property;
        rangeQuest = IntStream.range(0, prop.getQuantityQTickets()).boxed().collect(Collectors.toList());
        wrapListQuest = WrapperList.of(questions);
        wrapListQuestRandom = WrapperList.of(new ArrayList<>(questions));
        Collections.shuffle(wrapListQuestRandom.getList());
        wrapListRepeatQuest = WrapperList.of(questions.stream().filter(q -> q.getRepeat() > QuestionExt.MIN_VALUE_REPEAT)
                .collect(Collectors.toList()));
        bufferQuestCurrentTickets = new ArrayList<>(prop.getQuantityQTickets());

        // it is necessary to reduce by one the value of repetitions for questions with > MIN_VALUE_REPEAT
        // since always the question were separated into 2 arrays: mapWrapListQuestBySection and mapWrapListRepeatedQuestBySection
        questions.forEach(q -> {
            if (q.getRepeat() > QuestionExt.MIN_VALUE_REPEAT) {
                q.setRepeat(q.getRepeat() - 1);
            }
        });
    }

    @Override
    public void conditionGeneration(List<Question2> questions, GenerationProperty property) throws GenerationConditionException {
        initFields(questions, property);
        try {
            GenerationConditionException generationConditionException;
            if (questions.size() < prop.getQuantityQTickets()) {
                generationConditionException = new GenerationConditionException(
                        String.format("""
                                        Вы указали: %d вопросов в билете.
                                        Недостаточно вопросов, чтобы сгенерировать билет без повторяющихся вопросов
                                        """, prop.getQuantityQTickets()));
                throw new RuntimeException(generationConditionException);
            }

            int totalQuantity = questions.size() + wrapListRepeatQuest.stream()
                    .mapToInt(Question2::getRepeat).sum();
            totalQuantity = prop.getQuantityTickets() * prop.getQuantityQTickets() - totalQuantity;
            if (totalQuantity > 0) {
                if (prop.isUnique()) {
                    generationConditionException = new NumberQuestionsRequireException(
                            String.format("""
                                            Вы указали: %d вопросов в билете.
                                            Требуется, чтобы количество вопросов суммарно с учётом указанного Вами
                                            количество повторения был равен как минимум:
                                            КоличествоБилетов * КоличествоВопросовБилета = %d * %d = %d
                                            Недостаточно вопросов, в количестве: %d
                                            Среди вопросов, у которых указано число повторений строго больше %d, будет
                                            равномерно-последовательно увеличено недостающее число повторений, если таковые имеются,
                                            однако если среди вопросов нет ни одного вопроса
                                            повторяемость которого больше %d, вопросы будут выбраны равномерно-рандомно.
                                            """,
                                    prop.getQuantityQTickets(), prop.getQuantityTickets(), prop.getQuantityQTickets(),
                                    prop.getQuantityTickets() * prop.getQuantityQTickets(), totalQuantity,
                                    QuestionExt.MIN_VALUE_REPEAT, QuestionExt.MIN_VALUE_REPEAT));
                    throw new RuntimeException(generationConditionException);
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
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof GenerationConditionException generationConditionException1) {
                wrapListRepeatQuest.forEach(q -> q.setRepeat(q.getRepeat() + 1));
                throw generationConditionException1;
            } else {
                throw ex;
            }
        }
    }

    @Override
    public List<Ticket<Question2>> generate(Ticket<Question2> templateTicket, List<Question2> questions, GenerationProperty property) {
        List<Ticket<Question2>> listTickets = new ArrayList<>(property.getQuantityTickets());

        if (prop.isFlagRandomOrderReading()) {
            Collections.shuffle(wrapListQuest.getList());
            Collections.shuffle(wrapListRepeatQuest.getList());
        }
        int minQuantityTickets = getMinQuantityPossibleTickets();
        generateTicketsWithMinNumber(minQuantityTickets, listTickets, templateTicket);

        // checking on to continue generation additional tickets
        if (prop.getQuantityTickets() == minQuantityTickets) { // if equals, then complete generate
            return listTickets;
        }

        // else continue generation
        generateTicketsWithRemainingNumber(listTickets);
        return listTickets;
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
                q = wrapListQuest.next();
            } else {
                q = giveRepeatedQuest(-1);

                if (q == null /*&& !prop.isUnique()*/) {
                    q = tryReplaceThisQuest(null, null);
                }
            }

            quests.set(index, q);
            bufferQuestCurrentTickets.add(q);
        }

        if (prop.isFlagRandomOrderQuestInTicket()) Collections.shuffle(quests);
    }

    @Override
    protected Question2 tryReplaceThisQuest(Question2 nullQiest, WrapperList<Question2> nullWrapperList) {
        if (wrapListQuestRandom.isEmpty()) throw new RuntimeException("list is empty, by try replace question");

        if (!wrapListQuestRandom.hasNext()) {
            Collections.shuffle(wrapListQuestRandom.getList());
            wrapListQuestRandom.resetCurIndex();
        }
        while (bufferQuestCurrentTickets.contains(wrapListQuestRandom.current())) {
            wrapListQuestRandom.next();
            if (!wrapListQuestRandom.hasNext()) {
                Collections.shuffle(wrapListQuestRandom.getList());
                wrapListQuestRandom.resetCurIndex();
            }
        }

        return wrapListQuestRandom.next();
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

        if (q.getRepeat() == QuestionExt.MIN_VALUE_REPEAT) {
            wrapListRepeatQuest.remove(); // removed quest with prop:repeat == 0
        } else {
            q.setRepeat(q.getRepeat() - 1);
            wrapListRepeatQuest.next();
        }
        return q;
    }
}

