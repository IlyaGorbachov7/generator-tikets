package bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.controller.AbstractController;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.BasePanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.panels.impl.RecordSettingPanel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.AbstractModel;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.model.impl.RecordSettingModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecordSettingController extends AbstractController implements ActionListener {
    private final RecordSettingPanel view;
    private final RecordSettingModel model;

    public RecordSettingController(BasePanel view, AbstractModel model) {
        super(view, model);
        this.view = (RecordSettingPanel) view;
        this.model = (RecordSettingModel) model;
        initView();
        init();
    }

    @Override
    public void init() {
        model.addChangeFiledModelListener(this);
        model.addActionListener(this);
    }

    @Override
    public void initView() {
        view.getSpinnerQuantityTicketOnSinglePage().setValue(model.getQuantityTicketOnSinglePage());
        view.getSpinnerFontSize().setValue(model.getFontSize());
    }

    // callback methods, invoked via view object, when user action occurred

    public void actionWindowClosing(Integer quantityTicketOnSinglePage, Integer fontSize) {
        model.setQuantityTicketOnSinglePage(quantityTicketOnSinglePage);
        model.setFontSize(fontSize);
    }

    public void actionOkBtn(Integer quantityTicketOnSinglePage, Integer fontSize) {
        actionWindowClosing(quantityTicketOnSinglePage, fontSize);
    }

    // callbacks methods, invoked via model object

    @Override
    public void eventChangeFiledModel(ChangeFieldModelEvent e) {
        view.changeStateViewElems(e);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.actionViewElems(e);
    }
}
