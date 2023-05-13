package bntu.fitr.gorbachev.ticketsgenerator.main.models.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.models.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.impl.generatway.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.impl.generatway.WrapperList;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.exceptions.FindsNonMatchingLevel;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.exceptions.NumberQuestionsRequireException;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketsGeneratorWayImpl2 implements TicketsGeneratorWay<Question2, Ticket<Question2>> {
    protected GenerationPropertyImpl prop;
    protected List<Integer> rangeQuest;
    protected Map<Integer, WrapperList<Question2>> mapWrapListQuestGroupByLevel;
    protected Map<Integer, WrapperList<Question2>> mapWrapListQuestRepeatedGroupByLevel;


    private final RandomGenerator randomGenerator = RandomGeneratorFactory.getDefault().create();

    protected void initFields(List<Question2> questions, GenerationProperty property) {
        // Initialize here, that don't duplicate extra code
        prop = (GenerationPropertyImpl) property;
        rangeQuest = IntStream.rangeClosed(1, prop.getQuantityQTickets()).boxed().collect(Collectors.toList());

        mapWrapListQuestGroupByLevel = questions.stream()
                .collect(Collectors.groupingBy(Question2::getLevel, TreeMap::new,
                        Collectors.collectingAndThen(Collectors.toList(), WrapperList::of)));


        mapWrapListQuestRepeatedGroupByLevel = questions.stream()
                .collect(Collectors.groupingBy(Question2::getLevel, TreeMap::new,
                        Collectors.filtering(q -> q.getRepeat() > QuestionExt.MIN_VALUE_REPEAT,
                                Collectors.collectingAndThen(Collectors.toList(), WrapperList::of))));

        // it is necessary to reduce by one the value of repetitions for questions with > MIN_VALUE_REPEAT
        // since always the question were separated into 2 arrays: mapWrapListQuestBySection and mapWrapListRepeatedQuestBySection
        questions.forEach(q -> {
            if (q.getRepeat() > QuestionExt.MIN_VALUE_REPEAT) {
                q.setRepeat(q.getRepeat() - 1);
            }
        });
    }

    @Override
    public void conditionGeneration(List<Question2> questions, GenerationProperty property)
            throws GenerationConditionException {
        initFields(questions, property);
        try {
            GenerationConditionException generationConditionException;
            Map<Boolean, List<Integer>> mapRang = rangeQuest.stream()
                    .collect(Collectors.partitioningBy(mapWrapListQuestGroupByLevel::containsKey));

            List<Integer> findsNonMatchLevel = mapWrapListQuestGroupByLevel.keySet().stream()
                    .filter(Predicate.not(mapRang.get(true)::contains)).toList();

            if (!mapRang.get(false).isEmpty()) {
                System.out.println(mapRang.get(true));
                System.out.println(mapRang.get(false));
                // fatal exception
                generationConditionException = new GenerationConditionException(
                        "Вы указали количество вопросов в билете: " + prop.getQuantityQTickets() + ".\n" +
                        "Отсутствуют вопросы со сложностью: " + mapRang.get(false) + ".\n" +
                        ((findsNonMatchLevel.isEmpty()) ? ""
                                : "Однако найдены вопросы со сложностью: " + findsNonMatchLevel + ",\n") +
                        "необходимо указать сложность вопросов в приделах: [1;" + prop.getQuantityQTickets() + "]"
                );
                throw new RuntimeException(generationConditionException);
            } else if (!findsNonMatchLevel.isEmpty()) {
                if (!prop.isFlagContinGenWithDepriveLev()) {
                    // exception allowed to continue generation. That continue generation needed set flag == true
                    generationConditionException =
                            new FindsNonMatchingLevel("Вы указали количество вопросов в билете: " + prop.getQuantityQTickets() + "\n" +
                                                      "Кроме вопросов со сложностью: " + mapRang.get(true) + ", что позволяет сгенерировать билеты,\n" +
                                                      "были найдены вопросы сложности: " + findsNonMatchLevel + ".\n" +
                                                      "Выборка вопросов будет производится только в пределах: [1; " + prop.getQuantityQTickets() + "]\n");
                    throw new RuntimeException(generationConditionException);
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
                    generationConditionException =
                            new NumberQuestionsRequireException("Вы указали: " + prop.getQuantityQTickets() + " вопросов в билете.\n" +
                                                                "Требуется, чтобы количество вопросов в каждой из сложностей суммарно\n" +
                                                                " был равен как минимум: " + prop.getQuantityTickets() + "" +
                                                                " (с учётом указанного Вами количество повторения).\n" +
                                                                "Не достаточно вопросов у сложности:\n" +
                                                                entryQuantityNotEnough.stream()
                                                                        .map(e -> e.getKey() + " => в количестве: " + e.getValue())
                                                                        .collect(Collectors.joining("\n")) + ".\n" +
                                                                "Среди вопросов, у которых указано число повторений строго больше " + QuestionExt.MIN_VALUE_REPEAT + ", будет\n" +
                                                                "равномерно-последовательно увеличено недостающее число повторений, если таковые имеются,\n" +
                                                                "однако если среди вопросов нет ни одного вопроса\n" +
                                                                " повторяемость которого больше " + QuestionExt.MIN_VALUE_REPEAT +"," +"вопросы будут выбраны равномерно-рандомно.");
                    throw new RuntimeException(generationConditionException);
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
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof GenerationConditionException generationConditionException1) {
                mapWrapListQuestRepeatedGroupByLevel.values().forEach(listQuest -> {
                    listQuest.forEach(q -> q.setRepeat(q.getRepeat() + 1));
                });
                throw generationConditionException1;
            } else {
                throw ex;
            }
        }
    }

    @Override
    public List<Ticket<Question2>> generate(Ticket<Question2> templateTicket, List<Question2> questions,
                                            GenerationProperty property) {
        List<Ticket<Question2>> listTickets = new ArrayList<>(property.getQuantityTickets());

        if (prop.isFlagRandomOrderReading()) {
            mapWrapListQuestGroupByLevel.forEach((lev, wrapListQ) -> Collections.shuffle(wrapListQ.getList()));
            mapWrapListQuestRepeatedGroupByLevel.forEach((lev, wrapListQ) -> Collections.shuffle(wrapListQ.getList()));
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

    protected int getMinQuantityPossibleTickets() {
        int minListSize = mapWrapListQuestGroupByLevel.entrySet().stream()
                .min(Map.Entry.comparingByValue(Comparator.comparingInt(WrapperList::size)))
                .orElseThrow().getValue().size();

        // generate with minimum possible and needed number tickets
        return Integer.min(prop.getQuantityTickets(), minListSize);
    }

    protected void generateTicketsWithMinNumber(final int minQuantityTickets, final List<Ticket<Question2>> listTickets,
                                                Ticket<Question2> tmpT) {
        tmpT.clearQuestions();
        for (int indexArray = 0; indexArray < minQuantityTickets; ++indexArray) {
            Ticket<Question2> ticket = tmpT.clone();
            if (prop.isFlagRandomOrderQuestInTicket()) Collections.shuffle(rangeQuest);
            for (var level : rangeQuest) {
                ticket.add(mapWrapListQuestGroupByLevel.get(level).next());
            }
            listTickets.add(ticket);
        }
    }

    protected void generateTicketsWithRemainingNumber(final List<Ticket<Question2>> listTickets) {
        List<TicketNode> listNodes = initTicketChildrenFromParent(listTickets);
        listNodes.stream()
                .flatMap(parentTicketNode -> parentTicketNode.getChildrenNodes().stream())
                .map(childTicketNode -> {

                    Ticket<Question2> ticket = childTicketNode.getTicket();
                    System.out.println("childern TIcket: " + ticket);
                    if (prop.isFlagRandomOrderQuestInTicket()) Collections.shuffle(rangeQuest);
                    this.changeQuestionsTicket(ticket);
                    System.out.println(ticket + "\n");
                    return ticket;
                }).forEach(listTickets::add);
    }

    protected void changeQuestionsTicket(Ticket<Question2> ticket) {
        List<Question2> questions = ticket.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            int level = rangeQuest.get(i);
            WrapperList<Question2> wrapListQ = mapWrapListQuestGroupByLevel.get(level);
            // I must search question from child ticket, who level equals with question from list questions by level
            int findIndex = (Methods.findQuestByLevel(questions, level));
            if (findIndex < 0) {
                throw new RuntimeException("find index  < 0 probably into list child questions was reset level: " + level
                                           + ".\n No found level: " + level);
            }
            Question2 questFromChildTick = questions.get(findIndex);
            Question2 questFromList;
            if (wrapListQ.hasNext()) { // THis block code, designed in case free questions
                questFromList = wrapListQ.current();
                if (questFromList.getId().equals(questFromChildTick.getId())) { // this related with the advent of logic tryReplaceQuest
                    questFromList = tryReplaceThisQuest(questFromList, wrapListQ); // in case id-question from list == id question-replace
                } else {
                    questFromList = wrapListQ.next();
                }
            } else {
                questFromList = giveRepeatedQuest(level); // I take only those questions that con be repeated
                if (Objects.isNull(questFromList)) { // in ase if questions with repeated is present
                    if (!prop.isUnique()) {
                        questFromList = tryReplaceThisQuest(questFromChildTick, wrapListQ);
                    } else {
                        throw new RuntimeException("Compose unique questions not possible");
                    }
                }
            }
            Collections.swap(questions, findIndex, i); // this invoked very very very IMPORTANT!
            questions.set(i, questFromList);
        }
    }

    protected Question2 tryReplaceThisQuest(Question2 rscReplQuest, WrapperList<Question2> wrapperList) {
        if (wrapperList.isEmpty()) throw new RuntimeException("list is empty, by try replace question ");

        if (wrapperList.size() == 1) {
            return rscReplQuest;
        }
        if (wrapperList.hasNext()) {
            Collections.rotate(wrapperList.getList().subList(wrapperList.getCurIndex(), wrapperList.size()), -1);
            return wrapperList.next();
        }

        int findIndex = Methods.findQuest(wrapperList, rscReplQuest, Comparator.comparing(Question2::getId));
        if (findIndex < 0) {
            throw new NoSuchElementException(TicketsGeneratorWayImpl2.class + " no find element by id: " + rscReplQuest);
        }

        Collections.swap(wrapperList.getList(), 0, findIndex);

        // if wrapperList.hasNext == false
        Collections.shuffle(wrapperList.getList().subList(1, wrapperList.size()));
        Collections.rotate(wrapperList.getList(), -1);
        wrapperList.resetCurIndex();
        return wrapperList.next();
    }

    protected Question2 giveRepeatedQuest(int level) {
        WrapperList<Question2> wrapListRepeatQ = mapWrapListQuestRepeatedGroupByLevel.get(level);
        if (!wrapListRepeatQ.isEmpty()) {
            if (!wrapListRepeatQ.hasNext()) wrapListRepeatQ.resetCurIndex();

            Question2 q = wrapListRepeatQ.current();

            if (q.getRepeat() == QuestionExt.MIN_VALUE_REPEAT) {
                wrapListRepeatQ.remove(); // removed quest with prop:repeat == MIN_VALUE_REPEAT
                // but curStatePosRepeatQuest left unchanged
            } else {
                q.setRepeat(q.getRepeat() - 1);
                wrapListRepeatQ.next();
            }
            return q;
        }
        return null;
    }

    protected List<TicketNode> initTicketChildrenFromParent(List<Ticket<Question2>> listTicketsParent) {
        int quantityTickets = listTicketsParent.size();
        int remainQuantityTickets = prop.getQuantityTickets() - quantityTickets;
        int fullPass = remainQuantityTickets / quantityTickets;
        int remainsChildrens = remainQuantityTickets - fullPass * quantityTickets;

        int size = Math.min(quantityTickets, remainQuantityTickets);
        List<TicketNode> listTicketNode = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            int nNodes = fullPass;
            if (i < remainsChildrens) {
                nNodes++;
            }
            listTicketNode.add(TicketNode.of(listTicketsParent.get(i), nNodes));
        }
        return listTicketNode;
    }

    private static class Methods {
        static <T> int findQuest(List<T> list, T elem, Comparator<T> comparator) {
            for (int j = 0; j < list.size(); j++) {
                if (comparator.compare(list.get(j), elem) == 0) {
                    return j;
                }
            }
            return -1;
        }

        static <T extends Question2> int findQuestByLevel(List<T> list, int level) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getLevel() == level) {
                    return j;
                }
            }
            return -1;
        }

    }

    protected static class TicketNode {
        private Ticket<Question2> ticket;
        private LinkedList<TicketNode> childrenNodes;

        private TicketNode() {
            childrenNodes = new LinkedList<>();
        }

        private TicketNode(Ticket<Question2> ticket) {
            this();
            this.ticket = ticket;
        }

        private TicketNode(Ticket<Question2> ticket, LinkedList<TicketNode> childrenNodes) {
            this.ticket = ticket;
            this.childrenNodes = childrenNodes;
        }

        public Ticket<Question2> getTicket() {
            return ticket;
        }

        public List<TicketNode> getChildrenNodes() {
            return childrenNodes;
        }

        public void setTicket(Ticket<Question2> ticket) {
            this.ticket = ticket;
        }

        public void setChildrenNodes(LinkedList<TicketNode> childrenNodes) {
            this.childrenNodes = childrenNodes;
        }

        public int getQuantityChildrenNodes() {
            return childrenNodes.size();
        }

        public boolean addNode(TicketNode node) {
            return childrenNodes.add(node);
        }

        public static TicketNode of(Ticket<Question2> ticket) {
            return new TicketNode(ticket);
        }

        public static TicketNode of(Ticket<Question2> ticket, int nChildNodeFromParent) {
            TicketNode parNode = new TicketNode(ticket);
            parNode.initChNodesFromParentClone(nChildNodeFromParent);
            return parNode;
        }

        public static TicketNode of(Ticket<Question2> ticket, LinkedList<TicketNode> childrenNodes) {
            return new TicketNode(ticket, childrenNodes);
        }

        private void initChNodesFromParentClone(int nChNodes) {
            for (int i = 0; i < nChNodes; i++) {
                TicketNode chNode = TicketNode.of(ticket.clone());
                childrenNodes.add(chNode);
            }
        }
    }

}
