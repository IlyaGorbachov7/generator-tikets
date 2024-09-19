package bntu.fitr.gorbachev.ticketsgenerator.main.basis;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.AbstractContentExtractThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.threads.AbstractOutputContentThread;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Loggers;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Class is a representation of ticket generator.
 * From several file with docx extension where questions contained
 * extract contented after that creating new file is a representation
 * generated document with tickets
 *
 * @author Gorbachev I. D.
 * @version 12.03.2022
 */
@Log4j2
public abstract class AbstractTicketGenerator<Q extends QuestionExt, T extends Ticket<Q>>
        implements Callable<List<Q>> {
    private Future<List<Q>> futureTaskExtractContent;

    protected File[] filesRsc;
    private XWPFDocument docxDec;

    protected T templateTicket;
    protected List<T> listTicket;
    /**
     * Generation property
     */
    GenerationProperty generationProperty;

    public AbstractTicketGenerator() {
    }

    /**
     * This constructor will be start this thread designed for extract contents from documents
     *
     * @param filesRsc array paths of files resources
     * @see #call()
     */
    public AbstractTicketGenerator(File[] filesRsc, T templateTicket) {
        if (filesRsc == null || templateTicket == null) {
            throw new NullPointerException("Initialization attributes is needed condition");
        }
        this.filesRsc = filesRsc;
        this.templateTicket = templateTicket;
        // by default run start extracting threads
        this.runStartExtractorThreads();
    }

    /**
     * @param isLazyStartExtractor This attribute is indicator for lazy starting extraction-threads.
     *                             <p>
     *                             <I>{@code Lazy}</I> is means, that execution doesn't happen instantly, but only if necessary this system API
     *                             that means, that this method will be invoked in anything case, when this will be needed <b>{@code even if
     *                             value == false}</b>
     *                             <p>
     *                             <b>By default</b> value this attribute is {@code false}, then running starting this thread inside constructor.
     *                             <p>
     *                             <b>Otherwise provided constructor with this attribute. where may specify value</b>
     * @param filesRsc             - array paths of files resources
     * @param templateTicket
     * @see #call()  call() - is thread, that starting ALL extraction threads
     */
    public AbstractTicketGenerator(boolean isLazyStartExtractor, File[] filesRsc, T templateTicket) {
        if (filesRsc == null || templateTicket == null) {
            throw new NullPointerException("Initialization attributes is needed condition");
        }
        this.filesRsc = filesRsc;
        this.templateTicket = templateTicket;
        // run start extraction threadS if is false, else
        if (!isLazyStartExtractor) this.runStartExtractorThreads();
    }

    /**
     * @return class object {@link XWPFDocument} or {@code null} if you don't invoke {@link #startGenerate(GenerationProperty)}
     */
    public XWPFDocument getDocxDec() {
        return docxDec;
    }

    /**
     * @return tickets list or {@code null} if you don't invoke {@link #startGenerate(GenerationProperty)}
     */
    public List<T> getListTicket() {
        return listTicket;
    }

    /**
     * This setter can be called if ticket generator was created through <i> constructor without parameters</i>
     */
    public AbstractTicketGenerator<Q, T> setConstParam(File[] filesRsc, T templateTicket) {
        if (filesRsc == null || templateTicket == null) {
            throw new NullPointerException("Initialization attributes is needed condition");
        }
        this.templateTicket = templateTicket;
        this.filesRsc = filesRsc;
        return this;
    }

    /**
     * Getter return list questions, which was extracted from files
     *
     * @return list questions
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<Q> getListQuestions() throws ExecutionException, InterruptedException {
        if (futureTaskExtractContent == null) { // this notifies will be is as reason futureTask == null
            throw new ExecutionException("Apparently, the ticket generator was been created with parameter:" +
                                         "isLazyStartExtractor == true",
                    new NullPointerException("futureTaskExtractorContent == null"));
        }
        return futureTaskExtractContent.get();
    }

    /**
     * Writes contents to a file
     *
     * @throws IOException in case reading files
     */
    public void writeOutputFile(File fileDes) throws IOException {
        if (docxDec == null) return;
        try (FileOutputStream outputThread = new FileOutputStream(fileDes)) {
            docxDec.write(outputThread);
        }
    }


    /**
     * This method execute starting <b>this thread</b>, which in further will be starting
     * <i>Extraction Threads</i>
     * <p>
     * The instant invocation of this method is controlled by attribute isLazyStartExtractor in constructor
     * <p>
     * Method will be invoked or inside one the once constructors, otherwise {@link #startGenerate(GenerationProperty)}
     */
    private void runStartExtractorThreads() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        this.futureTaskExtractContent = executorService.submit(this);
        executorService.shutdown();
    }

    /**
     * This method will be used class implementing {@link AbstractContentExtractThread} <b>for extracting content from files-resources</b>
     * <p>
     * Such class supply from factory method: {@link #factoryExtractor(XWPFDocument, String)}
     *
     * @throws Exception general exception in case any troubles inside thread
     */
    @Override
    public List<Q> call() throws Exception {
        {
            List<Q> generalList = new ArrayList<>();
            FileInputStream[] inputTreads = new FileInputStream[filesRsc.length];
            XWPFDocument[] docxRsc = new XWPFDocument[filesRsc.length];
            ExecutorService executor = Executors.newFixedThreadPool(filesRsc.length);
            List<Future<List<Q>>> futures = new ArrayList<>(filesRsc.length);

            try {
                for (int i = 0; i < inputTreads.length; ++i) {
                    inputTreads[i] = new FileInputStream(filesRsc[i]);
                    docxRsc[i] = new XWPFDocument(inputTreads[i]);
                    log.debug("Read file: {}",filesRsc[i]);
                    // thread launch
                    AbstractContentExtractThread<Q> extractor = factoryExtractor(docxRsc[i], filesRsc[i].getName())
                            .get();
                    futures.add(executor.submit(extractor));

                }

                for (Future<List<Q>> futureTask : futures) {
                    generalList.addAll(futureTask.get());
                }

            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                log.warn(cause.getMessage());
                throw new ExecutionException(cause.getMessage(), null);
            } catch (InterruptedException ignored) {
            } finally {
                // closing pool bntu.fitr.gorbachev.ticketsgenerator.main.model.threads and cancel all executing tasks and tasks in the queue
                executor.shutdownNow();
                for (var thread : inputTreads) { // closing files
                    if (thread != null) {
                        thread.close();
                    }
                }
            }
            return generalList;
        }
    }

    /**
     * Generate file docx where containing tickets
     * <p>
     * This method will be entered in  mode block waiting answer from classes {@link #futureTaskExtractContent}
     * (it is this class with method {@link #call()}) and
     * {@link #factoryOutputContent(List)}
     * <p>
     * {@code This method is finaly, that is means, that his cannot  inherit}
     *
     * @param generationProperty@throws NumberQuestionsRequireException in case if current questions not enough
     * @throws IllegalArgumentException in case if value argument is illegal
     * @throws ExecutionException       in case any trouble inside flow
     */
    public final void startGenerate(GenerationProperty generationProperty)
            throws GenerationConditionException, ExecutionException, InterruptedException {
        log.info("Start generate Tickets");
        this.generationProperty = generationProperty;
        checkedNecessarilyConditions();

        // run staring thread for extract content from docx file
        if (futureTaskExtractContent == null) this.runStartExtractorThreads();

        List<Q> listQuestions;
        try {
            listQuestions = futureTaskExtractContent.get(); // await answer
        } catch (InterruptedException e) { // in case interrupted thread
            futureTaskExtractContent.cancel(true); // then also interrupt extract-thread
            throw new InterruptedException(e.getMessage()); // throw this exception one level higher
        }

        conditionsStartGeneration(listQuestions, this.generationProperty);

        listTicket = createListTickets(templateTicket, listQuestions, this.generationProperty);

        // lunch output content formation thread
        AbstractOutputContentThread<T> threadWriteTickets = factoryOutputContent(listTicket).get();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<XWPFDocument> futureTaskOutputContent = executorService.submit(threadWriteTickets);
        executorService.shutdown();
        try {
            docxDec = futureTaskOutputContent.get(); // await answer
        } catch (InterruptedException e) {
            futureTaskOutputContent.cancel(true);
            throw new InterruptedException(e.getMessage()); // throw this exception one level higher
        }
    }

    /**
     * This method represented yourself as <i>factory </i> to implementation {@code AbstractContentExtractorThread}
     *
     * @param p   document
     * @param url path to document
     * @return supplier class, that supply a class realization abstract {@link AbstractContentExtractThread}
     */
    protected abstract Supplier<AbstractContentExtractThread<Q>> factoryExtractor(XWPFDocument p, String url);

    /**
     * This method is <i>abstract</i> with goals allow consumer opportunity yourself realize this method for creation
     * list tickets.
     *
     * @param templateTicket template ticket
     * @param questions  list questions
     * @param property
     * @return a list of tickets
     */
    protected abstract List<T> createListTickets(T templateTicket, List<Q> questions, GenerationProperty property);

    /**
     * This method represented yourself as <i>factory</i> to implementation {@code AbstractOutputContentThread}
     *
     * @param listTickets list tickets
     * @return supplier class, that supply a class realization abstract {@link AbstractOutputContentThread}
     */
    protected abstract Supplier<AbstractOutputContentThread<T>> factoryOutputContent(List<T> listTickets);

    /**
     * This method is place, where checked necessarily basic conditions for further generation tickets
     * <p>
     * The following points relate to the basic check:
     * <p>
     * 1) quantity tickets > 0;
     * <p>
     * 2) quantity questions inside ticket > 0;
     * <p>
     * 3) generation property != null;
     * <p>
     * 4) fileRsc and templateTicket != null
     *
     * @throws GenerationConditionException in case failure
     */
    private void checkedNecessarilyConditions() throws GenerationConditionException {
        // Throw Exception if incorrect entered parameters method
        log.info("Check necessarily conditions");
        if (generationProperty == null) {
            throw new GenerationConditionException(new NullPointerException("Your need initialize generation property"));
        } else if (generationProperty.getQuantityTickets() <= 0) {
            throw new GenerationConditionException("Incorrect quality entered tickets");
        } else if (generationProperty.getQuantityQTickets() <= 0) {
            throw new GenerationConditionException("insufficient number of questions to ensure " +
                                                   "\nthat questions are not repeated in stupid tickets.");
        } else if (filesRsc == null || templateTicket == null) {
            throw new GenerationConditionException("Was invoked constructor without parameters." +
                                                   "You need to initialize attributes: filesRsc, templateTicket " +
                                                   "through methods setter");
        }
    }

    /**
     * This method is place, where should be checked all conditions generation before her starting.
     *
     * @param questions              list questions
     * @param generationProperty generation property, which contains defined data needed for generation
     * @throws GenerationConditionException in case failure
     * @apiNote In case any fail in conditions generate tickets, you should throw exception, that will be describing
     * the reason of the exception.
     */
    protected abstract void conditionsStartGeneration(List<Q> questions, GenerationProperty generationProperty)
            throws GenerationConditionException;

}
