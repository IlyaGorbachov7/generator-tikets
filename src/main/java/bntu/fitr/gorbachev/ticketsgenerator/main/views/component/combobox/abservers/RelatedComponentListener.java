package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.abservers;

@FunctionalInterface
public interface RelatedComponentListener {
    default void beforePerform(RelatedComponentEvent relatedComponentEvent){
    }

    void perform(RelatedComponentEvent relatedComponentEvent);

    default void afterPerform(RelatedComponentEvent relatedComponentEvent){
    }
}
