package bntu.fitr.gorbachev.ticketsgenerator.main.dto;

import bntu.fitr.gorbachev.ticketsgenerator.main.dto.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.dto.tablentity.Teacher;

import java.util.List;

public interface TeacherDAO extends AbstractDAO<Teacher> {
    Teacher findByName(String name) throws DAOException;

    List<Teacher> findByFacultyId(Integer facultyId) throws DAOException;
}
