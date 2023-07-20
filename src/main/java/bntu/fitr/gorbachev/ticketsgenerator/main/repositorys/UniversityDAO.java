package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;

import java.util.UUID;

public interface UniversityDAO extends AbstractDAO<University, UUID> {
    University findByName(String name) throws DAOException;
}
