package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Specialization;

import java.util.List;

public interface SpecializationDAO extends AbstractDAO<SpecializationDAO> {
    Specialization findByName(String name) throws DAOException;

    List<Specialization> findByFacultyId(Integer facultyId) throws DAOException;
}
