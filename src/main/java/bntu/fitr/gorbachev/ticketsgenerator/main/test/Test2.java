package bntu.fitr.gorbachev.ticketsgenerator.main.test;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.TicketGeneratorImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.impl.TicketsGeneratorWayImpl3;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.NumberQuestionsRequireException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Test2 {
    static File[] paths = List.of(
                    new File("C:\\Users\\SecuRiTy\\Documents\\БЗиППР, вопросы 2021 - Copy (3).docx"))
            .toArray(File[]::new);

    static Ticket<Question2> ticket = new Ticket<Question2>("", "", "",
            "", "", "", "", Ticket.SessionType.AUTUMN, "",
            "", 23);

    static {
        System.out.println(Test2.class.getName() + "is Loading vio classLouder: " + Test2.class.getClassLoader());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, NumberQuestionsRequireException, GenerationConditionException, IOException {

        TicketGeneratorImpl ticketGenerator = new TicketGeneratorImpl(false, paths, ticket);

        var prop = new GenerationPropertyImpl(30, 3, false);
        prop.setGenerationWay(TicketsGeneratorWayImpl3.class);
        prop.setFlagRandomOrderReading(true);
        prop.setFlagRandomOrderQuestInTicket(false);

        ticketGenerator.startGenerate(prop);

        ticketGenerator.getListTicket().forEach(System.out::println);
        ticketGenerator.writeOutputFile(new File("C:\\Users\\SecuRiTy\\Documents\\res.docx"));
//        System.out.println("1");
//        List<?> list = ticketGenerator.getListQuestions();
//        List<?> listTickets = ticketGenerator.getListTicket();
//        System.out.println("2");

//        list.forEach(System.out::println);

//        ticketGenerator.startGenerate(220, 3, true);

//        for (var ticket : ticketGenerator.getListTicket()) {
//            System.out.println(ticket);
//        }

    }
}