package bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.univ;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.EntityNoFoundByIdException;

public class UniversityNoFoundByIdException extends EntityNoFoundByIdException {

    public UniversityNoFoundByIdException() {
        super(University.class.getSimpleName());
    }
}
