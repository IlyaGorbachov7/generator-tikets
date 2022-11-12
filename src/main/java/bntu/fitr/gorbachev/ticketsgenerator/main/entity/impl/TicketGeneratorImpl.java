package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorManager;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.impl.TicketsGeneratorWayImpl1;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.impl.TicketsGeneratorWayImpl2;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.NumberQuestionsRequireException;
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
    protected Supplier<AbstractContentExtractThread<Question2>> factoryExtractor(XWPFDocument p, String url) {
        return () -> new ContentExtractor(p, url);
    }

    @Override
    protected Supplier<AbstractOutputContentThread<Ticket<Question2>>> factoryOutputContent(List<Ticket<Question2>> listTickets) {
        return () -> new OutputContentWriter(listTickets);
    }

    @Override
    protected void conditionsStartGeneration(List<Question2> questions, GenerationProperty property)
            throws GenerationConditionException {
        GenerationPropertyImpl generationPropertyImpl = (GenerationPropertyImpl) property;

        if (generationPropertyImpl.getGenerationMode() == GenerationMode.MODE_1) {
            TicketsGeneratorManager.getGenerator(TicketsGeneratorWayImpl1.class)
                    .conditionGeneration(questions, property);
        } else if (generationPropertyImpl.getGenerationMode() == GenerationMode.MODE_2) {
            TicketsGeneratorManager.getGenerator(TicketsGeneratorWayImpl2.class)
                    .conditionGeneration(questions, property);
        }
    }

    @Override
    protected List<Ticket<Question2>> createListTickets(Ticket<Question2> templateTicket, List<Question2> questions,
                                                        GenerationProperty property) {
        GenerationPropertyImpl generationPropertyImpl = (GenerationPropertyImpl) property;

        if (generationPropertyImpl.getGenerationMode() == GenerationMode.MODE_1) {
            TicketsGeneratorManager.getGenerator(TicketsGeneratorWayImpl1.class)
                    .generate(templateTicket, questions, property);
        } else if (generationPropertyImpl.getGenerationMode() == GenerationMode.MODE_2) {
            TicketsGeneratorManager.getGenerator(TicketsGeneratorWayImpl2.class)
                    .generate(templateTicket, questions, property);
        }
        return null;
    }
}
