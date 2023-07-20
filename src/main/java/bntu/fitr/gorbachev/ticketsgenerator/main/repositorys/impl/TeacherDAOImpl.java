package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.TeacherDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;

import java.util.List;
import java.util.UUID;

public class TeacherDAOImpl extends AbstractDAOImpl<Teacher, UUID> implements TeacherDAO {

    @Override
    public Teacher findByName(String name) throws DAOException {
        return null;
    }

    @Override
    public List<Teacher> findByFacultyId(UUID facultyId) throws DAOException {
        return null;
    }
}
