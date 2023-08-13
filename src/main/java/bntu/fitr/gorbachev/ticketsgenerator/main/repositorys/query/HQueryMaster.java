package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.PoolConnection;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;

import java.util.List;
import java.util.Map;

public abstract class HQueryMaster<T> {

    public abstract List<T> executeQuery(String query, Class<?> resultClass,
                                         Map.Entry<String, Object>... params) throws DAOException;

    public abstract List<T> executeSelectionQuery(String selectionQuery, Class<?> resultClass,
                                                  Map.Entry<String, Object>... params) throws DAOException;

    public abstract <ID> ID persist(T entity) throws DAOException;

    public abstract void delete(T entity) throws DAOException;

    public abstract void update(T entity) throws DAOException;

    protected abstract <R> void setStatementParams(Query<R> query, Map.Entry<String, Object>... mappedParams);

    protected abstract <R> void setStatementParams(SelectionQuery<R> query, Map.Entry<String, Object>... mappedParams);

    public Session getSession() throws DAOException {
        try {
            return PoolConnection.Builder.build().getSession();
        } catch (ConnectionPoolException e) {
            throw new DAOException(e);
        }
    }

    public HQueryMaster<T> beginTransaction(Session session) {
        if (!isActiveTransaction(session)) {
            session.beginTransaction();
        }
        return this;
    }

    public HQueryMaster<T> commitTransaction(Session session) {
        if (!isActiveTransaction(session)) {
            session.getTransaction().commit();
        }
        return this;
    }

    public void rollbackTransaction(Session session) {
        if (!isActiveTransaction(session)) {
            session.getTransaction().rollback();
        }
    }

    public boolean isActiveTransaction(Session session) {
        return session.getTransaction().isActive();
    }
}
