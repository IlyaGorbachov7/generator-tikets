package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.function.Function;

@Data
@Builder
public class ModelTableViewSupplier {

    private Class<?> clazzModelView;

    private RelatedTblDataBase relatedMdlTbl;

    private Function<Object, List<?>> supplierData;

    private Function<Object, Object> supplierCreate;

    private Function<Object, Object> supplierUpdate;

    private Function<Object, List<?>> supplierDelete;
}
