package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.InitViewListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.InitViewEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.AbstractModel;
import lombok.*;

public abstract class AbstractController implements ChangeFieldModelListener, InitViewListener {

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

    /**
     * This method require necessary invoke inside constructors extends classes
     * <p>
     * It contains base realize, required for normal work
     */
    public void init() {
        // invoke through this.getModel very necessary, because extends classes may be overriding fields supper class
        this.getModel().addChangeFiledModelListener(this);
        this.getModel().addInitViewEvent(this);
    }

    public void actionInitViewByModel() {
        // invoke through this.getModel() very necessary!
        this.getModel().triggeringInitView();
    }

    public abstract void eventInitView(InitViewEvent event);

    @Override
    public abstract void eventChangeFiledModel(ChangeFieldModelEvent e);
}
