package bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.univ;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

public class UniversityNoFoundById extends ServiceException {
    public UniversityNoFoundById() {
        super("University don't found by ID");
    }
}
