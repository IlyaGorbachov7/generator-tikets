package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.models.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.UniversityDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;

import java.beans.Customizer;
import java.beans.PropertyChangeListener;
import java.util.UUID;

public class UniversityDAOImpl extends UniversityAbstractDAOImpl<University, UUID> implements UniversityDAO, Customizer {

    @Override
    public University findByName(String name) throws DAOException {
        return null;
    }

    @Override
    public void setObject(Object bean) {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public Ticket<Question2> get() {
        return null;
    }
}
