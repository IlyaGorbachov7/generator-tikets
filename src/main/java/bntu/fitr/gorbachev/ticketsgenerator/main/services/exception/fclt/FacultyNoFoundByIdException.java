package bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.fclt;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.EntityNoFoundByIdException;

public class FacultyNoFoundByIdException extends EntityNoFoundByIdException {

    public FacultyNoFoundByIdException() {
        super(Faculty.class.getSimpleName());
    }
}
