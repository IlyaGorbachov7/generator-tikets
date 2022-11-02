package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.AbstractTicketGenerator;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractContentExtractThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractOutputContentThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.impl.ContentExtractor;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.impl.OutputContentWriter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TicketGeneratorImpl extends AbstractTicketGenerator<Question2, Ticket<Question2>> {

    public TicketGeneratorImpl() {
    }

    /**
     * This constructor will be start this thread designed for extract contents from documents
     *
     * @param filesRsc       array paths of files resources
     * @param templateTicket
     * @see #call()
     */
    public TicketGeneratorImpl(File[] filesRsc, Ticket<Question2> templateTicket) {
        super(filesRsc, templateTicket);
    }

    public TicketGeneratorImpl(boolean isLazyStartExtractor, File[] filesRsc, Ticket<Question2> templateTicket) {
        super(isLazyStartExtractor, filesRsc, templateTicket);
    }

    @Override
    protected void conditionsStartGeneration(List<Question2> question2s, GenerationProperty generationProperty)
            throws GenerationConditionException {
        super.conditionsStartGeneration(question2s, generationProperty);
    }

    @Override
    protected Supplier<AbstractContentExtractThread<Question2>> factoryExtractor(XWPFDocument p, String url) {
        return () -> new ContentExtractor(p, url);
    }

    @Override
    protected Supplier<AbstractOutputContentThread<Ticket<Question2>>> factoryOutputContent(List<Ticket<Question2>> listTickets) {
        return () -> new OutputContentWriter(listTickets);
    }

    @Override
    protected List<Ticket<Question2>> createListTickets(Ticket<Question2> tempTicket, List<Question2> listQuestions,
                                                        GenerationProperty property) {

        Map<String, List<Question2>> mapQuestions = listQuestions.stream()
                .collect(Collectors.groupingBy(Question2::getSection, LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));

        mapQuestions.forEach((k, v) -> {
            System.out.println("=========== k: " + k + " : ====================");
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
            Ticket<Question2> ticket = new Ticket<>(tempTicket.getInstitute(), tempTicket.getFaculty(), tempTicket.getDepartment(),
                    tempTicket.getSpecialization(), tempTicket.getDiscipline(), tempTicket.getTeacher(),
                    tempTicket.getHeadDepartment(), tempTicket.getType(), tempTicket.getDate(),
                    tempTicket.getProtocolNumber(), quantityQuestionsTicket);
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
