package bntu.fitr.gorbachev.ticketsgenerator.main.test;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.TicketGeneratorImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.NumberQuestionsRequireException;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Test2 {
    static File[] paths = List.of(
                    new File("D:\\1)1-4 курс Универ\\3 курс\\2) Программирование на Java\\Задания к Курсовой работе\\Questions.docx"),
                    new File("D:\\1)1-4 курс Универ\\3 курс\\2) Программирование на Java\\Задания к Курсовой работе\\Voprosy_PSP.docx"))
            .toArray(File[]::new);

    static Ticket<Question2> ticket = new Ticket<Question2>("", "", "",
            "", "", "", "", Ticket.SessionType.AUTUMN, "",
            "", 23);

    static {
        System.out.println(Test2.class.getName() + "is Loading vio classLouder: " + Test2.class.getClassLoader());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, NumberQuestionsRequireException, GenerationConditionException {

        TicketGeneratorImpl ticketGenerator = new TicketGeneratorImpl(false, paths, ticket);

        ticketGenerator.startGenerate(new GenerationProperty(30, 3, false));

        ticketGenerator.getListTicket().forEach(System.out::println);
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