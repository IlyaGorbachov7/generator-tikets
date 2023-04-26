package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.AbstractController;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.impl.AboutProgramPanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.AbstractModel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.AboutProgramModel;
import lombok.Getter;

@Getter
public class AboutProgramController extends AbstractController {
    private final AboutProgramPanel view;
    private final AboutProgramModel model;

    public AboutProgramController(BasePanel view, AbstractModel model) {
        super(view, model);
        this.view = (AboutProgramPanel) view;
        this.model = (AboutProgramModel) model;
        initView();
        init();
    }

    @Override
    public void init() {
        model.addChangeFiledModelListener(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void eventChangeFiledModel(ChangeFieldModelEvent e) {
        view.changeStateViewElems(e);
    }
}
