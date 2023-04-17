package bntu.fitr.gorbachev.ticketsgenerator.main.dto;

import bntu.fitr.gorbachev.ticketsgenerator.main.dto.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.dto.tablentity.Faculty;

import java.util.List;

public interface FacultyDAO extends AbstractDAO<Faculty> {
    Faculty findByName(String name) throws DAOException;

    List<Faculty> findByUniversityId(Integer universityId) throws DAOException;
}
