package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentDAO extends AppAreaAbstractDAO<Department, UUID> {

    List<Department> findByFacultyId(UUID facultyId) throws DAOException;

    long countByFacultyId(UUID facultyId) throws DAOException;

    List<Department> findByFacultyName(String facultyName) throws DAOException;

    long countByFacultyName(String facultyName) throws DAOException;

    List<Department> findByLikeNameAndFacultyId(String name, UUID facultyId) throws DAOException;

    long countByLikeNameAndFacultyId(String name, UUID facultyId) throws DAOException;
}
