package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.models.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.WriterTicketProperty;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.exceptions.GenerationConditionException;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.impl.GenerationPropertyImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.impl.TicketGeneratorImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.AbstractController;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.InitViewEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.impl.MainWindowPanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.tools.GenerationMode;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.MainWindowInputDataModel;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl.actionconst.MainWindowActionConst.ACTION_TICKET_GENERATION_IS_COMPLETELY;

@Getter
public class MainWindowInputDataController extends AbstractController implements ActionListener {
    private final MainWindowPanel view;
    private final MainWindowInputDataModel model;

    public MainWindowInputDataController(MainWindowPanel view,
                                         MainWindowInputDataModel model) {
        super(view, model);
        this.view = view;
        this.model = model;
    }
    // callback methods, invoked via view object, when user action occurred

    @Override
    public void actionInitViewByModel() {
        super.actionInitViewByModel();
    }

    public void actionChangeRandomRead(boolean b) {
        model.setRandomRead(b);
    }

    public void actionChangeRandomWrite(boolean b) {
        model.setRandomWrite(b);
    }

    public void actionChangeQuantityTickets(int quantityTickets) {
        model.setQuantityTickets(quantityTickets);
    }

    public void actionChangeQuantityQuestionsTickets(int quantityQuestionsTickets) {
        model.setQuantityQuestionTickets(quantityQuestionsTickets);
    }

    public void actionChangeGenerationMode(GenerationMode mode) {
        model.setGenerationMode(mode);
    }

    public void actionChangeTypeSession(Ticket.SessionType sessionType) {
        model.setTypeSession(sessionType);
    }

    public void actionChangeDateTime(LocalDate date) {
        model.setDateTime(date);
    }

    public void actionInitializeDataGeneration(WriterTicketProperty writerTicketProperty) {
        Ticket<Question2> tempTicket = Ticket.of(
                model.getInstitute(),
                model.getFaculty(),
                model.getDepartment(),
                model.getSpecialization(),
                model.getDiscipline(),
                model.getTeacher(),
                model.getHeadDepartment(),
                model.getTypeSession(),
                model.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                model.getProtocol()
        );
        var property = new GenerationPropertyImpl(
                model.getQuantityTickets(),
                model.getQuantityQuestionTickets(),
                true,
                model.getGenerationMode().getGenerateWay(),
                model.isRandomRead(),
                model.isRandomWrite());
        property.setWriterTicketProperty(writerTicketProperty);
        model.setTicket(tempTicket);
        model.setPropertyGeneration(property);
        model.setTicketGenerator(new TicketGeneratorImpl(model.getListFilesRsc().toArray(File[]::new), tempTicket));
    }

    public void actionClearDataGeneration(){
             /*Since if the user wants 100,000 tickets,the memory will fill up by 3GB.
                          At the end of the generation, we will write the generator*/
        model.setPropertyGeneration(null);
        model.setTicketGenerator(null);
        Runtime.getRuntime().gc();
    }
    public void actionStartGenerate() throws GenerationConditionException, ExecutionException, InterruptedException {
        model.getTicketGenerator().startGenerate(model.getPropertyGeneration());
        actionPerformed(new ActionEvent(model.getTicketGenerator().getDocxDec(), Integer.MAX_VALUE,
                ACTION_TICKET_GENERATION_IS_COMPLETELY));
    }

    public void actionWriteOutputFile(File file) throws IOException {
        model.getTicketGenerator().writeOutputFile(file);
    }

    public void actionChangeValueFieldGenerationPropertyFlagContinGenWithDepriveLev(boolean b) {
        model.getPropertyGeneration().setFlagContinGenWithDepriveLev(b);
    }

    public void actionChangeValueFieldGenerationPropertyUnique(boolean b) {
        model.getPropertyGeneration().setUnique(b);
    }

    public void actionChangeValueFieldGenerationPropertyFlagContinGenWithChapterWithoutSection(boolean b) {
        model.getPropertyGeneration().setFlagContinGenWithChapterWithoutSection(b);
    }

    @SneakyThrows
    public void actionFocusLostOnTextField(String fieldName, String textV) {
        try {
            Objects.requireNonNull(model.getClass().getMethod("set" + String.valueOf(fieldName.charAt(0)).toUpperCase()
                                                              + fieldName.substring(1), textV.getClass()))
                    .invoke(model, textV);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    public void actionAddFileQuestions(@NonNull File file) {
        if (!model.containsFile(file)) {
            model.addFile(file);
        }
    }

    public void actionRemoveFileQuestions(@NonNull File file) {
        if (model.containsFile(file)) {
            model.removeFile(file);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.actionViewElems(e);
    }
}
