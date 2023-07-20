package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Specialization;

import java.util.List;
import java.util.UUID;

public interface SpecializationDAO extends AbstractDAO<SpecializationDAO, UUID> {
    Specialization findByName(String name) throws DAOException;

    List<Specialization> findByFacultyId(UUID facultyId) throws DAOException;
}
