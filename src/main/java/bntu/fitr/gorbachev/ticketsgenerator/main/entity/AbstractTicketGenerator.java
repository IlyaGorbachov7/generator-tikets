package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.NumberQuestionsRequireException;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractContentExtractThread;
import org.apache.poi.xwpf.usermodel.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractOutputContentThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
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
public abstract class AbstractTicketGenerator<Q extends QuestionExt, T extends Ticket<Q>>
        implements Callable<List<Q>> {

    private ExecutorService poolThreads;

    private Future<List<Q>> futureTaskExtractContent;
    private Future<XWPFDocument> futureTaskManagerThread;

    private File[] filesRsc;
    private T templateTicket;

    private XWPFDocument docxDec;

    protected List<T> listTicket;

    {
        poolThreads = Executors.newFixedThreadPool(3);
    }

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
            poolThreads.shutdownNow();
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
            poolThreads.shutdownNow();
            throw new NullPointerException("Initialization attributes is needed condition");
        }
        this.filesRsc = filesRsc;
        this.templateTicket = templateTicket;
        // run start extraction threadS if is false, else
        if (!isLazyStartExtractor) this.runStartExtractorThreads();
    }

    /**
     * @return class object {@link XWPFDocument} or {@code null} if you don't invoke {@link #startGenerate(int, int, boolean)}
     */
    public XWPFDocument getDocxDec() throws IllegalArgumentException, NumberQuestionsRequireException,
            ExecutionException, InterruptedException {
        docxDec = futureTaskManagerThread.get();
        return docxDec;
    }

    /**
     * @return tickets list or {@code null} if you don't invoke {@link #startGenerate(int, int, boolean)}
     */
    public List<T> getListTicket() throws IllegalArgumentException, NumberQuestionsRequireException,
            ExecutionException, InterruptedException {
        futureTaskManagerThread.get();
        return listTicket;
    }

    public AbstractTicketGenerator<Q, T> setFilesRsc(File[] filesRsc) {
        this.filesRsc = filesRsc;
        return this;
    }

    public AbstractTicketGenerator<Q, T> setTemplateTicket(T templateTicket) {
        this.templateTicket = templateTicket;
        return this;
    }


    /**
     * This method execute starting <b>this thread</b>, which in further will be starting
     * <i>Extraction Threads</i>
     * <p>
     * The instant invocation of this method is controlled by attribute isLazyStartExtractor in constructor
     * <p>
     * Method will be invoked or inside one the once constructors, otherwise {@link #startGenerate(int, int, boolean)}
     */
    private void runStartExtractorThreads() {
        System.out.println("submint EXTRACT contnet");
        this.futureTaskExtractContent = poolThreads.submit(this);
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

                    System.out.println(filesRsc[i].getName() + "------------------------");

                    // thread launch
                    AbstractContentExtractThread<Q> extractor = factoryExtractor(docxRsc[i], filesRsc[i].getName())
                            .get();
                    futures.add(executor.submit(extractor));

                }

                for (Future<List<Q>> futureTask : futures) {
                    generalList.addAll(futureTask.get());
                }

            } catch (ExecutionException e) {
                poolThreads.shutdownNow();
                throw new IllegalArgumentException(e.getCause().getMessage());
            } catch (InterruptedException ignored) {
                poolThreads.shutdownNow();
            } finally {
                // closing pool bntu.fitr.gorbachev.ticketsgenerator.main.threads and cancel all executing tasks and tasks in the queue
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
     * @param quantityTickets  quantity tickets
     * @param quantityQTickets quantity questions inside ticket
     * @param uniqueQTickets   provide unique questions into each ticket
     * @throws NumberQuestionsRequireException in case if current questions not enough
     * @throws IllegalArgumentException        in case if value argument is illegal
     * @throws ExecutionException              in case any trouble inside flow
     */
    public final void startGenerate(int quantityTickets, int quantityQTickets, boolean uniqueQTickets) throws ExecutionException {
        try {
            System.out.println("Main sleep");
            Thread.sleep(20000);
            System.out.println("Main exit from sleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (poolThreads.isShutdown()) {
            try {
                futureTaskManagerThread.get();
            } catch (Exception e) {
                throw new ExecutionException("Generation tickets was stopped!", e.getCause());
            }
        }
        this.futureTaskManagerThread = poolThreads.submit(
                this.new ManagerFlowsTicketGenerator(quantityTickets, quantityQTickets, uniqueQTickets));
        System.out.println("submit ManagerFlows");
    }

    /**
     * Writes contents to a file
     *
     * @throws IOException in case reading files
     */
    public void writeOutputFile(File fileDes) throws IOException, IllegalArgumentException,
            NumberQuestionsRequireException, ExecutionException, InterruptedException {
        docxDec = futureTaskManagerThread.get();
        if (docxDec == null) return;
        try (FileOutputStream outputThread = new FileOutputStream(fileDes)) {
            docxDec.write(outputThread);
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
     * @param tempTicket              template ticket
     * @param listQuestions           list questions
     * @param quantityTickets         quantity tickets
     * @param quantityQuestionsTicket quantity questions into one Ticket
     * @return a list of tickets
     */
    protected abstract List<T> createListTickets(T tempTicket, List<Q> listQuestions,
                                                 final int quantityTickets,
                                                 final int quantityQuestionsTicket);

    /**
     * This method represented yourself as <i>factory</i> to implementation {@code AbstractOutputContentThread}
     *
     * @param listTickets list tickets
     * @return supplier class, that supply a class realization abstract {@link AbstractOutputContentThread}
     */
    protected abstract Supplier<AbstractOutputContentThread<T>> factoryOutputContent(List<T> listTickets);

    private final class ManagerFlowsTicketGenerator implements Callable<XWPFDocument> {
        private final int quantityTickets;
        private final int quantityQTickets;
        private final boolean uniqueQTickets;

        public ManagerFlowsTicketGenerator(int quantityTickets, int quantityQTickets, boolean uniqueQTickets) {
            this.quantityTickets = quantityTickets;
            this.quantityQTickets = quantityQTickets;
            this.uniqueQTickets = uniqueQTickets;
        }

        @Override
        public XWPFDocument call()
                throws IllegalArgumentException, NumberQuestionsRequireException,
                ExecutionException, InterruptedException {
            // Throw Exception if incorrect entered parameters method
            if (quantityTickets <= 0) {
                throw new IllegalArgumentException("Incorrect quality entered tickets");
            } else if (quantityQTickets <= 0) {
                throw new IllegalArgumentException("insufficient number of questions to ensure " +
                                                   "\nthat questions are not repeated in stupid tickets.");
            } else if (filesRsc == null || templateTicket == null) {
                throw new IllegalArgumentException("Was invoked constructor without parameters." +
                                                   "You need to initialize attributes: filesRsc, templateTicket " +
                                                   "through methods setter");
            }

            // run staring thread for extract content from docx file
            if (futureTaskExtractContent == null) AbstractTicketGenerator.this.runStartExtractorThreads();

            List<Q> listQuestions;
            try {
                listQuestions = futureTaskExtractContent.get(); // await answer
            } catch (InterruptedException e) { // in case interrupted thread
//                futureTaskExtractContent.cancel(true); // then also interrupt extract-thread
                poolThreads.shutdownNow();
                throw new InterruptedException(e.getMessage()); // throw this exception one level higher
            }

            // throw exception if insufficient quantity questions
            int quantityQuestions = listQuestions.size();
            if (uniqueQTickets && quantityTickets * quantityQTickets > (quantityQuestions)) {
                poolThreads.shutdownNow();
                throw new NumberQuestionsRequireException("Insufficient number of questions ("
                                                          + quantityQuestions + ") to " +
                                                          "\nensure no repetition of questions in tickets");
            }

            listTicket = createListTickets(templateTicket, listQuestions,
                    quantityTickets, quantityQTickets);

            // lunch output content formation thread
            AbstractOutputContentThread<T> threadWriteTickets = factoryOutputContent(listTicket).get();
            Future<XWPFDocument> futureTaskOutputContent = poolThreads.submit(threadWriteTickets);
            poolThreads.shutdown();
            try {
                docxDec = futureTaskOutputContent.get(); // await answer
            } catch (InterruptedException e) {
                futureTaskOutputContent.cancel(true);
                throw new InterruptedException(e.getMessage()); // throw this exception one level higher
            }
            return docxDec;
        }
    }
}
