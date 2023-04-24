package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;

import java.util.List;

public interface FacultyDAO extends AbstractDAO<Faculty> {
    Faculty findByName(String name) throws DAOException;

    List<Faculty> findByUniversityId(Integer universityId) throws DAOException;
}
