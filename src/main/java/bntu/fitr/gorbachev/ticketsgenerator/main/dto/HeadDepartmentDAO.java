package bntu.fitr.gorbachev.ticketsgenerator.main.dto;

import bntu.fitr.gorbachev.ticketsgenerator.main.dto.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.dto.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.dto.tablentity.Teacher;

import java.util.List;

public interface HeadDepartmentDAO extends AbstractDAO<Department> {
    Teacher findByName(String name) throws DAOException;

    List<Teacher> findByDepartmentId(Integer departmentId) throws DAOException;
}
