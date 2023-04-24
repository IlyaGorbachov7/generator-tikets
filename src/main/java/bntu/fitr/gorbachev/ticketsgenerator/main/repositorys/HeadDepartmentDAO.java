package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;

import java.util.List;

public interface HeadDepartmentDAO extends AbstractDAO<Department> {
    Teacher findByName(String name) throws DAOException;

    List<Teacher> findByDepartmentId(Integer departmentId) throws DAOException;
}
