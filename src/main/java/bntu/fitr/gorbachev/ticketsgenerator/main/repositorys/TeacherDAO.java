package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;

import java.util.List;
import java.util.UUID;

public interface TeacherDAO extends AbstractDAO<Teacher, UUID> {
    Teacher findByName(String name) throws DAOException;

    List<Teacher> findByFacultyId(UUID facultyId) throws DAOException;
}
