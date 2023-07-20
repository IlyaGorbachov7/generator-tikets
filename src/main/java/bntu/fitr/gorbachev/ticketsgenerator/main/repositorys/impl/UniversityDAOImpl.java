package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.UniversityDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;

import java.util.UUID;

public class UniversityDAOImpl extends AbstractDAOImpl<University, UUID> implements UniversityDAO {

    @Override
    public University findByName(String name) throws DAOException {
        return null;
    }
}
