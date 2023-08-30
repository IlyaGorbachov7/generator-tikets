package bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.specl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Specialization;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.EntityNoFoundByIdException;

public class SpecializationNoFoundByIdException extends EntityNoFoundByIdException {
    public SpecializationNoFoundByIdException() {
        super(Specialization.class.getSimpleName());
    }
}
