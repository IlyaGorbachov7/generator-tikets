package bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.FindsChapterWithoutSection;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.FindsNonMatchingLevel;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.NumberQuestionsRequireException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.generatway.WrapperList;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;

import java.util.*;
import java.util.function.Predicate;
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
    public void conditionGeneration(List<Question2> questions, GenerationProperty property) throws GenerationConditionException {
        initFields(questions, property);
        try {
            GenerationConditionException generationConditionException;
            List<String> sections = mapWrapListQuestBySection.keySet()
                    .stream().filter(Predicate.not(String::isEmpty)).toList();

            if (!Objects.isNull(mapWrapListQuestBySection.get(""))) {
                if (!prop.isFlagContinGenWithChapterWithoutSection()) {
                    generationConditionException = new FindsChapterWithoutSection(String.format(
                            Objects.requireNonNull(Localizer.get("generator.message.error.1")), prop.getQuantityQTickets()));
                    throw new RuntimeException(generationConditionException);
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
                    String res = String.format(Objects.requireNonNull(Localizer.get("generator.message.warn.1")),
                            prop.getQuantityQTickets(), mapWrapListQuestBySection.keySet(), prop.getQuantityQTickets(),
                            findsNonMatch);
                    generationConditionException = new FindsNonMatchingLevel(res);
                    throw new RuntimeException(generationConditionException);
                } else {
                    for (var section :
                            findsNonMatch) {
                        mapWrapListQuestBySection.remove(section);
                        mapWrapListRepeatedQuestBySection.remove(section);
                    }
                }
            } else if (notEnough < 0) {
                if (sections.isEmpty()) {
                    generationConditionException = new GenerationConditionException(Localizer.get("generator.message.warn.1.not-found"));
                    throw new RuntimeException(generationConditionException);
                }
                String res = String.format(Objects.requireNonNull(Localizer.get("generator.message.warn.1.if-find")),
                        prop.getQuantityQTickets(), mapWrapListQuestBySection.keySet(), prop.getQuantityQTickets(),
                        Math.abs(notEnough));
                generationConditionException = new GenerationConditionException(res);
                throw new RuntimeException(generationConditionException);
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
                    String res = String.format(Localizer.get("generator.message.warn.1.if-find2"),prop.getQuantityQTickets(),prop.getQuantityTickets(),                                                                                       entryQuantityNotEnough.stream()
                            .map(e -> e.getKey() + " => в количестве: " + e.getValue())
                            .collect(Collectors.joining("\n")),QuestionExt.MIN_VALUE_REPEAT,QuestionExt.MIN_VALUE_REPEAT);
                    generationConditionException = new NumberQuestionsRequireException(res);
                    throw new RuntimeException(generationConditionException);
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
                int lev = rangeQuest.get(i++);
                listQ.forEach(q -> q.setLevel(lev)); // set level question
                mapWrapListQuestGroupByLevel.put(lev, listQ);
            }

            mapWrapListQuestRepeatedGroupByLevel = new LinkedHashMap<>(prop.getQuantityQTickets());
            i = 0;
            for (var entry :
                    mapWrapListRepeatedQuestBySection.entrySet()) {
                int lev = rangeQuest.get(i++);
                WrapperList<Question2> listQ = entry.getValue();
                listQ.forEach(q -> q.setLevel(lev));
                mapWrapListQuestRepeatedGroupByLevel.put(lev, listQ);
            }
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof GenerationConditionException generationConditionException1) {
                mapWrapListRepeatedQuestBySection.values().forEach(listQuest -> {
                    listQuest.forEach(q -> q.setRepeat(q.getRepeat() + 1));
                });
                throw generationConditionException1;
            } else {
                throw ex;
            }
        }

    }

    @Override
    public List<Ticket<Question2>> generate(Ticket<Question2> templateTicket, List<Question2> questions, GenerationProperty property) {
        return super.generate(templateTicket, questions, property);
    }
}
