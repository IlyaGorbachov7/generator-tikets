package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentDAO extends AbstractDAO<Department, UUID> {
    Department findByName(String name)throws DAOException;

    List<Department> findByFacultyId(UUID facultyId)throws DAOException;
}
