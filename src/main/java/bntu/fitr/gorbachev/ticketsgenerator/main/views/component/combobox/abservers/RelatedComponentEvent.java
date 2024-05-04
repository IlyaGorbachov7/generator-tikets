package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.combobox.abservers;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RelatedComponentEvent extends AWTEvent {
    public final String DEFAULT_SOURCE = "defaultSource";
    Map<String, Object> mapSources = new HashMap<>(4);

    public RelatedComponentEvent(Object component) {
        super(component, 0);
        mapSources.put(DEFAULT_SOURCE, component);
    }

    public Object getSource(String key) {
        return mapSources.get(key);
    }

    @Override
    public void setSource(Object newSource) {
        super.setSource(newSource);
        mapSources.replace(DEFAULT_SOURCE, newSource);
    }

    public Object setSource(String key, Object newSource) {
        return mapSources.replace(key, newSource);
    }

    public Object addSource(String newKey, Object newSource) {
        return mapSources.put(newKey, newSource);
    }

    public Object removeSource(String key) {
        if (key.equals(DEFAULT_SOURCE)) throw new RuntimeException("Default value cannot be removed");
        return mapSources.remove(key);
    }
}
