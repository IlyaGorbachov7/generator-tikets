package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractContentExtractThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractOutputContentThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.impl.ContentExtractor;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.impl.OutputContentWriter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;

public class TicketGeneratorImpl extends AbstractTicketGenerator<Question2, Ticket<Question2>> {
    /**
     * This pointer will be initialized only on the time {@link #conditionsStartGeneration(List, GenerationProperty)}
     */
    protected GenerationPropertyImpl property;

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

        return () -> new OutputContentWriter(listTickets, property.getWriterTicketProperty());
    }

    @Override
    protected void conditionsStartGeneration(List<Question2> questions, GenerationProperty property)
            throws GenerationConditionException {
        GenerationPropertyImpl prop = (GenerationPropertyImpl) property;
        this.property = prop;
        TicketGeneratorManager.getGenerator(prop.getGenerationWay()).conditionGeneration(questions, property);
    }

    @Override
    protected List<Ticket<Question2>> createListTickets(Ticket<Question2> templateTicket, List<Question2> questions,
                                                        GenerationProperty property) {
        GenerationPropertyImpl prop = (GenerationPropertyImpl) property;
        return TicketGeneratorManager.getGenerator(prop.getGenerationWay()).generate(templateTicket, questions, property);
    }
}
