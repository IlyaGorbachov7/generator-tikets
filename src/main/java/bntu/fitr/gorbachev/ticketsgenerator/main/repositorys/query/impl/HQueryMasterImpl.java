package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.*;

public class HQueryMasterImpl<T> extends HQueryMaster<T> {

    @Override
    @SuppressWarnings("unchecked")
    public <R> List<T> executeQuery(String queryStr, Class<R> resultClass,
                                    Map.Entry<String, Object>... params) throws DAOException {
        Session session = getSession();
        boolean isActiveTransactionEarly = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTransactionEarly, session);
            Query<?> query = session.createQuery(queryStr, resultClass);
            setStatementParams((Query<T>) query, params);
            @SuppressWarnings("unchecked")
            List<T> resultList = (List<T>) query.getResultList();
            commitTransaction(isActiveTransactionEarly, session);
            return resultList;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTransactionEarly, session);
            throw new DAOException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Optional<T> executeSingleEntityQuery(String queryStr, Class<R> resultClass,
                                                    Map.Entry<String, Object>... params) throws DAOException {
        Session session = getSession();
        boolean isActiveTransactionEarly = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTransactionEarly, session);
            Query<?> query = session.createQuery(queryStr, resultClass);
            setStatementParams((Query<T>) query, params);
            @SuppressWarnings("unchecked")
            T entity = (T) query.getSingleResultOrNull();
            commitTransaction(isActiveTransactionEarly, session);
            return Optional.ofNullable(entity);
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTransactionEarly, session);
            throw new DAOException(e);
        }
    }

    @Override
    public <R> List<R> wrapTransactionalResultList(Supplier<List<R>> runner) throws DAOException {
        Session session = getSession();
        boolean isActiveTransactionEarly = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTransactionEarly, session);
            List<R> res = runner.get();
            commitTransaction(isActiveTransactionEarly, session);
            return res;
        } catch (Exception e) {
            rollbackTransaction(isActiveTransactionEarly, session);
            throw new DAOException(e);
        }
    }

    @Override
    public <R> R wrapTransactionalEntitySingle(Supplier<R> runner) throws DAOException {
        Session session = getSession();
        boolean isActiveTransactionEarly = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTransactionEarly, session);
            R res = runner.get();
            commitTransaction(isActiveTransactionEarly, session);
            return res;
        } catch (Exception e) {
            rollbackTransaction(isActiveTransactionEarly, session);
            throw new DAOException(e);
        }
    }

    @Override
    public void wrapTransactional(Runnable runner) throws ServiceException {
        Session session = getSession();
        boolean isActiveTransactionEarly = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTransactionEarly, session);
            runner.run();
            commitTransaction(isActiveTransactionEarly, session);
        } catch (Exception e) {
            rollbackTransaction(isActiveTransactionEarly, session);
            throw new DAOException(e);
        }
    }

    @Override
    public <ID> ID persist(T entity) throws DAOException {
        Session session = getSession();
        boolean isActiveTransactionEarly = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTransactionEarly, session);
            session.persist(entity);
            commitTransaction(isActiveTransactionEarly, session);
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTransactionEarly, session);
            throw new DAOException(e);
        }
        return getValueFromFieldFindByAnnotation(entity, Id.class);
    }

    @Override
    public void delete(T entity) throws DAOException {
        Session session = getSession();
        boolean isActiveTransactionEarly = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTransactionEarly, session);
            session.remove(entity);
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTransactionEarly, session);
            throw new DAOException(e);
        }
    }

    @Override
    public void update(T entity) throws DAOException {
        Session session = getSession();
        boolean isActiveTransactionEarly = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTransactionEarly, session);
            session.persist(entity);
            commitTransaction(isActiveTransactionEarly, session);
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTransactionEarly, session);
            throw new DAOException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setStatementParams(Query<T> query, Map.Entry<String, Object>... mappedParams) {
        Arrays.stream(mappedParams).forEach(mappedParam ->
                query.setParameter(mappedParam.getKey(), mappedParam.getValue()));
    }
}
