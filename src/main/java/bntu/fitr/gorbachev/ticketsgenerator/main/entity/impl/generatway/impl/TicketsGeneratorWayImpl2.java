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
                    "Вы указали количество вопросов в билете: " + prop.getQuantityQTickets() + ".\n" +
                    "Отсутствуют вопросы со сложностью: " + mapRang.get(false) + ".\n" +
                    ((findsNonMatchLevel.isEmpty()) ? ""
                            : "Однако найдены вопросы со сложностью: " + findsNonMatchLevel + ",\n") +
                    "необходимо указать сложность вопросов в приделах: [1;" + prop.getQuantityQTickets() + "]"
            );
        } else if (!findsNonMatchLevel.isEmpty()) {
            if (!prop.isFlagContinGenWithDepriveLev()) {
                // exception allowed to continue generation. That continue generation needed set flag == true
                throw new FindsNonMatchingLevel("Вы указали количество вопросов в билете: " + prop.getQuantityQTickets() + "\n" +
                                                "Кроме вопросов со сложностью: " + mapRang.get(true) + ", что позволяет сгенерировать билеты,\n" +
                                                "были найдены вопросы сложности: " + findsNonMatchLevel + ".\n" +
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
                                                          " (с учётом указанного Вами количество повторения).\n" +
                                                          "Не достаточно вопросов у сложности:\n" +
                                                          entryQuantityNotEnough.stream()
                                                                  .map(e -> e.getKey() + " => в количестве: " + e.getValue())
                                                                  .collect(Collectors.joining("\n")) + ".\n" +
                                                          "Среди вопросов, у которых указано число повторений будет\n" +
                                                          "равномерно увеличено недостающее число повторений, если таковые имеются,\n" +
                                                          "иначе вопросы будут выбраны произвольно.");
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
    public List<Ticket<Question2>> generate(Ticket<Question2> templateTicket, List<Question2> questions,
                                            GenerationProperty property) {
        List<Ticket<Question2>> listTickets = new ArrayList<>(property.getQuantityTickets());

        if (prop.isFlagRandomOrderReading()) {
            mapWrapListQuestGroupByLevel.forEach((lev, wrapListQ) -> Collections.shuffle(wrapListQ.getList()));
            mapWrapListQuestRepeatedGroupByLevel.forEach((lev, wrapListQ) -> Collections.shuffle(wrapListQ.getList()));
        }
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
            if (prop.isFlagRandomOrderQuestInTicket()) Collections.shuffle(rangeQuest);
            for (var level : rangeQuest) {
                ticket.add(mapWrapListQuestGroupByLevel.get(level).next());
            }
            listTickets.add(ticket);
        }
    }

    private void generateTicketsWithRemainingNumber(final List<Ticket<Question2>> listTickets) {
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

    private void changeQuestionsTicket(Ticket<Question2> ticket) {
        List<Question2> questions = ticket.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            int level = rangeQuest.get(i);
            WrapperList<Question2> wrapListQ = mapWrapListQuestGroupByLevel.get(level);

            if (wrapListQ.hasNext()) { // THis block code, designed in case free questions
                var q = wrapListQ.current();
                if (q.getId().equals(questions.get(i).getId())) { // this related with the advent of logic tryReplaceQuest
                    q = tryReplaceThisQuest(q, wrapListQ); // in case id-question from list == id question-replace
                } else {
                    q = wrapListQ.next();
                }
                questions.set(i, q);
            } else {
                // I take only those questions that con be repeated
                Question2 q = giveRepeatedQuest(level);
                if (!Objects.isNull(q)) { // in ase if questions with repeated is present
                    questions.set(i, q);
                } else if (!prop.isUnique()) { // in case if questions with repeated is absent, then random index
                    System.out.println(wrapListQ);
                    q = tryReplaceThisQuest(questions.get(i), wrapListQ);
                    System.out.println(wrapListQ);
                    questions.set(i, q);
                } else {
                    throw new RuntimeException("Compose unique questions not possible");
                }
            }

        }
    }

    private Question2 tryReplaceThisQuest(Question2 rscReplQuest, WrapperList<Question2> wrapperList) {
        class Methods {
            static <L, T> int findQuest(List<T> list, T elem, Comparator<T> comparator) {
                int findIndex = -1;
                for (int j = 0; j < list.size(); j++) {
                    if (comparator.compare(list.get(j), elem) == 0) {
                        findIndex = j;
                        break;
                    }
                }
                return findIndex;
            }
        }
        if (wrapperList.isEmpty()) throw new RuntimeException("list is empty, by try replace question ");

        if (wrapperList.size() == 1) {
            return rscReplQuest;
        }
        if (wrapperList.hasNext()) {
            Collections.rotate(wrapperList.getList().subList(wrapperList.getCurIndex(), wrapperList.size()), -1);
            return wrapperList.next();
        }

        int findIndex = Methods.findQuest(wrapperList, rscReplQuest, Comparator.comparing(Question2::getId));
        if (findIndex < 0) throw new NoSuchElementException("no find element by id: " + rscReplQuest);

        Collections.swap(wrapperList.getList(), 0, findIndex);

        // if wrapperList.hasNext == false
        Collections.shuffle(wrapperList.getList().subList(1, wrapperList.size()));
        Collections.rotate(wrapperList.getList(), -1);
        wrapperList.resetCurIndex();
        return wrapperList.next();
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

    private List<TicketNode> initTicketChildrenFromParent(List<Ticket<Question2>> listTicketsParent) {
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

    private static class TicketNode {
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
