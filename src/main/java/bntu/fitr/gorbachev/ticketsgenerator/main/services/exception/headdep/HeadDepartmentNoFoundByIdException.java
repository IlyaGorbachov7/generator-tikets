package bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.headdep;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.HeadDepartment;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.EntityNoFoundByIdException;

public class HeadDepartmentNoFoundByIdException extends EntityNoFoundByIdException {
    public HeadDepartmentNoFoundByIdException() {
        super(HeadDepartment.class.getSimpleName());
    }
}
