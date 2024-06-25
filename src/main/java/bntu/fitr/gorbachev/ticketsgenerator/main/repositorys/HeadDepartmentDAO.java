package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.HeadDepartment;

import java.util.List;
import java.util.UUID;

public interface HeadDepartmentDAO extends AppAreaAbstractDAO<HeadDepartment, UUID> {

    List<HeadDepartment> findByDepartmentId(UUID departmentId) throws DAOException;

    long countByDepartmentId(UUID departmentId) throws DAOException;

    List<HeadDepartment> findByDepartmentName(String departmentName) throws DAOException;

    long countByDepartmentName(String departmentName) throws DAOException;

    List<HeadDepartment> findByLikeNameAndDepartmentId(String name, UUID departmentId) throws DAOException;

    long countByLikeNameAndDepartmentId(String name, UUID departmentId) throws DAOException;
}
