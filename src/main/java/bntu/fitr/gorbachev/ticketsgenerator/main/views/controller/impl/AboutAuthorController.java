package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.AbstractController;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.impl.AboutAuthorPanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.AbstractModel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.AboutAuthorModel;
import lombok.Getter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
public class AboutAuthorController extends AbstractController implements ActionListener {
    private AboutAuthorPanel view;

    private AboutAuthorModel model;

    public AboutAuthorController(BasePanel view, AbstractModel model) {
        super(view, model);
        this.view = (AboutAuthorPanel) view;
        this.model= (AboutAuthorModel) model;
        initView();
        init();
    }

    @Override
    public void init() {
        this.model.addChangeFiledModelListener(this);
        this.model.addActionListener(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void eventChangeFiledModel(ChangeFieldModelEvent e) {
        view.changeStateViewElems(e);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.actionViewElems(e);
    }

    public void actionOkBtn(){
        view.getRootFrame().setVisible(false);
    }

}
