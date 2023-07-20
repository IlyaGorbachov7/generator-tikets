package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;

import java.util.List;
import java.util.UUID;

public interface HeadDepartmentDAO extends AbstractDAO<Department, UUID> {
    Teacher findByName(String name) throws DAOException;

    List<Teacher> findByDepartmentId(UUID departmentId) throws DAOException;
}
