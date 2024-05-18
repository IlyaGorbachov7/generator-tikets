package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.function.Function;

@Data
@Builder
public class ModelTableViewSupplier {

    private Class<?> clazzModelView;

    private Function<Object, List<?>> supplierData;

    private Function<Object, List<?>> supplierCreate;

    private Function<Object, List<?>> supplierUpdate;

    private Function<Object, List<?>> supplierDelete;
}
