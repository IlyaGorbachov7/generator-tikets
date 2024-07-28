package bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.displn;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Discipline;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.EntityNoFoundByIdException;

public class DisciplineNoFoundByIdException extends EntityNoFoundByIdException {
    public DisciplineNoFoundByIdException() {
        super(Discipline.class.getSimpleName());
    }
}
