package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.handers;

public interface ChoiceButtonListListener {


    default void beforePerform(EventChoiceBtn event) {
    }

    void perform(EventChoiceBtn event);

    default void afterPerform(EventChoiceBtn event) {
    }
}
