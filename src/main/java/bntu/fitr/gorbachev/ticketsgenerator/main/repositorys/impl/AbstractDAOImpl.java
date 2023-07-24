package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.AbstractDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.PoolConnection;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO;
import jakarta.persistence.Id;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.extractEntityClassFromDao;
import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.extractEntityNameFromJakartaAnnEntity;

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
        if (isActiveEarly != session.getTransaction().isActive()) {
            log.warn("isActiveEarly={} don't matching current transaction", isActiveEarly);
            throw new IllegalArgumentException(String.format("isActiveEarly=%b don't matching current transaction", isActiveEarly));
        }
        if (!isActiveEarly) {
            session.getTransaction().commit();
        }
    }

    protected void rollbackTransaction(boolean isActiveEarly, Session session) {
        if (isActiveEarly != session.getTransaction().isActive()) {
            log.warn("isActiveEarly={} don't matching current transaction", isActiveEarly);
            throw new IllegalArgumentException(String.format("isActiveEarly=%b don't matching current transaction", isActiveEarly));
        }
        if (!isActiveEarly) {
            session.getTransaction().rollback();
        }
    }

    @Override
    public ID create(T entity) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        beginTransaction(isActiveTrans, session);
        session.persist(entity);
        commitTransaction(isActiveTrans, session);
        return ReflectionHelperDAO.getValueFromFieldFindByAnnotation(entity, Id.class);
    }

    @Override
    public void delete(T entity) throws DAOException {
        try (Session session = poolConnection.getSession();) {
            Transaction tran = session.beginTransaction();
            session.remove(entity);
            tran.commit();
        } catch (ConnectionPoolException e) {
            log.warn(e.getCause().getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public void update(T entity) throws DAOException {
        try (Session session = poolConnection.getSession();) {
            Transaction tran = session.beginTransaction();
            session.refresh(entity);
            tran.commit();
        } catch (ConnectionPoolException e) {
            log.warn(e.getCause().getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public T findById(ID id) throws DAOException {
        try (Session session = poolConnection.getSession();) {
            Transaction tran = session.beginTransaction();
            T entity = session.get(extractEntityClassFromDao(this.getClass()), id);
            tran.commit();
            return entity;
        } catch (ConnectionPoolException e) {
            log.warn(e.getCause().getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public List<T> findAll() throws DAOException {
        try {
            Session session = poolConnection.getSession();
            Transaction tran = session.beginTransaction();
            Class<?> entityClazz = extractEntityClassFromDao(this.getClass());
            SelectionQuery<?> selectionQuery = session.createSelectionQuery(String.format("from %s",
                    Objects.requireNonNull(extractEntityNameFromJakartaAnnEntity(entityClazz)), entityClazz));
            @SuppressWarnings("unchecked")
            List<T> resultList = (List<T>) selectionQuery.getResultList();
            tran.commit();
            return resultList;
        } catch (ConnectionPoolException e) {
            log.warn(e.getCause().getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<T> findAny() throws DAOException {
        try {
            Session session = poolConnection.getSession();
            Transaction trans = session.beginTransaction();
            Class<?> entityClazz = extractEntityClassFromDao(this.getClass());
            SelectionQuery<?> selectionQuery = session.createSelectionQuery(String.format("""
                            from %s
                            order by name
                            LIMIT 1 OFFSET 0
                            """,
                    Objects.requireNonNull(extractEntityNameFromJakartaAnnEntity(entityClazz)), entityClazz));
            Optional<T> res = (Optional<T>) selectionQuery.stream().findAny();
            trans.commit();
            if (session.isOpen()) {
                session.close();
            }
            return res;
        } catch (ConnectionPoolException e) {
            log.warn(e.getCause().getMessage());
            throw new DAOException(e);
        }
    }
}
