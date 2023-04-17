package bntu.fitr.gorbachev.ticketsgenerator.main.dto;

import bntu.fitr.gorbachev.ticketsgenerator.main.dto.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.dto.tablentity.Specialization;

import java.util.List;

public interface SpecializationDAO extends AbstractDAO<SpecializationDAO> {
    Specialization findByName(String name) throws DAOException;

    List<Specialization> findByFacultyId(Integer facultyId) throws DAOException;
}
