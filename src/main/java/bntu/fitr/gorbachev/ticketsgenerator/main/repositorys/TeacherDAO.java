package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;

import java.util.List;
import java.util.UUID;

public interface TeacherDAO extends AbstractDAO<Teacher, UUID> {

    List<Teacher> findByFacultyId(UUID facultyId) throws DAOException;

    List<Teacher> finByFacultyName(String facultyName) throws DAOException;

    List<Teacher> findByNameAndFacultyId(String name, UUID facultyId) throws DAOException;
}
