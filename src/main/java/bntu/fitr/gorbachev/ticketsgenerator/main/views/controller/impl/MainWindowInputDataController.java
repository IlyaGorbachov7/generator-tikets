package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.models.Ticket;
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
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Objects;

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

    public void actionChangeGenerationMode(GenerationMode mode){
        model.setGenerationMode(mode);
    }

    public void actionChangeTypeSession(Ticket.SessionType sessionType){
        model.setTypeSession(sessionType);
    }
    public void actionChangeDateTime(LocalDate date){
        model.setDateTime(date);
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

    // callbacks methods, invoked via model object

    @Override
    public void eventInitView(InitViewEvent event) {
        view.primaryInitViewElems(event);
    }

    @Override
    public void eventChangeFiledModel(ChangeFieldModelEvent e) {
        view.changeStateViewElems(e);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        view.actionViewElems(e);
    }
}
