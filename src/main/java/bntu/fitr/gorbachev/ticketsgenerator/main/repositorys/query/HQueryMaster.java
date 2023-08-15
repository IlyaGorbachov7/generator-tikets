package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.PoolConnection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class HQueryMaster<T> {

    @SuppressWarnings("unchecked")
    public abstract <R> List<T> executeQuery(String query, Class<R> resultClass,
                                             Map.Entry<String, Object>... params) throws DAOException;

    @SuppressWarnings("unchecked")
    public abstract <R> Optional<T> executeSingleEntityQuery(String query, Class<R> resultClass,
                                                             Map.Entry<String, Object>... params) throws DAOException;

    public abstract <R> List<R> executeSupplierQuery(Supplier<List<R>> runner) throws DAOException;

    public abstract  <R> R executeSingleEntitySupplierQuery(Supplier<R> runner) throws DAOException;

    public abstract <ID> ID persist(T entity) throws DAOException;

    public abstract void delete(T entity) throws DAOException;

    public abstract void update(T entity) throws DAOException;

    @SuppressWarnings("unchecked")
    protected abstract void setStatementParams(Query<T> query, Map.Entry<String, Object>... mappedParams);

    // ------------ Helper methods ---------------------

    public Session getSession() throws DAOException {
        try {
            return PoolConnection.Builder.build().getSession();
        } catch (ConnectionPoolException e) {
            throw new DAOException(e);
        }
    }

    public void beginTransaction(boolean isActiveEarly, Session session) {
        if (!isActiveEarly) {
            session.beginTransaction();
        }
        session.getTransaction();
    }

    public void commitTransaction(boolean isActiveEarly, Session session) {
        if (!isActiveEarly) {
            session.getTransaction().commit();
        }
    }

    public void rollbackTransaction(boolean isActiveEarly, Session session) {
        if (!isActiveEarly) {
            session.getTransaction().rollback();
        }
    }

    public boolean isActiveTransaction(Session session) {
        return session.getTransaction().isActive();
    }

}
