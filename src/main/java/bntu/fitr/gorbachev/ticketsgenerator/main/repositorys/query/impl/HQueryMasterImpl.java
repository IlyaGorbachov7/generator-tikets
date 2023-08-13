package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.query.SelectionQuery;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.*;

public class HQueryMasterImpl<T> extends HQueryMaster<T> {

    @Override
    public List<T> executeQuery(String query, Class<?> resultClass,
                                Map.Entry<String, Object>... params) throws DAOException {
        Session session = getSession();
        try {
            beginTransaction(session);
            Query<?> selectionQuery = session.createQuery(query, resultClass);
            @SuppressWarnings("unchecked")
            List<T> resultList = (List<T>) selectionQuery.getResultList();
            commitTransaction(session);
            return resultList;
        } catch (PersistenceException e) {
            rollbackTransaction(session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<T> executeSelectionQuery(String selectQuery, Class<?> resultClass,
                                         Map.Entry<String, Object>... params) throws DAOException {
        Session session = getSession();
        try {
            beginTransaction(session);
            SelectionQuery<?> selectionQuery = session.createSelectionQuery(selectQuery, resultClass);
            @SuppressWarnings("unchecked")
            List<T> resultList = (List<T>) selectionQuery.getResultList();
            commitTransaction(session);
            return resultList;
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
    public <R> void setStatementParams(Query<R> query, Map.Entry<String, Object>... mappedParams) {
        Arrays.stream(mappedParams).forEach(mappedParam ->
                query.setParameter(mappedParam.getKey(), mappedParam.getValue()));
    }

    @Override
    public <R> void setStatementParams(SelectionQuery<R> query, Map.Entry<String, Object>... mappedParams) {
        Arrays.stream(mappedParams).forEach(mappedParam ->
                query.setParameter(mappedParam.getKey(), mappedParam.getValue()));
    }
}
