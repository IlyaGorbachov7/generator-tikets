package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;

import java.util.List;

public interface TeacherDAO extends AbstractDAO<Teacher> {
    Teacher findByName(String name) throws DAOException;

    List<Teacher> findByFacultyId(Integer facultyId) throws DAOException;
}
