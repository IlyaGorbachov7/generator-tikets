package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.WrapperList;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.FindsChapterWithoutSection;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.FindsNonMatchingLevel;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.NumberQuestionsRequireException;

import java.util.*;
import java.util.function.Predicate;
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

        mapWrapListQuestBySection = questions.stream().collect(Collectors.groupingBy(Question2::getSection, LinkedHashMap::new,
                Collectors.collectingAndThen(Collectors.toList(), WrapperList::of)));

        mapWrapListRepeatedQuestBySection = questions.stream()
                .collect(Collectors.groupingBy(Question2::getSection, LinkedHashMap::new,
                        Collectors.filtering(q -> q.getRepeat() > 0,
                                Collectors.collectingAndThen(Collectors.toList(), WrapperList::of))));

    }

    @Override
    public void conditionGeneration(List<Question2> questions, GenerationProperty property) throws GenerationConditionException {
        initFields(questions, property);

        List<String> sections = mapWrapListQuestBySection.keySet()
                .stream().filter(Predicate.not(String::isEmpty)).toList();

        if (!Objects.isNull(mapWrapListQuestBySection.get(""))) {
            if (!prop.isFlagContinGenWithChapterWithoutSection()) {

                throw new FindsChapterWithoutSection("Вы указали " + prop.getQuantityQTickets() + " вопрос в билете.\n" +
                                                     "В файле обнаружены вопросы, не имеющих определения темы.\n" +
                                                     "В дальнейшем эти вопросы не будут включены в выборку."

                );
            } else {
                mapWrapListQuestBySection.remove("");
                mapWrapListRepeatedQuestBySection.remove("");
            }
        }

        int notEnough = mapWrapListQuestBySection.size() - prop.getQuantityQTickets();
        if (notEnough > 0) {
            List<String> findsNonMatch = mapWrapListQuestBySection.keySet().stream()
                    .toList().subList(prop.getQuantityQTickets(), mapWrapListQuestBySection.size());

            if (!prop.isFlagContinGenWithDepriveLev()) {
                throw new FindsNonMatchingLevel("Вы указали " + prop.getQuantityQTickets() + " вопрос в билете.\n" +
                                                "Были найдены следующие темы: \n" +
                                                mapWrapListQuestBySection.keySet() + ".\n" +
                                                "Выборка вопросов будет производится по первым " + prop.getQuantityQTickets() +
                                                " перечисленным темам.\n" +
                                                "Остальные темы: \n" + findsNonMatch + "\nв выборку не войдут");
            } else {
                for (var section :
                        findsNonMatch) {
                    mapWrapListQuestBySection.remove(section);
                    mapWrapListRepeatedQuestBySection.remove(section);
                }
            }
        } else if (notEnough < 0) {
            if (sections.isEmpty()) {
                throw new GenerationConditionException("Не обнаружено ни одна тема");
            }

            throw new GenerationConditionException("Вы указали " + prop.getQuantityQTickets() + " вопрос в билете.\n" +
                                                   "Были найдены следующие темы: " +
                                                   mapWrapListQuestBySection.keySet() + "\n" +
                                                   "Не достаточно количество тем, чтобы сгруппировать вопросы билета по темам.\n" +
                                                   "Минимально необходимое количество тем: " + prop.getQuantityQTickets() + "\n" +
                                                   "Не хватает тем в количестве: " + Math.abs(notEnough));
        }

        // Further, checking quantity needed questions for each property: level with taking into account property: repeat
        // For each key:level  must be list, contains total quantity questions (with repeated) == requirement quantity
        // tickets
        Set<Map.Entry<String, Integer>> entryQuantityNotEnough = new HashSet<>(rangeQuest.size());
        for (var entry : mapWrapListRepeatedQuestBySection.entrySet()) {
            int totalQuantity = entry.getValue().stream().mapToInt(Question2::getRepeat).sum();

            totalQuantity = prop.getQuantityTickets() - totalQuantity - mapWrapListQuestBySection.get(entry.getKey()).size();
            if (totalQuantity > 0) {
                entryQuantityNotEnough.add(new AbstractMap.SimpleEntry<>(entry.getKey(), totalQuantity));
            }
        }
        if (prop.isUnique()) { // if true, this is means then we require that USER control repeated questions
            // then check really user take into account the conditions for generation tickets

            if (!entryQuantityNotEnough.isEmpty()) {
                // exception allowed to continew generation
                throw new NumberQuestionsRequireException("Вы указали: " + prop.getQuantityQTickets() + " вопросов в билете.\n" +
                                                          "Требуется, чтобы количество вопросов в каждой из тем суммарно\n" +
                                                          " был равен как минимум: " + prop.getQuantityTickets() + " " +
                                                          " (с учётом указанного Вами количество повторения)\n" +
                                                          "Не достаточно вопросов у тем:\n" +
                                                          entryQuantityNotEnough.stream()
                                                                  .map(e -> e.getKey() + " => в количестве: " + e.getValue())
                                                                  .collect(Collectors.joining("\n")) + "\n" +
                                                          "Среди вопросов, у которых указано число повторений будет\n" +
                                                          "произвольно увеличено недостающее число повторений, если таковые имеются,\n" +
                                                          "иначе вопросы будут выбраны рандомно.");
            }
        } else {
            for (var entry : entryQuantityNotEnough) {
                List<Question2> listRepeatedQuest = mapWrapListRepeatedQuestBySection.get(entry.getKey());
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

        mapWrapListQuestGroupByLevel = new LinkedHashMap<>(prop.getQuantityQTickets());
        int i = 0;
        for (var entry :
                mapWrapListQuestBySection.entrySet()) {
            WrapperList<Question2> listQ = entry.getValue();
            int lev = i;
            listQ.forEach(q -> q.setLevel(lev)); // set level question
            mapWrapListQuestGroupByLevel.put(rangeQuest.get(i++), listQ);
        }

        mapWrapListQuestRepeatedGroupByLevel = new LinkedHashMap<>(prop.getQuantityQTickets());
        i = 0;
        for (var entry :
                mapWrapListRepeatedQuestBySection.entrySet()) {
            int lev = i;
            WrapperList<Question2> listQ = entry.getValue();
            listQ.forEach(q->q.setLevel(lev));
            mapWrapListQuestRepeatedGroupByLevel.put(rangeQuest.get(i++), listQ);
        }

    }

    @Override
    public List<Ticket<Question2>> generate(Ticket<Question2> templateTicket, List<Question2> questions, GenerationProperty property) {
        return super.generate(templateTicket, questions, property);
    }
}
