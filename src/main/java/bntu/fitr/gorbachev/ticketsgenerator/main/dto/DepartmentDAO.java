package bntu.fitr.gorbachev.ticketsgenerator.main.dto;

import bntu.fitr.gorbachev.ticketsgenerator.main.dto.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.dto.tablentity.Department;

import java.util.List;

public interface DepartmentDAO extends AbstractDAO<Department> {
    Department findByName(String name)throws DAOException;

    List<Department> findByFacultyId(Integer facultyId)throws DAOException;
}
