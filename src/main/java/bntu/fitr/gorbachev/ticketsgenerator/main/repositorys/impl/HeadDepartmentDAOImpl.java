package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.HeadDepartmentDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.HeadDepartment;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public class HeadDepartmentDAOImpl extends AppAreaAbstractDAOImpl<HeadDepartment, UUID> implements HeadDepartmentDAO {
    protected final String DEPARTMENT_ID_ARG = "departmentId_arg";

    protected final String DEPARTMENT_NAME_ARG = "departmentName_arg";

    // ------------ HQL entry ------------------------ //

    private final String HQL_FIND_BY_departmentId = String.format("""
                    %s
                    where %s.department.id=:%s
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            DEPARTMENT_ID_ARG);

    private final String HQL_COUNT_BY_departmentId = String.format("""
                    select count(*)
                    %s
                    """,
            HQL_FIND_BY_departmentId);

    private final String HQL_FIND_BY_departmentName = String.format("""
                    %s
                    where %s.department.name=:%s
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            DEPARTMENT_NAME_ARG);

    private final String HQL_COUNT_BY_departmentName = String.format("""
                    select count(*)
                    %s
                     """,
            HQL_FIND_BY_departmentName);

    private final String HQL_FIND_BY_NAME_AND_departmentId = String.format("""
                    %s
                    where %s.department.id=:%s
                    and lower(%s.name) like lower(:%s)
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            DEPARTMENT_ID_ARG,
            ALLIES_TABLE,
            DEPARTMENT_NAME_ARG);

    private final String HQL_COUNT_BY_NAME_AND_departmentId = String.format("""
                    select count(*)
                    %s
                    """,
            HQL_FIND_BY_NAME_AND_departmentId);

    @Override
    @SuppressWarnings("unchecked")
    public List<HeadDepartment> findByDepartmentId(UUID departmentId) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_departmentId,
                ENTITY_CLAZZ,
                Map.entry(DEPARTMENT_ID_ARG, departmentId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByDepartmentId(UUID departmentId) throws DAOException {
        return executor.executeLongResult(HQL_COUNT_BY_departmentId, Map.entry(DEPARTMENT_ID_ARG, departmentId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<HeadDepartment> findByDepartmentName(String departmentName) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_departmentName,
                ENTITY_CLAZZ,
                Map.entry(DEPARTMENT_NAME_ARG, departmentName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByDepartmentName(String departmentName) throws DAOException {
        return executor.executeLongResult(HQL_COUNT_BY_departmentId, Map.entry(DEPARTMENT_NAME_ARG, departmentName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<HeadDepartment> findByLikeNameAndDepartmentName(String name, UUID departmentId) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_NAME_AND_departmentId,
                ENTITY_CLAZZ,
                Map.entry(DEPARTMENT_ID_ARG, departmentId),
                Map.entry(DEPARTMENT_NAME_ARG, String.join("", "%", name, "%")));
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByLikeNameAndDepartmentName(String name, UUID departmentId) throws DAOException {
        return executor.executeLongResult(
                HQL_COUNT_BY_departmentName,
                Map.entry(DEPARTMENT_ID_ARG, departmentId),
                Map.entry(DEPARTMENT_NAME_ARG, String.join("", "%", name, "%"))
        );
    }


}
