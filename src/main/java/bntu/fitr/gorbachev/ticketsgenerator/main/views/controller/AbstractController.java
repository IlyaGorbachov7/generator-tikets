package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.InitViewListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.InitViewEvent;
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
        init();
    }

    /**
     * This method require necessary invoke inside constructors this classes
     * <p>
     * It contains base initialization, required for normal work.
     * <p>
     * For extended classes no necessary invoke it method. All that will be required
     * is overridden method and adding necessary's code
     */
    public void init() {
        model.addChangeFiledModelListener(this);
        model.addInitViewEvent(this);
    }

    public void actionInitViewByModel() {
        // invoke through this.getModel() very necessary!
        this.getModel().triggeringInitView();
    }

    // callbacks methods, invoked via model object

    public  void eventInitView(InitViewEvent event){
        view.primaryInitViewElems(event);
    };

    @Override
    public void eventChangeFiledModel(ChangeFieldModelEvent e){
        view.changeStateViewElems(e);
    };
}
