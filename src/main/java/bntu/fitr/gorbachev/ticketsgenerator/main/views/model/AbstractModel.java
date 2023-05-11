package bntu.fitr.gorbachev.ticketsgenerator.main.views.model;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.ChangeFieldModelListener;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.InitViewEvent;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.InitViewListener;
import lombok.NonNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Map;

public abstract class AbstractModel {
    protected final List<Map.Entry<Class<? extends EventListener>, EventListener>> listeners = new ArrayList<>();

    public void addChangeFiledModelListener(@NonNull ChangeFieldModelListener listener) {
        listeners.add(Map.entry(ChangeFieldModelListener.class, listener));
    }

    public void removeChangeFiledModelListener(@NonNull ChangeFieldModelListener listener) {
        listeners.removeIf(entityList -> entityList.getKey() == ChangeFieldModelListener.class &&
                                         entityList.getValue().equals(listener));
    }

    protected void fireChangeFiledEvent(String filedName, Object oldValue, Object newValue) {
        listeners.stream()
                .filter(entity -> entity.getKey() == ChangeFieldModelListener.class)
                .map(entity -> (ChangeFieldModelListener) entity.getValue())
                .forEach(lst -> lst.eventChangeFiledModel(ChangeFieldModelEvent.of(filedName, newValue, oldValue)));
    }

    public void addActionListener(@NonNull ActionListener listener) {
        listeners.add(Map.entry(ActionListener.class, listener));
    }

    public void removeActionListener(@NonNull ActionListener listener) {
        listeners.removeIf(entity -> entity.getKey() == ActionListener.class && entity.getValue().equals(listener));
    }

    public void fireActionEvent(String commandName,Object value) {
        listeners.stream()
                .filter(entity -> entity.getKey() == ChangeFieldModelListener.class)
                .map(entity -> (ActionListener) entity.getValue())
                .forEach(lst -> lst.actionPerformed(new ActionEvent(value, Integer.MAX_VALUE, commandName)));
    }

    public void addInitViewEvent(@NonNull InitViewListener listener) {
        listeners.add(Map.entry(InitViewListener.class, listener));
    }

    public void fireInitViewEvent(@NonNull Map<String, Object> fields) {
        listeners.stream()
                .filter(entity -> entity.getKey() == InitViewListener.class)
                .map(entity -> (InitViewListener) entity.getValue())
                .forEach(lst -> lst.eventInitView(new InitViewEvent(fields)));
    }

    public void triggeringInitView() {
    }

    protected void checkConditionsValueField(final String fieldName, Object value){
    }
}
