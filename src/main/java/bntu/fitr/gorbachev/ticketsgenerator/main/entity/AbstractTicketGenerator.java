package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.NumberQuestionsRequireException;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.AbstractContentExtractThread;
import org.apache.poi.xwpf.usermodel.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.OutputContentFormationThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Class is a representation of ticket generator.
 * From several file with docx extension where questions contained
 * extract contented after that creating new file is a representation
 * generated document with tickets
 *
 * @author Gorbachev I. D.
 * @version 12.03.2022
 */
public abstract class AbstractTicketGenerator<T extends QuestionExt> implements Callable<List<T>> {

    protected final Future<List<T>> futureTaskExtractContent;

    protected final File[] filesRsc;
    protected final Ticket templateTicket;

    private XWPFDocument docxDec;

    protected List<Ticket> listTicket;

    /**
     * @param filesRsc array paths of files resources
     */
    public AbstractTicketGenerator(File[] filesRsc, Ticket templateTicket) {
        this.filesRsc = filesRsc;
        this.templateTicket = templateTicket;
        // launch external thread
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        this.futureTaskExtractContent = executorService.submit(this);
        executorService.shutdown();
    }

    /**
     * @return class object {@link XWPFDocument} or {@code null} if you don't invoke {@link #startGenerate(int, int, boolean)}
     */
    public XWPFDocument getDocxDec() {
        return docxDec;
    }

    /**
     * @return tickets list or {@code null} if you don't invoke {@link #startGenerate(int, int, boolean)}
     */
    public List<Ticket> getListTicket() {
        return listTicket;
    }

    /**
     * Method for extracted contents from files-resources
     * <p>
     * 23.04.2022
     * I had to think carefully about the question: If a thread is interrupted, are all resources and bntu.fitr.gorbachev.ticketsgenerator.main.threads closed correctly?
     * As a result, everything closes well, in the case of elsi, the stream was interrupted from
     *
     * @throws Exception general exception in case any troubles inside thread
     */
    @Override
    public List<T> call() throws Exception {
        {
            List<T> generalList = new ArrayList<>();
            FileInputStream[] inputTreads = new FileInputStream[filesRsc.length];
            XWPFDocument[] docxRsc = new XWPFDocument[filesRsc.length];
            ExecutorService executor = Executors.newFixedThreadPool(filesRsc.length);
            List<Future<List<T>>> futures = new ArrayList<>(filesRsc.length);

            try {
                for (int i = 0; i < inputTreads.length; ++i) {
                    inputTreads[i] = new FileInputStream(filesRsc[i]);
                    docxRsc[i] = new XWPFDocument(inputTreads[i]);

                    // thread launch
                    AbstractContentExtractThread<T> extractor = factoryExtractor(docxRsc[i], filesRsc[i].getName())
                            .get();
                    futures.add(executor.submit(extractor));

                }

                for (Future<List<T>> futureTask : futures) {
                    generalList.addAll(futureTask.get());
                }

            } catch (ExecutionException e) {
                throw new IllegalArgumentException(e.getCause().getMessage());
            } catch (InterruptedException ignored) {
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

    protected abstract Supplier<AbstractContentExtractThread<T>> factoryExtractor(XWPFDocument p, String url);

    /**
     * Generate file docx where containing tickets
     *
     * @param quantityTickets  quantity tickets
     * @param quantityQTickets quantity questions inside ticket
     * @param uniqueQTickets   provide unique questions into each ticket
     * @throws NumberQuestionsRequireException in case if current questions not enough
     * @throws IllegalArgumentException        in case if value argument is illegal
     * @throws ExecutionException              in case any trouble inside flow
     */
    public void startGenerate(int quantityTickets, int quantityQTickets, boolean uniqueQTickets)
            throws NumberQuestionsRequireException, IllegalArgumentException, ExecutionException, InterruptedException {
        // Throw Exception if incorrect entered parameters method
        if (quantityTickets <= 0) {
            throw new IllegalArgumentException("Incorrect quality entered tickets");
        } else if (quantityQTickets <= 0) {
            throw new IllegalArgumentException("insufficient number of questions to ensure " +
                                               "\nthat questions are not repeated in stupid tickets.");
        }

        List<T> listQuestions;
        try {
            listQuestions = futureTaskExtractContent.get(); // await answer
        } catch (InterruptedException e) { // in case interrupted thread
            futureTaskExtractContent.cancel(true); // then also interrupt extract-thread
            throw new InterruptedException(e.getMessage()); // throw this exception one level higher
        }

        // throw exception if insufficient quantity questions
        int quantityQuestions = listQuestions.size();
        if (uniqueQTickets && quantityTickets * quantityQTickets > (quantityQuestions)) {
            throw new NumberQuestionsRequireException("Insufficient number of questions ("
                                                      + quantityQuestions + ") to " +
                                                      "\nensure no repetition of questions in tickets");
        }

        Map<String, List<T>> mapBySection = listQuestions.stream()
                .collect(Collectors.groupingBy(T::getSection, LinkedHashMap::new,
                        Collectors.toCollection(ArrayList::new)));

        mapBySection.forEach((k, v) -> {
            System.out.println("=========== k: " + k + " : ====================");
            for (T e : v) {
                System.out.println(e);
            }
        });

        listTicket = createListTickets(templateTicket, mapBySection,
                quantityTickets, quantityQTickets);

        // lunch output content formation thread
        OutputContentFormationThread threadWriteTickets = new OutputContentFormationThread(listTicket);
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
     * Create list tickets
     *
     * @param mapQuestions            map questions
     * @param quantityTickets         quantity tickets
     * @param quantityQuestionsTicket quantity questions into one Ticket
     * @return a list of tickets
     */
    protected abstract List<Ticket> createListTickets(Ticket tempTicket, Map<String, List<T>> mapQuestions,
                                                      final int quantityTickets,
                                                      final int quantityQuestionsTicket);
}
