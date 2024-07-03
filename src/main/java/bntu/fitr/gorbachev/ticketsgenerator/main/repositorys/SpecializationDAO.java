package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Specialization;

import java.util.List;
import java.util.UUID;

public interface SpecializationDAO extends AppAreaAbstractDAO<Specialization, UUID> {

    List<Specialization> findByDepartmentId(UUID departmentId) throws DAOException;

    List<Specialization> findByDepartmentId(UUID departmentId, int page, int itemsOnPage) throws DAOException;

    long countByDepartmentId(UUID departmentId) throws DAOException;

    List<Specialization> findByDepartmentName(String departmentName) throws DAOException;

    long countByDepartmentName(String departmentName) throws DAOException;

    List<Specialization> findByLikeNameAndDepartmentId(String name, UUID departmentId) throws DAOException;

    List<Specialization> findByLikeNameAndDepartmentId(String name, UUID departmentId, int page, int itemsOnPage) throws DAOException;

    long countByLikeNameAndDepartmentId(String name, UUID departmentId) throws DAOException;
}
