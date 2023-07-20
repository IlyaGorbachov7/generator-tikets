package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DepartmentDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;

import java.util.List;
import java.util.UUID;

public class DepartmentDAOImpl extends AbstractDAOImpl<Department, UUID> implements DepartmentDAO {

    @Override
    public Department findByName(String name) throws DAOException {
        return null;
    }

    @Override
    public List<Department> findByFacultyId(UUID facultyId) throws DAOException {
        return null;
    }
}
