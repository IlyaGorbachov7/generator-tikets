package bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.deptm;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.EntityNoFoundByIdException;

public class DepartmentNoFoundByIdException extends EntityNoFoundByIdException {
    public DepartmentNoFoundByIdException() {
        super(Department.class.getSimpleName());
    }
}
