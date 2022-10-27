package bntu.fitr.gorbachev.ticketsgenerator.main.threads.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractOutputContentThread;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.List;

public class OutputContentWriter extends AbstractOutputContentThread<Ticket<Question2>> {

    public OutputContentWriter() {
    }

    /**
     * Constructor without parameters
     *
     * @param listTickets list tickets
     */
    public OutputContentWriter(List<Ticket<Question2>> listTickets) {
        super(listTickets);
    }

    @Override
    public List<Ticket<Question2>> getListTickets() {
        return super.getListTickets();
    }

    @Override
    public XWPFDocument call() throws Exception {
        return super.call();
    }
}
