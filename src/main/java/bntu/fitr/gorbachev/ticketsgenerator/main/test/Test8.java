package bntu.fitr.gorbachev.ticketsgenerator.main.test;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.TicketGeneratorImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl.TicketsGeneratorWayImpl2;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Test8 {
    static File[] paths = List.of(
            new File("C:\\Users\\SecuRiTy\\Documents\\Questions.docx")).toArray(File[]::new);

    static Ticket<Question2> ticket = new Ticket<Question2>("", "", "",
            "", "", "", "", Ticket.SessionType.AUTUMN, "",
            "", 23);

    public static void main(String[] args) throws GenerationConditionException, ExecutionException, InterruptedException {
        TicketGeneratorImpl ticketGenerator = new TicketGeneratorImpl(paths, ticket);
//        System.out.println("-------------------- list questions ------------------" );
//        ticketGenerator.getListQuestions().forEach(System.out::println);
//
        GenerationProperty property = new GenerationPropertyImpl(30, 3, TicketsGeneratorWayImpl2.class);
        ticketGenerator.startGenerate(property);
    }
}
