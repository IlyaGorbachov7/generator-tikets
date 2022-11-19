package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.NumberQuestionsRequireException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TicketsGeneratorWayImpl1 implements TicketsGeneratorWay<Question2, Ticket<Question2>> {

    @Override
    public void conditionGeneration(List<Question2> questions, GenerationProperty property) throws GenerationConditionException {
        // throw exception if insufficient quantity questions
        int quantityQuestions = questions.size();
        if (property.isUnique() &&
            property.getQuantityTickets() * property.getQuantityQTickets() > (quantityQuestions)) {
            throw new GenerationConditionException(new NumberQuestionsRequireException("Insufficient number of questions ("
                                                                                       + quantityQuestions + ") to " +
                                                                                       "\nensure no repetition of questions in tickets"));
        }
    }

    @Override
    public List<Ticket<Question2>> generate(Ticket<Question2> templateTicket, List<Question2> questions, GenerationProperty property) {
        Map<String, List<Question2>> mapQuestions = questions.stream()
                .collect(Collectors.groupingBy(Question2::getSection, LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));

        mapQuestions.forEach((k, v) -> {
            System.out.println("=========== k: " + k + " : ===== : size : " + v.size());
            for (var e : v) {
                System.out.println(e);
            }
        });

        // убрать жосткую привязку
        int quantityTickets = property.getQuantityTickets();
        int quantityQuestionsTicket = property.getQuantityQTickets();
        List<Ticket<Question2>> listTickets = new ArrayList<>(quantityTickets);
        List<List<Question2>> listsQ = new ArrayList<>(mapQuestions.values());
        if (mapQuestions.isEmpty()) {
            return listTickets;
        }
        int[] arrCurPosList = new int[listsQ.size()]; // current positions of each list
        for (int indexTicket = 0; indexTicket < quantityTickets; ++indexTicket) {
            Ticket<Question2> ticket = Ticket.of(templateTicket.getInstitute(), templateTicket.getFaculty(), templateTicket.getDepartment(),
                    templateTicket.getSpecialization(), templateTicket.getDiscipline(), templateTicket.getTeacher(),
                    templateTicket.getHeadDepartment(), templateTicket.getType(), templateTicket.getDate(),
                    templateTicket.getProtocolNumber(), quantityQuestionsTicket);
            int curIdListQ = 0;

            for (int indexQuestion = 0; indexQuestion < quantityQuestionsTicket; ++indexQuestion) {
                //------------------------
                if (curIdListQ == listsQ.size()) {
                    curIdListQ = 0;
                }

                int countFilledList = 0;
                while (curIdListQ < listsQ.size()) {
                    if ((arrCurPosList[curIdListQ] < listsQ.get(curIdListQ).size())) {
                        countFilledList = 0;
                        break;
                    } else {
                        ++curIdListQ;
                        if (curIdListQ == listsQ.size()) {
                            curIdListQ = 0;
                            // But if also each lists is filled, then start from the beginning
                            if (countFilledList >= listsQ.size()) {
                                arrCurPosList = new int[listsQ.size()];
                                countFilledList = 0;
                            }
                        }
                        ++countFilledList;
                    }
                }
                //------------------------

                List<Question2> listQ = listsQ.get(curIdListQ);
                ticket.add(listQ.get(arrCurPosList[curIdListQ]));
                arrCurPosList[curIdListQ]++;
                curIdListQ++;
            }
            listTickets.add(ticket);
        }

        return listTickets;

    }
}
