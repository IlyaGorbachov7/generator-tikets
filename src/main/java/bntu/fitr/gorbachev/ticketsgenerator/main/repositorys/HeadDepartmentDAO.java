package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.HeadDepartmentDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.HeadDepartment;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;

import java.util.List;
import java.util.UUID;

public interface HeadDepartmentDAO extends AbstractDAO<HeadDepartment, UUID> {

    List<HeadDepartment> findByDepartmentId(UUID departmentId) throws DAOException;

    List<HeadDepartment> findByDepartmentName(String departmentName) throws DAOException;

    List<HeadDepartment> findByLikeNameAndDepartmentName(String name, UUID departmentId) throws DAOException;
}
