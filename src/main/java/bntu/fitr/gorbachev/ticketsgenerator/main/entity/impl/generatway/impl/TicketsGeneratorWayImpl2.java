package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.FindsNonMatchingLevel;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.NumberQuestionsRequireException;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TicketsGeneratorWayImpl2 implements TicketsGeneratorWay<Question2, Ticket<Question2>> {
    private GenerationPropertyImpl prop;
    private Map<Integer, List<Question2>> mapListQuestGroupByLevel;
    private Map<Integer, Map.Entry<Integer, Integer>> mapStatePosQuest;
    private Set<Integer> rangeQuest;
    private Map<Integer, List<Question2>> mapListQuestRepeatedGroupByLevel;
    private Map<Integer, Map.Entry<Integer, Integer>> mapStatePosRepeatedQuest;

    private final RandomGenerator randomGenerator = RandomGeneratorFactory.getDefault().create();

    private void initFields(List<Question2> questions, GenerationProperty property) {
        // Initialize here, that don't duplicate extra code
        prop = (GenerationPropertyImpl) property;
        rangeQuest = IntStream.rangeClosed(1, prop.getQuantityQTickets()).boxed()
                .collect(Collectors.toUnmodifiableSet());

        ExecutorService service = Executors.newFixedThreadPool(2);
        List<Future<?>> futures = new ArrayList<>(2);

        futures.add(service.submit(() -> {
            mapListQuestGroupByLevel = questions.stream()
                    .collect(Collectors.groupingBy(Question2::getLevel, TreeMap::new, Collectors.toList()));

            mapStatePosQuest = rangeQuest.stream().map(lev -> new AbstractMap.SimpleEntry<>(lev, 0))
                    .collect(Collectors.toMap(Map.Entry::getKey, Function.identity()));
        }));
        futures.add(service.submit(() -> {
            mapListQuestRepeatedGroupByLevel = questions.stream()
                    .collect(Collectors.groupingBy(Question2::getLevel,
                            TreeMap::new, Collectors.filtering(q -> q.getRepeat() > 0, Collectors.toList())));

            mapStatePosRepeatedQuest = rangeQuest.stream().map(lev -> new AbstractMap.SimpleEntry<>(lev, 0))
                    .collect(Collectors.toMap(Map.Entry::getKey, Function.identity()));
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
                .collect(Collectors.partitioningBy(mapListQuestGroupByLevel::containsKey));

        List<Integer> findsNonMatchLevel = mapListQuestGroupByLevel.keySet().stream()
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
                    mapListQuestGroupByLevel.remove(unnecessaryLevel);
                    mapListQuestRepeatedGroupByLevel.remove(unnecessaryLevel);
                }
            }
        }

        // Further, checking quantity needed questions for each property: level with taking into account property: repeat
        // For each key:level  must be list, contains total quantity questions (with repeated) == requirement quantity
        // tickets
        Set<Map.Entry<Integer, Integer>> entryQuantityNotEnough = new HashSet<>(rangeQuest.size());
        for (var entry : mapListQuestRepeatedGroupByLevel.entrySet()) {
            int totalQuantity = entry.getValue().stream().mapToInt(Question2::getRepeat).sum();

            totalQuantity = prop.getQuantityTickets() - totalQuantity - mapListQuestGroupByLevel.get(entry.getKey()).size();
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
                                                          "Вопросом, у которых указано число повторений будет\n" +
                                                          "равномерно увеличено число повторений, если таковые имеются,\n" +
                                                          "иначе вопросы будут выбраны рандомно.");
            }
        } else {
            for (var entry : entryQuantityNotEnough) {
                List<Question2> listRepeatedQuest = mapListQuestRepeatedGroupByLevel.get(entry.getKey());
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
        Integer rangWithMinQuantityQ = mapListQuestGroupByLevel.entrySet().stream()
                .min(Map.Entry.comparingByValue(Comparator.comparingInt(List::size)))
                .orElseThrow().getKey();
        System.out.println("range with min quantity questions: " + rangWithMinQuantityQ);

        // generate with minimum possible and needed number tickets
        int minQuantityTickets = Integer.min(prop.getQuantityTickets(), mapListQuestGroupByLevel.get(rangWithMinQuantityQ).size());
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
        for (int indexArray = 0; indexArray < minQuantityTickets; ++indexArray) {
            Ticket<Question2> ticket = tmpT.clone();
            tmpT.clearQuestions();
            int level = 0;
            for (var listQ  // iterate by level : 1 , 2 , 3 ...
                    : mapListQuestGroupByLevel.values()) { // grouped map by level question
                var q = listQ.get(indexArray);
                ticket.add(q); // added in sequential order ~ equivalent to 1, 2, 3 ... questions with same sequential level
                // change state position lists. level++ -- this is iteration by level
                Map.Entry<Integer, Integer> entry = mapStatePosQuest.get(++level);
                entry.setValue(entry.getValue() + 1);
            }
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

        /*Теперрь нужно циклом бежать по вопросам в билете
         *
         * Справшивать по key (это level) састояение текущей позиции в
         * map списков вопросов и уже в зависимости от того есть ли там еще
         * сободные вопросы, которые еще нигразу не брались ТО БЕРЕМ и изминяем состояение
         *
         * Елси уже закончились то фиксируем вопросы легкие,
         * А ВОПРОС с уровнем сложности самой большей измияем в любом случаи   */

        for (int i = 0; i < questions.size(); i++) {
            int level = i + 1;
            int curStatePosQuest = mapStatePosQuest.get(level).getValue();
            List<Question2> listQuestByLevel = mapListQuestGroupByLevel.get(level);

            // checking curPos on the is within bounds list
            boolean isWithin = WrapperList.isIndexWithinBounds(curStatePosQuest, listQuestByLevel.size());
            if (isWithin) {
                questions.set(i, listQuestByLevel.get(curStatePosQuest));
                mapStatePosQuest.get(level).setValue(++curStatePosQuest);
            } else {
                // I take only those questions that con be repeated
                Question2 q = giveRepeatedQuest(level);

                if (!Objects.isNull(q)) { // in ase if questions with repeated is present
                    questions.set(i, q);
                } else if (!prop.isUnique()) { // in case if questions with repeated is absent, then random index
                    // Forced generate, via random // можно все рандомить списки сложностей
                    // или можно рандомить только список с паксимальной сложностю, а те оставить не подвижные как в родителе
                    curStatePosQuest = randomGenerator.nextInt(0, listQuestByLevel.size());
                    Logger.getLogger(this.getClass().getName()).info("Forced  choice question from list grouped  by level="
                                                                     + level + " : numberQuest = " + curStatePosQuest);
                    questions.set(i, listQuestByLevel.get(curStatePosQuest));
                    mapStatePosQuest.get(level).setValue(++curStatePosQuest);
                }
            }

        }
    }

    private Question2 giveRepeatedQuest(int level) {
        List<Question2> listRepeatedQuest = mapListQuestRepeatedGroupByLevel.get(level);
        if (!listRepeatedQuest.isEmpty()) {
            var entryPos = mapStatePosRepeatedQuest.get(level);
            int curStatePosRepeatedQuest = entryPos.getValue();

            boolean isWithin = WrapperList.isIndexWithinBounds(curStatePosRepeatedQuest, listRepeatedQuest.size());
            curStatePosRepeatedQuest = (isWithin) ? curStatePosRepeatedQuest : 0;

            Question2 q = listRepeatedQuest.get(curStatePosRepeatedQuest);
            q.setRepeat(q.getRepeat() - 1);

            if (q.getRepeat() == 0) {
                listRepeatedQuest.remove(curStatePosRepeatedQuest); // removed quest with prop:repeat == 0
                // but curStatePosRepeatQuest left unchanged
            } else {
                entryPos.setValue(++curStatePosRepeatedQuest);
            }
            return q;
        } else {
            mapStatePosRepeatedQuest.remove(level);
//            mapListQuestRepeatedGroupByLevel.remove(level);
        }
        return null;
    }

    private static class WrapperList {
        private int curIndexList;
        private final List<Question2> list;

        private WrapperList(List<Question2> list) {
            curIndexList = 0;
            this.list = list;
        }

        public int getCurIndexList() {
            return curIndexList;
        }

        public void setCurIndexList(int curIndexList) {
            this.curIndexList = curIndexList;
        }

        public int improvePosPrefix() {
            return ++curIndexList;
        }

        public int improvePosPostfix() {
            return curIndexList++;
        }

        public boolean isIndexWithinBounds() {
            return isIndexWithinBounds(curIndexList, list.size());
        }

        public void resetPos() {
            curIndexList = 0;
        }

        public List<Question2> getList() {
            return list;
        }

        public static WrapperList of(List<Question2> list) {
            return new WrapperList(list);
        }

        public static boolean isIndexWithinBounds(int index, int length) {
            try {
                Objects.checkIndex(index, length);
            } catch (RuntimeException e) {
                return false;
            }
            return true;
        }
    }

    public static void main(String[] args) {

        var randomer = RandomGeneratorFactory.getDefault().create();
        for (int i = 0; i < 10; i++) {
            System.out.println(randomer.nextInt(0, 14));
        }
        System.out.println("------------");
        for (int i = 0; i < 10; i++) {
            System.out.println(randomer.nextInt(0, 14));
        }
    }

}
