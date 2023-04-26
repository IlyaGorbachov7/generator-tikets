package bntu.fitr.gorbachev.ticketsgenerator.main.views;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.EventObject;

@Getter
@AllArgsConstructor(staticName = "of")
public class ChangeFieldModelEvent {
    private String fieldName;

    private Object newValue;

    private Object oldValue;

}
