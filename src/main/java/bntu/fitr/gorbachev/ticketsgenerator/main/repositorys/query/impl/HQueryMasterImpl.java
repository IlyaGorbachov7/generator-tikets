package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.*;

public class HQueryMasterImpl<T> extends HQueryMaster<T> {

    @Override
    @SuppressWarnings("unchecked")
    public <R> List<T> executeQuery(String queryStr, Class<R> resultClass,
                                    Map.Entry<String, Object>... params) throws DAOException {
        Session session = getSession();
        try {
            beginTransaction(session);
            Query<?> query = session.createQuery(queryStr, resultClass);
            setStatementParams((Query<T>) query, params);
            @SuppressWarnings("unchecked")
            List<T> resultList = (List<T>) query.getResultList();
            commitTransaction(session);
            return resultList;
        } catch (PersistenceException e) {
            rollbackTransaction(session);
            throw new DAOException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Optional<T> executeSingleEntityQuery(String queryStr, Class<R> resultClass,
                                                Map.Entry<String, Object>... params) throws DAOException {
        Session session = getSession();
        try {
            beginTransaction(session);
            Query<?> query = session.createQuery(queryStr, resultClass);
                    setStatementParams((Query<T>) query, params);
            @SuppressWarnings("unchecked")
            T entity = (T) query.getSingleResultOrNull();
            commitTransaction(session);
            return Optional.ofNullable(entity);
        } catch (PersistenceException e) {
            rollbackTransaction(session);
            throw new DAOException(e);
        }
    }

    @Override
    public <ID> ID persist(T entity) throws DAOException {
        Session session = getSession();
        beginTransaction(session);
        try {
            session.persist(entity);
            commitTransaction(session);
        } catch (PersistenceException e) {
            rollbackTransaction(session);
            throw new DAOException(e);
        }
        return getValueFromFieldFindByAnnotation(entity, Id.class);
    }

    @Override
    public void delete(T entity) throws DAOException {
        Session session = getSession();
        try {
            beginTransaction(session);
            session.remove(entity);
        } catch (PersistenceException e) {
            rollbackTransaction(session);
            throw new DAOException(e);
        }
    }

    @Override
    public void update(T entity) throws DAOException {
        Session session = getSession();
        try {
            beginTransaction(session);
            session.refresh(entity);
            commitTransaction(session);
        } catch (PersistenceException e) {
            rollbackTransaction(session);
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
