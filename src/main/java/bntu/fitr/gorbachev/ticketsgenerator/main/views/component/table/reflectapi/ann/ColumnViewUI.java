package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnViewUI {

    String NUMBER = "NUMBER";

    String STRING = "STRING";

    String name() default "";

    String typeView();
}
