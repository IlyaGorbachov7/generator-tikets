package bntu.fitr.gorbachev.ticketsgenerator.main.views;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.gui.InitViewEvent;

import java.util.EventListener;

public interface InitViewListener extends EventListener {

    void eventInitView(InitViewEvent event);
}
