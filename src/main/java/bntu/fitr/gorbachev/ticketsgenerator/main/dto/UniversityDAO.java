package bntu.fitr.gorbachev.ticketsgenerator.main.dto;

import bntu.fitr.gorbachev.ticketsgenerator.main.dto.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.dto.tablentity.University;

public interface UniversityDAO extends AbstractDAO<University> {
    University findByName(String name) throws DAOException;
}
