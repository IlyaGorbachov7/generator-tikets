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

    private final String HQL_COUNT_FIND_BY_departmentId = String.format("""
            select count(*)
            %s
            """, HQL_FIND_BY_departmentId);

    private final String HQL_FIND_BY_departmentName = String.format("""
                    %s
                    where %s.department.name=:%s
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            DEPARTMENT_NAME_ARG);

    private final String HQL_COUNT_FIND_BY_departmentName = String.format("""
            select count(*)
            %s
            """, HQL_FIND_BY_departmentName);

    private final String HQL_FIND_BY_LIKE_NAME_AND_departmentId = String.format("""
                    %s
                    where %s.department.id=:%s
                    and lower(%s.name) like lower(:%s)
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            DEPARTMENT_ID_ARG,
            ALLIES_TABLE,
            DEPARTMENT_NAME_ARG);

    private final String HQL_COUNT_FIND_BY_LIKE_NAME_AND_departmentId = String.format("""
            select count(*)
            %s
            """, HQL_FIND_BY_LIKE_NAME_AND_departmentId);

    private final String HQL_FIND_BY_departmentId_LIMIT = String.format("""
                    %s
                    %s
                    """,
            HQL_FIND_BY_departmentId,
            HQL_LIMIT);

    private final String HQL_FIND_BY_LIKE_NAME_AND_departmentId_LIMIT = String.format("""
                    %s
                    %s
                    """,
            HQL_FIND_BY_LIKE_NAME_AND_departmentId,
            HQL_LIMIT);

    @Override
    @SuppressWarnings("unchecked")
    public List<Specialization> findByDepartmentId(UUID departmentId) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_departmentId,
                ENTITY_CLAZZ,
                Map.entry(DEPARTMENT_ID_ARG, departmentId)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Specialization> findByDepartmentId(UUID departmentId, int page, int itemsOnPage) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_departmentId_LIMIT,
                ENTITY_CLAZZ,
                Map.entry(DEPARTMENT_ID_ARG, departmentId),
                Map.entry(ITEMS_ON_PAGE_arg, itemsOnPage),
                Map.entry(OFFSET_arg, calculateOffset.apply(page, itemsOnPage))
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByDepartmentId(UUID departmentId) throws DAOException {
        return executor.executeLongResult(HQL_COUNT_FIND_BY_departmentId,
                Map.entry(DEPARTMENT_ID_ARG, departmentId)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Specialization> findByDepartmentName(String departmentName) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_departmentName,
                ENTITY_CLAZZ,
                Map.entry(DEPARTMENT_NAME_ARG, departmentName)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByDepartmentName(String departmentName) throws DAOException {
        return executor.executeLongResult(HQL_COUNT_FIND_BY_departmentName,
                Map.entry(DEPARTMENT_NAME_ARG, departmentName)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Specialization> findByLikeNameAndDepartmentId(String name, UUID departmentId) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_LIKE_NAME_AND_departmentId,
                ENTITY_CLAZZ,
                Map.entry(DEPARTMENT_ID_ARG, departmentId),
                Map.entry(DEPARTMENT_NAME_ARG, String.join("", "%", name, "%"))
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Specialization> findByLikeNameAndDepartmentId(String name, UUID departmentId, int page, int itemsOnPage) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_LIKE_NAME_AND_departmentId_LIMIT,
                ENTITY_CLAZZ,
                Map.entry(DEPARTMENT_ID_ARG, departmentId),
                Map.entry(DEPARTMENT_NAME_ARG, String.join("", "%", name, "%")),
                Map.entry(ITEMS_ON_PAGE_arg, itemsOnPage),
                Map.entry(OFFSET_arg, calculateOffset.apply(page, itemsOnPage)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByLikeNameAndDepartmentId(String name, UUID departmentId) throws DAOException {
        return executor.executeLongResult(
                HQL_COUNT_FIND_BY_LIKE_NAME_AND_departmentId,
                Map.entry(DEPARTMENT_ID_ARG, departmentId),
                Map.entry(DEPARTMENT_NAME_ARG, String.join("", "%", name, "%"))
        );
    }
}
