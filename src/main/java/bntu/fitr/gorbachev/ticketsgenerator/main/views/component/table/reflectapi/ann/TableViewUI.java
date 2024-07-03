package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TableViewUI {
    String CONST = "04052024";

    /**
     * Russion version
     */
    String name() default "";
}
