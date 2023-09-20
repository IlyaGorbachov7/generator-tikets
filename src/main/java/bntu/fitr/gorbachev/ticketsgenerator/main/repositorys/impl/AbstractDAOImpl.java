package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.AbstractDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.impl.HQueryMasterImpl;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.*;

/**
 * <b>Learn note: </b> When compose HQL query we should use entityName.
 * HQL query: from 'entityName' - in HQL queries Hibernate automatically replace EntityName to particular
 * TableName of DB. If you're going to execute SQL query, then combine SQL query you should use particular table name or
 * DB.
 */
@Slf4j
public abstract class AbstractDAOImpl<T, ID> implements AbstractDAO<T, ID> {
    @Getter
    protected HQueryMaster<T> executor = new HQueryMasterImpl<>();

    protected final Class<?> ENTITY_CLAZZ = extractEntityClassFromDao(this.getClass());

    protected final String ENTITY_NAME = extractEntityNameFromJakartaAnnEntity(ENTITY_CLAZZ);

    protected final String ALLIES_TABLE = "tbl";

    private final String ENTITY_ID_ARG = "entityId_arg";

    // ------------ HQL entry ------------------------ //

    protected final String HQL_SELECT = String.format("""
                    from %s as %s
                    """,
            ENTITY_NAME,
            ALLIES_TABLE);

    protected final String HQL_COUNT_SELECT = String.format("""
            select count(*)
            %s
            """, HQL_SELECT);

    private final String HQL_FIND_BY_ID = String.format("""
                    %s
                    where %s.%s=:%s
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            getFieldNameFindByAnnotation(ENTITY_CLAZZ, Id.class),
            ENTITY_ID_ARG);


    private final String HQL_FIND_ANY = String.format("""
                    %s
                    order by %s
                    LIMIT 1 OFFSET 0
                    """,
            HQL_SELECT,
            getFieldNameFindByAnnotation(ENTITY_CLAZZ, Id.class));

    @Override
    public ID create(T entity) throws DAOException {
        return executor.persist(entity);
    }

    @Override
    public void delete(T entity) throws DAOException {
        executor.delete(entity);
    }

    @Override
    public void update(T entity) throws DAOException {
        executor.update(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<T> findById(ID id) throws DAOException {
        return executor.executeSingleEntityQuery(
                HQL_FIND_BY_ID,
                ENTITY_CLAZZ,
                Map.entry(ENTITY_ID_ARG, id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() throws DAOException {
        return executor.executeQuery(
                HQL_SELECT,
                ENTITY_CLAZZ);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<T> findAny() throws DAOException {
        return executor.executeSingleEntityQuery(
                HQL_FIND_ANY,
                ENTITY_CLAZZ);
    }

    @Override
    @SuppressWarnings("unchecked")
    public long count() throws DAOException {
        return  executor.executeLongResult(HQL_COUNT_SELECT);
    }
}
