package bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.tchr;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.EntityNoFoundByIdException;

public class TeacherNoFoundByIdException extends EntityNoFoundByIdException {
    public TeacherNoFoundByIdException() {
        super(Teacher.class.getSimpleName());
    }
}
