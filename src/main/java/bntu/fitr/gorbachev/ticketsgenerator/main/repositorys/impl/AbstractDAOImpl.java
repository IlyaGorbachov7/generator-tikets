package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.AbstractDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.PoolConnection;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.*;

@Slf4j
public abstract class AbstractDAOImpl<T, ID> implements AbstractDAO<T, ID> {
    protected final PoolConnection poolConnection = PoolConnection.Builder.build();

    protected Session getSession() throws DAOException {
        try {
            return poolConnection.getSession();
        } catch (ConnectionPoolException e) {
            log.warn(e.getCause().getMessage());
            throw new DAOException(e);
        }
    }

    protected boolean isActiveTransaction(Session session) {
        return session.getTransaction().isActive();
    }

    protected void beginTransaction(boolean isActiveEarly, Session session) {
        if (!isActiveEarly) {
            session.beginTransaction();
        }
        session.getTransaction();
    }

    protected void commitTransaction(boolean isActiveEarly, Session session) {
        if (!isActiveEarly) {
            session.getTransaction().commit();
        }
    }

    protected void rollbackTransaction(boolean isActiveEarly, Session session) {
        if (!isActiveEarly) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public ID create(T entity) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        beginTransaction(isActiveTrans, session);
        try {
            session.persist(entity);
            commitTransaction(isActiveTrans, session);
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
        return getValueFromFieldFindByAnnotation(entity, Id.class);
    }

    @Override
    public void delete(T entity) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            session.remove(entity);
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public void update(T entity) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            session.refresh(entity);
            commitTransaction(isActiveTrans, session);
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public T findById(ID id) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            T entity = session.get(extractEntityClassFromDao(this.getClass()), id);
            commitTransaction(isActiveTrans, session);
            return entity;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<T> findAll() throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        Class<?> entityClazz = extractEntityClassFromDao(this.getClass());
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<?> selectionQuery = session.createSelectionQuery(String.format("from %s",
                    Objects.requireNonNull(extractEntityNameFromJakartaAnnEntity(entityClazz))));
            @SuppressWarnings("unchecked")
            List<T> resultList = (List<T>) selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return resultList;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<T> findAny() throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        Class<?> entityClazz = extractEntityClassFromDao(this.getClass());
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<?> selectionQuery = session.createSelectionQuery(String.format("""
                            from %s
                            order by %s
                            LIMIT 1 OFFSET 0
                            """,
                    Objects.requireNonNull(extractEntityNameFromJakartaAnnEntity(entityClazz)),
                    extractColumnNameFromJakartaAnnColumn(entityClazz, getFieldNameFindByAnnotation(entityClazz, Id.class))));
            @SuppressWarnings("unchecked")
            Optional<T> res = (Optional<T>) selectionQuery.stream().findAny();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (HibernateException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }
}
