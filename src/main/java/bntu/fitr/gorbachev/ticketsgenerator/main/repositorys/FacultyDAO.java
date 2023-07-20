package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;

import java.util.List;
import java.util.UUID;

public interface FacultyDAO extends AbstractDAO<Faculty, UUID> {
    Faculty findByName(String name) throws DAOException;

    List<Faculty> findByUniversityId(UUID universityId) throws DAOException;
}
