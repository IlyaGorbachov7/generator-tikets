package bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.AbstractTicketGenerator;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.GenerationProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender.SenderMessage;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender.SenderMsgFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.AbstractContentExtractThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.AbstractOutputContentThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.impl.ContentExtractor;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.impl.OutputContentWriter;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.loc.Localizer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;

@Log4j2
public class TicketGeneratorImpl extends AbstractTicketGenerator<Question2, Ticket<Question2>> {
    /**
     * This pointer will be initialized only on the time {@link #conditionsStartGeneration(List, GenerationProperty)}
     */
    protected GenerationPropertyImpl property;

    @Getter
    private SenderMessage registrarSenderMsg = SenderMsgFactory.getInstance().getSingleSenderMsg();

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

    public TicketGeneratorImpl(boolean isLazyStartExtractor, File[] filesRsc, Ticket<Question2> templateTicket, SenderMessage registrarSenderMsg) {
        super();
        this.registrarSenderMsg = registrarSenderMsg;
        setConstParam(filesRsc, templateTicket);
        if (!isLazyStartExtractor) this.runStartExtractorThreads();
    }

    /**
     *
     * @throws bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender.SenderStopSleepException
     */
    @Override
    protected Supplier<AbstractContentExtractThread<Question2>> factoryExtractor(XWPFDocument p, String url) {
        log.debug("Created factory extractor by: url= {}", url);
        registrarSenderMsg.sendMsg(Localizer.getWithValues("panel.main.message.registrator.file.extract", url));
        return () -> new ContentExtractor(p, url);
    }

    /**
     *
     * @throws bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender.SenderStopSleepException
     */
    @Override
    protected Supplier<AbstractOutputContentThread<Ticket<Question2>>> factoryOutputContent(List<Ticket<Question2>> listTickets) {
        log.debug("Created factory output content");
        registrarSenderMsg.sendMsg(Localizer.get("panel.main.message.registrator.file.combine"));
        return () -> new OutputContentWriter(listTickets, property.getWriterTicketProperty());
    }

    /**
     *
     * @throws bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender.SenderStopSleepException
     */
    @Override
    protected void conditionsStartGeneration(List<Question2> questions, GenerationProperty property)
            throws GenerationConditionException {
        log.info("Check conditions start generation by type generation");
        registrarSenderMsg.sendMsg(Localizer.get("panel.main.message.registrator.check-conditions"));
        GenerationPropertyImpl prop = (GenerationPropertyImpl) property;
        this.property = prop;
        TicketGeneratorManager.getGenerator(prop.getGenerationWay()).conditionGeneration(questions, property);
    }

    /**
     *
     * @throws bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.sender.SenderStopSleepException
     */
    @Override
    protected List<Ticket<Question2>> createListTickets(Ticket<Question2> templateTicket, List<Question2> questions,
                                                        GenerationProperty property) {
        GenerationPropertyImpl prop = (GenerationPropertyImpl) property;
        registrarSenderMsg.sendMsg(Localizer.get("panel.main.message.registrator.list-tickets"));
        return TicketGeneratorManager.getGenerator(prop.getGenerationWay()).generate(templateTicket, questions, property);
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor
    public static class Builder {

        protected SenderMessage senderMessage;

        public Builder senderMsg(SenderMessage senderMessage) {
            this.senderMessage = senderMessage;
            return this;
        }

        public TicketGeneratorImpl build(boolean isLazyStartExtractor, File[] filesRsc, Ticket<Question2> templateTicket) {
            return (senderMessage != null) ?
                    new TicketGeneratorImpl(isLazyStartExtractor, filesRsc, templateTicket, senderMessage) :
                    new TicketGeneratorImpl(isLazyStartExtractor, filesRsc, templateTicket);
        }

        public TicketGeneratorImpl build(File[] filesRsc, Ticket<Question2> templateTicket) {
            return build(true, filesRsc, templateTicket);
        }
    }
}
