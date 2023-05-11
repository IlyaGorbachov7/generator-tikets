package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.AbstractController;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.impl.AboutAuthorPanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.AboutAuthorModel;
import lombok.Getter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
public class AboutAuthorController extends AbstractController implements ActionListener {
    private final AboutAuthorPanel view;

    private final AboutAuthorModel model;

    public AboutAuthorController(AboutAuthorPanel view, AboutAuthorModel model) {
        super(view, model);
        this.view = view;
        this.model = model;
    }

    @Override
    public void init() {
        super.init();
       /*invoking super.getModel() link, by reason that link this.getModel() == null
         take look on the constructor*/
        super.getModel().addActionListener(this);
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
    public void actionPerformed(ActionEvent e) {
        view.actionViewElems(e);
    }
}
