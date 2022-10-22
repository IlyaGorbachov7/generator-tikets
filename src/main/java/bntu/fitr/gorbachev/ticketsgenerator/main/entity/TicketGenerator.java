package bntu.fitr.gorbachev.ticketsgenerator.main.entity;

import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.NumberQuestionsRequireException;
import org.apache.poi.xwpf.usermodel.*;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.ContentExtractThread;
import bntu.fitr.gorbachev.ticketsgenerator.main.threads.OutputContentFormationThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Class is a representation of ticket generator.
 * From several file with docx extension where questions contained
 * extract contented after that creating new file is a representation
 * generated document with tickets
 *
 * @author Gorbachev I. D.
 * @version 12.03.2022
 */
public class TicketGenerator implements Callable<Map<String, List<Question>>> {

    private final Future<Map<String, List<Question>>> futureTaskExtractContent;

    private final File[] filesRsc;
    private final Ticket templateTicket;

    private XWPFDocument docxDec;

    private List<Ticket> listTicket;

    /**
     * @param filesRsc array paths of files resources
     */
    public TicketGenerator(File[] filesRsc, Ticket templateTicket) {
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
    public Map<String, List<Question>> call() throws Exception {
        {
            Map<String, List<Question>> mapQuestions = new LinkedHashMap<>();
            FileInputStream[] inputTreads = new FileInputStream[filesRsc.length];
            XWPFDocument[] docxRsc = new XWPFDocument[filesRsc.length];
            ExecutorService executor = Executors.newFixedThreadPool(filesRsc.length);
            List<Future<Map<String, List<Question>>>> futures = new ArrayList<>(filesRsc.length);

            try {
                for (int i = 0; i < inputTreads.length; ++i) {
                    inputTreads[i] = new FileInputStream(filesRsc[i]);
                    docxRsc[i] = new XWPFDocument(inputTreads[i]);

                    // thread launch
                    futures.add(executor.submit(new ContentExtractThread(docxRsc[i], filesRsc[i].getPath())));
                }

                for (var futureTask : futures) {
                    var map = futureTask.get();
                    for (var entry : map.entrySet()) {
                        var listQ = mapQuestions.putIfAbsent(entry.getKey(), entry.getValue());
                        if (listQ != null) {
                            listQ.addAll(entry.getValue());
                        }
                    }
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
            return mapQuestions;
        }
    }

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
            throws NumberQuestionsRequireException, IllegalArgumentException, ExecutionException,
            InterruptedException {
        // Throw Exception if incorrect entered parameters method
        if (quantityTickets <= 0) {
            throw new IllegalArgumentException("Incorrect quality entered tickets");
        } else if (quantityQTickets <= 0) {
            throw new IllegalArgumentException("insufficient number of questions to ensure " +
                                               "\nthat questions are not repeated in stupid tickets.");
        }

        Map<String, List<Question>> mapQuestions;
        try {
            mapQuestions = futureTaskExtractContent.get(); // await answer
        } catch (InterruptedException e) { // in case interrupted thread
            futureTaskExtractContent.cancel(true); // then also interrupt extract-thread
            throw new InterruptedException(e.getMessage()); // throw this exception one level higher
        }

        // throw exception if insufficient quantity questions
        int quantityQuestions = Toolkit.amountQuestions(mapQuestions);
        if (uniqueQTickets && quantityTickets * quantityQTickets > (quantityQuestions)) {
            throw new NumberQuestionsRequireException("Insufficient number of questions ("
                                                      + quantityQuestions + ") to " +
                                                      "\nensure no repetition of questions in tickets");
        }

        listTicket = Toolkit.createListTickets(templateTicket, mapQuestions,
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
     * Class content necessary tools for {@link TicketGenerator}
     *
     * @author Gorbachev I. D.
     * @version 12.03.2022
     */
    private static final class Toolkit {

        /**
         * Create list tickets
         *
         * @param mapQuestions            map questions
         * @param quantityTickets         quantity tickets
         * @param quantityQuestionsTicket quantity questions into one Ticket
         * @return a list of tickets
         */
        private static List<Ticket> createListTickets(Ticket tempTicket, Map<String, List<Question>> mapQuestions,
                                                      final int quantityTickets, final int quantityQuestionsTicket) {
            List<Ticket> listTickets = new ArrayList<>(quantityTickets);
            List<List<Question>> listsQ = new ArrayList<>(mapQuestions.values());
            if (mapQuestions.isEmpty()) {
                return listTickets;
            }
            int[] arrCurPosList = new int[listsQ.size()]; // current positions of each list
            for (int indexTicket = 0; indexTicket < quantityTickets; ++indexTicket) {
                Ticket ticket = new Ticket(tempTicket.getInstitute(), tempTicket.getFaculty(), tempTicket.getDepartment(),
                        tempTicket.getSpecialization(), tempTicket.getDiscipline(), tempTicket.getTeacher(),
                        tempTicket.getHeadDepartment(), tempTicket.getType(), tempTicket.getDate(),
                        tempTicket.getProtocolNumber(), quantityQuestionsTicket);
                int curIdListQ = 0;

                for (int indexQuestion = 0; indexQuestion < quantityQuestionsTicket; ++indexQuestion) {
                    //------------------------
                    if (curIdListQ == listsQ.size()) {
                        curIdListQ = 0;
                    }

                    int countFilledList = 0;
                    while (curIdListQ < listsQ.size()) {
                        if ((arrCurPosList[curIdListQ] < listsQ.get(curIdListQ).size())) {
                            countFilledList = 0;
                            break;
                        } else {
                            ++curIdListQ;
                            if (curIdListQ == listsQ.size()) {
                                curIdListQ = 0;
                                // But if also each lists is filled, then start from the beginning
                                if (countFilledList >= listsQ.size()) {
                                    arrCurPosList = new int[listsQ.size()];
                                    countFilledList = 0;
                                }
                            }
                            ++countFilledList;
                        }
                    }
                    //------------------------

                    List<Question> listQ = listsQ.get(curIdListQ);
                    ticket.add(listQ.get(arrCurPosList[curIdListQ]));
                    arrCurPosList[curIdListQ]++;
                    curIdListQ++;
                }
                listTickets.add(ticket);
            }

            return listTickets;
        }

        /**
         * Count amount all questions
         *
         * @param mapQuestions map questions
         * @return amount all questions in map questions
         */
        private static int amountQuestions(Map<String, List<Question>> mapQuestions) {
            int count = 0;
            for (var list : mapQuestions.values()) {
                count += list.size();
            }
            return count;
        }
    }
}
