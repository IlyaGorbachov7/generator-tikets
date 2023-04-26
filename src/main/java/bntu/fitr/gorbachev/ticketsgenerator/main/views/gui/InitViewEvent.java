package bntu.fitr.gorbachev.ticketsgenerator.main.views.gui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Map;

@Data
@AllArgsConstructor
public class InitViewEvent {
    private Map<String, Object> fields;

    public Object addField(@NonNull String fieldName, @NonNull Object value) {
        return fields.put(fieldName, value);
    }

    public Object removeField(@NonNull String fieldName) {
        return fields.remove(fieldName);
    }

    public boolean containsField(@NonNull String fieldName) {
        return fields.containsKey(fieldName);
    }

    public Object getValueField(@NonNull String fieldName) {
        return fields.get(fieldName);
    }
}
