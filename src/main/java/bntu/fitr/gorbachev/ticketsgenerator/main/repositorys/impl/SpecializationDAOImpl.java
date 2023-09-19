package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.SpecializationDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Specialization;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpecializationDAOImpl extends AppAreaAbstractDAOImpl<Specialization, UUID> implements SpecializationDAO {
    protected final String DEPARTMENT_ID_ARG = "departmentId_arg";

    protected final String DEPARTMENT_NAME_ARG = "departmentName_arg";

    private final String HQL_FIND_BY_departmentId = String.format("""
                    %s
                    where %s.department.id=:%s
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            DEPARTMENT_ID_ARG);

    private final String HQL_FIND_BY_departmentName = String.format("""
                    %s
                    where %s.department.name=:%s
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            DEPARTMENT_NAME_ARG);

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

    @Override
    @SuppressWarnings("unchecked")
    public List<Specialization> findByDepartmentId(UUID departmentId) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_departmentId,
                ENTITY_CLAZZ,
                Map.entry(DEPARTMENT_ID_ARG, departmentId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Specialization> findByDepartmentName(String departmentName) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_departmentName,
                ENTITY_CLAZZ,
                Map.entry(DEPARTMENT_NAME_ARG, departmentName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Specialization> findByLikeNameAndDepartmentId(String name, UUID departmentId) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_NAME_AND_departmentId,
                ENTITY_CLAZZ,
                Map.entry(DEPARTMENT_ID_ARG, departmentId),
                Map.entry(DEPARTMENT_NAME_ARG, String.join("", "%", name, "%")));
    }
}
