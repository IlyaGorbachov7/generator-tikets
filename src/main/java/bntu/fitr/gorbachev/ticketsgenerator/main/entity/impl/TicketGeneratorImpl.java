package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.AbstractTicketGenerator;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractContentExtractThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.impl.ContentExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TicketGeneratorImpl extends AbstractTicketGenerator<Question2> {
    /**
     * @param filesRsc         array paths of files resources
     * @param templateTicket
     */
    public TicketGeneratorImpl(File[] filesRsc, Ticket templateTicket) {
        super(filesRsc, templateTicket);
    }

    @Override
    protected AbstractContentExtractThread<Question2> factoryExtractor(XWPFDocument p, String url) {
        return new ContentExtractor(p,url);
    }


    @Override
    protected List<Ticket> createListTickets(Ticket tempTicket, Map<String, List<Question2>> mapQuestions,
                                             int quantityTickets, int quantityQuestionsTicket) {
        List<Ticket> listTickets = new ArrayList<>(quantityTickets);
        List<List<Question2>> listsQ = new ArrayList<>(mapQuestions.values());
        if (mapQuestions.isEmpty()) {
            return listTickets;
        }
        int[] arrCurPosList = new int[listsQ.size()]; // current positions of each list
        for (int indexTicket = 0; indexTicket < quantityTickets; ++indexTicket) {
            Ticket ticket = new Ticket(tempTicket.getInstitute(), tempTicket.getFaculty(), tempTicket.getDepartment(),
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
