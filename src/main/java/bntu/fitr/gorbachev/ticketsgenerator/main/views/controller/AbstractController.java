package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.AbstractModel;
import lombok.*;

public abstract class AbstractController implements ChangeFieldModelListener {

    @Setter
    @Getter
    protected BasePanel view;
    @Setter
    @Getter
    protected AbstractModel model;

    public AbstractController(BasePanel view, AbstractModel model) {
        this.view = view;
        this.model = model;
    }

    public abstract void init();

    public abstract void initView();

    @Override
    public abstract void eventChangeFiledModel(ChangeFieldModelEvent e);
}
