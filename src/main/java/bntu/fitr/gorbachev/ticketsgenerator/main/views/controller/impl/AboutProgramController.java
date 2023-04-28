package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.AbstractController;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.InitViewEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.impl.AboutProgramPanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.AboutProgramModel;
import lombok.Getter;

@Getter
public class AboutProgramController extends AbstractController {
    private final AboutProgramPanel view;
    private final AboutProgramModel model;

    public AboutProgramController(AboutProgramPanel view, AboutProgramModel model) {
        super(view, model);
        this.view = view;
        this.model = model;
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
}
