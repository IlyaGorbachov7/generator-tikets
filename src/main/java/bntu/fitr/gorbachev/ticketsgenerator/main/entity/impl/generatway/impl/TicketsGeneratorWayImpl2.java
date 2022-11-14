package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;

import java.util.*;
import java.util.function.Function;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TicketsGeneratorWayImpl2 implements TicketsGeneratorWay<Question2, Ticket<Question2>> {
    private GenerationPropertyImpl prop;
    private Map<Integer, List<Question2>> mapGroupByLevel;
    private Set<Integer> rangeQuest;
    private Map<Integer, Map.Entry<Integer, Integer>> mapStatePos;
    private RandomGenerator randomGenerator = RandomGeneratorFactory.getDefault().create();


    @Override
    public void conditionGeneration(List<Question2> questions, GenerationProperty property)
            throws GenerationConditionException {
        // Initialize here, that don't duplicate extra code
        prop = (GenerationPropertyImpl) property;
        rangeQuest = IntStream.rangeClosed(1, prop.getQuantityQTickets()).boxed()
                .collect(Collectors.toUnmodifiableSet());

        mapGroupByLevel = questions.stream()
                .collect(Collectors.groupingBy(Question2::getLevel, TreeMap::new, Collectors.toList()));

        mapStatePos = rangeQuest.stream().map(lev -> Map.entry(lev, 0))
                .collect(Collectors.toMap(Map.Entry::getKey, Function.identity()));

        Set<Integer> range = rangeQuest;
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


        // Further, checking quantity needed questions for each property: level with taking into account property: repeat
        // For each key:level  must be list, contains total quantity questions (with repeated) == requirement quantity
        // tickets
        if (prop.isUnique()) { // if true, this is means then we require that USER control repeated questions
            // then check really user take into account the conditions for generation tickets
            for (var entry : mapGroupByLevel.entrySet()) {
                int totalQuantity = entry.getValue()
                        .stream().mapToInt(Question2::getRepeat).sum();
                if (totalQuantity != prop.getQuantityTickets()) {
                    throw new GenerationConditionException("Вы указали " + prop.getQuantityQTickets() + " вопросов в билете.\n" +
                                                           "Данный режим генерации требует, чтобы количество вопросов\n" +
                                                           "в списке со ложностью: " + entry.getKey() + "\n" +
                                                           "(с учётом указанного количество повторения вопроса) был\n" +
                                                           "равен количеству билетов: " + prop.getQuantityTickets());
                }
            }
        }

    }

    @Override
    public List<Ticket<Question2>> generate(Ticket<Question2> templateTicket, List<Question2> questions, GenerationProperty property) {
        List<Ticket<Question2>> listTickets = new ArrayList<>();

//        mapGroupByLevel.values().forEach(listQuest -> {
//            listQuest.sort(Comparator.comparingInt(QuestionExt::getRepeat));
//        });
//        System.out.println("-------------------- Map SORTED ----------------");
        mapGroupByLevel.forEach((k, v) -> {
            System.out.println("------ k=" + k + " -------- size: " + v.size());
            v.forEach(System.out::println);
        });

        Integer rangWithMinQuantityQ = mapGroupByLevel.entrySet().stream()
                .min(Map.Entry.comparingByValue(Comparator.comparingInt(List::size)))
                .orElseThrow().getKey();
        System.out.println("range with min quantity questions: " + rangWithMinQuantityQ);

        // generate with minimum possible and needed number tickets
        int minQuantityTickets = Integer.min(prop.getQuantityTickets(), mapGroupByLevel.get(rangWithMinQuantityQ).size());
        generateTicketsWithMinNumber(minQuantityTickets, listTickets, templateTicket);

        // checking on to continue generation additional tickets
        if (prop.getQuantityQTickets() == minQuantityTickets) { // if equals, then complete generate
            return listTickets;
        }

        // else continue generation
        int offsetStartIndex = minQuantityTickets;
        minQuantityTickets = prop.getQuantityTickets() - minQuantityTickets;
        generateTicketsWithRemainingNumber(offsetStartIndex, minQuantityTickets, listTickets);

        return listTickets;
    }

    private void generateTicketsWithMinNumber(final int minQuantityTickets, final List<Ticket<Question2>> listTickets,
                                              Ticket<Question2> tmpT) {
        for (int indexArray = 0; indexArray < minQuantityTickets; ) {
            Ticket<Question2> ticket = tmpT.clone();
            tmpT.clearQuestions();
            int level = 0;
            for (var listQ  // iterate by level : 1 , 2 , 3 ...
                    : mapGroupByLevel.values()) { // grouped map by level question
                var q = listQ.get(indexArray);
                ticket.add(q); // added in sequential order ~ equivalent to 1, 2, 3 ... questions with same sequential level
                // change state position lists. level++ -- this is iteration by level
                mapStatePos.get(level++).setValue(++indexArray);
            }
            listTickets.add(ticket);
        }
    }

    private void generateTicketsWithRemainingNumber(final int offsetStartIndex, final int remainQuantityTickets,
                                                    final List<Ticket<Question2>> listTickets) {
        System.out.println("offsetStart:" + offsetStartIndex);
        System.out.println("remainQuestionTic: " + remainQuantityTickets);
        System.out.println("-------------------------------- list tickets ----------------");
        listTickets.forEach(System.out::println);

        List<TicketNode> listNodes = initTicketChildrenFromParent(listTickets);

        for (TicketNode ticketNode : listNodes) {
            for (var chNode : ticketNode.getChildrenNodes()) {
                changeQuestionsTicket(chNode);
                listTickets.add(chNode.getTicket());
            }
        }
        System.out.println("-------------------------------- list tickets ----------------");
        listTickets.forEach(System.out::println);
    }

    private List<TicketNode> initTicketChildrenFromParent(List<Ticket<Question2>> listTicketsParent) {
        // сколько сгенерированых билетов
        int quantityTickets = listTicketsParent.size();
        // сктолько билетов еще осталось
        int remainQuantityTickets = prop.getQuantityTickets() - quantityTickets;
        // 1 полный проход по всем билетам, чтобы сгенерировать детей
        int fullPass = remainQuantityTickets / quantityTickets;
        // и узнаем остаток билетов, которым нужно повторно сгенерировать детей
        int remainsChildrens = remainQuantityTickets - fullPass * quantityTickets;

        List<TicketNode> listTicketNode = new ArrayList<>();
        for (int i = 0; i < quantityTickets; i++) {
            int nNodes = fullPass;
            if (i < remainsChildrens) {
                nNodes++;
            }
            listTicketNode.add(TicketNode.of(listTicketsParent.get(i), nNodes));
        }

        return listTicketNode;
    }

    private void changeQuestionsTicket(TicketNode ticketNode) {
        List<Question2> questions = ticketNode.getTicket().getQuestions();

        /*Теперрь нужно циклом бежать по вопросам в билете
         *
         * Справшивать по key (это level) састояение текущей позиции в
         * map списков вопросов и уже в зависимости от того есть ли там еще
         * сободные вопросы, которые еще нигразу не брались ТО БЕРЕМ и изминяем состояение
         *
         * Елси уже закончились то фиксируем вопросы легкие,
         * А ВОПРОС с уровнем сложности самой большей измияем в любом случаи   */

        for (int i = 0; i < questions.size(); i++) {
            int levelQuest = i + 1;
            int curPosInQuestListByLevel = mapStatePos.get(levelQuest).getValue();
            List<Question2> listQuestByLevel = mapGroupByLevel.get(levelQuest);

            // checking curPos on the is within bounds list
            boolean isWithin = WrapperList.isIndexWithinBounds(curPosInQuestListByLevel, listQuestByLevel.size());
            if (isWithin) {
                questions.set(i, listQuestByLevel.get(curPosInQuestListByLevel));
            } else {
                // I take only those questions that con be repeated
                /*
                *
                *
                * Здесь нужно учесть тот факт, чтобы не брало вопрос один и тот же пока  он не обнулился
                * допустпм если у нас есть вопрос с repeat = 100 то только он будет повторяться, пока он не
                * исчезнит.
                *
                * Нужно сделать как-то равномерно, то есть как бы равномерно сделать по всем вопросам, которые повторяються
                *
                * */
                Question2 qFirst = listQuestByLevel.stream().filter(q -> q.getRepeat() > 0)
                        .findFirst().orElse(null);

                if (!Objects.isNull(qFirst)) { // in ase if questions with repeated is present
                    qFirst.setRepeat(qFirst.getRepeat() - 1); // reducing number repeat this question
                    questions.set(i, qFirst);
                } else if (!prop.isUnique()) { // in case if questions with repeated is absent, then random index
                    // Forced generate, via random // можно все рандомить списки сложностей
                    // или можно рандомить только список с паксимальной сложностю, а те оставить не подвижные как в родителе
                }
            }

        }
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
            System.out.println(randomer.nextInt(0, 10));
        }
        System.out.println("------------");
        for (int i = 0; i < 10; i++) {
            System.out.println(randomer.nextInt(0, 10));
        }
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

        private void initChNodesFromParentClone(int nChNodes) {
            for (int i = 0; i < nChNodes; i++) {
                TicketNode chNode = TicketNode.of(ticket.clone());
                childrenNodes.add(chNode);
            }
        }

        public static TicketNode of(Ticket<Question2> ticket, LinkedList<TicketNode> childrenNodes) {
            return new TicketNode(ticket, childrenNodes);
        }
    }


}
