package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.AbstractController;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.InitViewEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.impl.AboutAuthorPanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.AbstractModel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.AboutAuthorModel;
import lombok.Getter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
public class AboutAuthorController extends AbstractController implements ActionListener {
    private final AboutAuthorPanel view;

    private final AboutAuthorModel model;

    public AboutAuthorController(BasePanel view, AbstractModel model) {
        super(view, model);
        this.view = (AboutAuthorPanel) view;
        this.model = (AboutAuthorModel) model;
        init();
    }

    @Override
    public void init() {
        super.init();
        model.addActionListener(this);
    }

    // callback methods, invoked via view object, when user action occurred

    @Override
    public void actionInitViewByModel() {
        super.actionInitViewByModel();
    }

    public void actionOkBtn() {
        view.getRootFrame().setVisible(false);
    }

    // callbacks methods, invoked via model object

    @Override
    public void eventInitView(InitViewEvent event) {
        view.actionInitViewElems(event);
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
