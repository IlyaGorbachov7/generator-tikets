package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;

import java.util.List;

public interface DepartmentDAO extends AbstractDAO<Department> {
    Department findByName(String name)throws DAOException;

    List<Department> findByFacultyId(Integer facultyId)throws DAOException;
}
