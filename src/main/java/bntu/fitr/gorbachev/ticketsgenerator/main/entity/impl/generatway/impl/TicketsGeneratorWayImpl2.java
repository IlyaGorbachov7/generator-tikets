package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.WrapperList;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.FindsNonMatchingLevel;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.NumberQuestionsRequireException;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TicketsGeneratorWayImpl2 implements TicketsGeneratorWay<Question2, Ticket<Question2>> {
    private GenerationPropertyImpl prop;
    private Set<Integer> rangeQuest;
    private Map<Integer, WrapperList<Question2>> mapWrapListQuestGroupByLevel;
    private Map<Integer, WrapperList<Question2>> mapWrapListQuestRepeatedGroupByLevel;


    private final RandomGenerator randomGenerator = RandomGeneratorFactory.getDefault().create();

    private void initFields(List<Question2> questions, GenerationProperty property) {
        // Initialize here, that don't duplicate extra code
        prop = (GenerationPropertyImpl) property;
        rangeQuest = IntStream.rangeClosed(1, prop.getQuantityQTickets()).boxed()
                .collect(Collectors.toUnmodifiableSet());

        ExecutorService service = Executors.newFixedThreadPool(2);
        List<Future<?>> futures = new ArrayList<>(2);

        futures.add(service.submit(() -> {
            mapWrapListQuestGroupByLevel = questions.stream()
                    .collect(Collectors.groupingBy(Question2::getLevel, TreeMap::new,
                            Collectors.collectingAndThen(Collectors.toList(), WrapperList::of)));
        }));
        futures.add(service.submit(() -> {
            mapWrapListQuestRepeatedGroupByLevel = questions.stream()
                    .collect(Collectors.groupingBy(Question2::getLevel, TreeMap::new,
                            Collectors.filtering(q -> q.getRepeat() > 0,
                                    Collectors.collectingAndThen(Collectors.toList(), WrapperList::of))));
        }));
        service.shutdown();
        for (var future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void conditionGeneration(List<Question2> questions, GenerationProperty property)
            throws GenerationConditionException {
        initFields(questions, property);

        Map<Boolean, List<Integer>> mapRang = rangeQuest.stream()
                .collect(Collectors.partitioningBy(mapWrapListQuestGroupByLevel::containsKey));

        List<Integer> findsNonMatchLevel = mapWrapListQuestGroupByLevel.keySet().stream()
                .filter(Predicate.not(mapRang.get(true)::contains)).toList();

        if (!mapRang.get(false).isEmpty()) {
            System.out.println(mapRang.get(true));
            System.out.println(mapRang.get(false));
            // fatal exception
            throw new GenerationConditionException(
                    "Вы указали количество вопросов в билете: " + prop.getQuantityQTickets() + "\n" +
                    "Отсутствуют вопросы со сложностью: " + mapRang.get(false) + "\n" +
                    ((findsNonMatchLevel.isEmpty()) ? ""
                            : "Однако найдены вопросы со сложностью: " + findsNonMatchLevel + "\n") +
                    "Необходимо указать сложность вопросов в приделах: [1;" + prop.getQuantityQTickets() + "]"
            );
        } else if (!findsNonMatchLevel.isEmpty()) {
            if (!prop.isFlagContinGenWithDepriveLev()) {
                // exception allowed to continue generation. That continue generation needed set flag == true
                throw new FindsNonMatchingLevel("Вы указали количество вопросов в билете: " + prop.getQuantityQTickets() + "\n" +
                                                "По-мимо вопросов со сложностью: " + mapRang.get(true) + ", что позволяет сгенерировать билеты\n" +
                                                "Были найдены вопросы сложности: " + findsNonMatchLevel + "\n" +
                                                "Выборка вопросов будет производится только в пределах: [1; " + prop.getQuantityQTickets() + "]\n"
                );
            } else {
                // just remove unnecessary levels from global mapListQuest
                for (int unnecessaryLevel : findsNonMatchLevel) {
                    mapWrapListQuestGroupByLevel.remove(unnecessaryLevel);
                    mapWrapListQuestRepeatedGroupByLevel.remove(unnecessaryLevel);
                }
            }
        }

        // Further, checking quantity needed questions for each property: level with taking into account property: repeat
        // For each key:level  must be list, contains total quantity questions (with repeated) == requirement quantity
        // tickets
        Set<Map.Entry<Integer, Integer>> entryQuantityNotEnough = new HashSet<>(rangeQuest.size());
        for (var entry : mapWrapListQuestRepeatedGroupByLevel.entrySet()) {
            int totalQuantity = entry.getValue().stream().mapToInt(Question2::getRepeat).sum();

            totalQuantity = prop.getQuantityTickets() - totalQuantity - mapWrapListQuestGroupByLevel.get(entry.getKey()).size();
            if (totalQuantity > 0) {
                entryQuantityNotEnough.add(new AbstractMap.SimpleEntry<>(entry.getKey(), totalQuantity));
            }
        }
        if (prop.isUnique()) { // if true, this is means then we require that USER control repeated questions
            // then check really user take into account the conditions for generation tickets

            if (!entryQuantityNotEnough.isEmpty()) {
                // exception allowed to continew generation
                throw new NumberQuestionsRequireException("Вы указали: " + prop.getQuantityQTickets() + " вопросов в билете.\n" +
                                                          "Требуется, чтобы количество вопросов в каждой из сложностей суммарно\n" +
                                                          " был равен как минимум: " + prop.getQuantityTickets() + "" +
                                                          "(с учётом указанного Вами количество повторения)\n" +
                                                          "Не достаточно вопросов у сложностей:\n" +
                                                          entryQuantityNotEnough.stream()
                                                                  .map(e -> e.getKey() + " => в количестве: " + e.getValue())
                                                                  .collect(Collectors.joining("\n")) + "\n" +
                                                          "Среди вопросов, у которых указано число повторений будет\n" +
                                                          "равномерно увеличено недостающее число повторений, если таковые имеются,\n" +
                                                          "иначе вопросы будут выбраны рандомно.");
            }
        } else {
            for (var entry : entryQuantityNotEnough) {
                List<Question2> listRepeatedQuest = mapWrapListQuestRepeatedGroupByLevel.get(entry.getKey());
                if (!listRepeatedQuest.isEmpty()) {
                    int fullPass = entry.getValue() / listRepeatedQuest.size();
                    int quantityQuestForIncrease = entry.getValue() - fullPass * listRepeatedQuest.size();

                    for (int i = 0; i < listRepeatedQuest.size(); i++) {
                        Question2 repQuest = listRepeatedQuest.get(i);

                        int rep = fullPass + repQuest.getRepeat();
                        if (i < quantityQuestForIncrease) {
                            ++rep;
                        }
                        repQuest.setRepeat(rep);
                    }
                }
            }
        }
    }

    @Override
    public List<Ticket<Question2>> generate(Ticket<Question2> templateTicket, List<Question2> questions, GenerationProperty property) {
        List<Ticket<Question2>> listTickets = new ArrayList<>(property.getQuantityTickets());
        int minListSize = mapWrapListQuestGroupByLevel.entrySet().stream()
                .min(Map.Entry.comparingByValue(Comparator.comparingInt(WrapperList::size)))
                .orElseThrow().getValue().size();

        // generate with minimum possible and needed number tickets
        int minQuantityTickets = Integer.min(prop.getQuantityTickets(), minListSize);
        generateTicketsWithMinNumber(minQuantityTickets, listTickets, templateTicket);

        // checking on to continue generation additional tickets
        if (prop.getQuantityTickets() == minQuantityTickets) { // if equals, then complete generate
            return listTickets;
        }

        // else continue generation
        generateTicketsWithRemainingNumber(listTickets);
        return listTickets;
    }

    private void generateTicketsWithMinNumber(final int minQuantityTickets, final List<Ticket<Question2>> listTickets,
                                              Ticket<Question2> tmpT) {
        tmpT.clearQuestions();
        for (int indexArray = 0; indexArray < minQuantityTickets; ++indexArray) {
            Ticket<Question2> ticket = tmpT.clone();
            mapWrapListQuestGroupByLevel.values().forEach(wrapListQ -> ticket.add(wrapListQ.next()));
            listTickets.add(ticket);
        }
    }

    private void generateTicketsWithRemainingNumber(final List<Ticket<Question2>> listTickets) {
        Ticket<Question2> template = listTickets.get(0);

        int remainQuantityTickets = prop.getQuantityTickets() - listTickets.size();
        IntStream.range(0, remainQuantityTickets)
                .mapToObj(i -> template.clone())
                .peek(this::changeQuestionsTicket)
                .forEach(listTickets::add);
    }

    private void changeQuestionsTicket(Ticket<Question2> ticket) {
        List<Question2> questions = ticket.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            int level = i + 1;
            WrapperList<Question2> wrapListQ = mapWrapListQuestGroupByLevel.get(level);
            if (wrapListQ.hasNext()) {
                questions.set(i, wrapListQ.next());
            } else {
                // I take only those questions that con be repeated
                Question2 q = giveRepeatedQuest(level);
                if (!Objects.isNull(q)) { // in ase if questions with repeated is present
                    questions.set(i, q);

                } else if (!prop.isUnique()) { // in case if questions with repeated is absent, then random index
                    // Forced generate, via random // можно все рандомить списки сложностей
                    // или можно рандомить только список с паксимальной сложностю, а те оставить не подвижные как в родителе
                    wrapListQ.setCurIndex(randomGenerator.nextInt(0, wrapListQ.size()));
                    Logger.getLogger(this.getClass()
                            .getName()).info("Forced  choice question from list grouped  by level=" + level +
                                             " : indexQuest = " + wrapListQ.getCurIndex());
                    questions.set(i, wrapListQ.next());
                }
            }

        }
    }

    private Question2 giveRepeatedQuest(int level) {
        WrapperList<Question2> wrapListRepeatQ = mapWrapListQuestRepeatedGroupByLevel.get(level);
        if (!wrapListRepeatQ.isEmpty()) {
            if (!wrapListRepeatQ.hasNext()) wrapListRepeatQ.resetCurIndex();

            Question2 q = wrapListRepeatQ.current();
            q.setRepeat(q.getRepeat() - 1);

            if (q.getRepeat() == 0) {
                wrapListRepeatQ.remove(); // removed quest with prop:repeat == 0
                // but curStatePosRepeatQuest left unchanged
            } else {
                wrapListRepeatQ.next();
            }
            return q;
        }
        return null;
    }
}
