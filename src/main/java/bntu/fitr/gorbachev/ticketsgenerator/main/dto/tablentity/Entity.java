package bntu.fitr.gorbachev.ticketsgenerator.main.dto.tablentity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
public abstract class Entity {
    protected Integer id = null;

    protected Entity() {
    }

    public static void main(String[] args) {
        new Entity() {
        };
    }
}
