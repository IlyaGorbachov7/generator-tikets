package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.AbstractController;
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
}
