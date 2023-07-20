package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.AbstractDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.PoolConnection;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public abstract class AbstractDAOImpl<T, ID> implements AbstractDAO<T, ID> {
    protected final PoolConnection poolConnection = PoolConnection.Builder.build();

    @Override
    public ID create(T entity) throws DAOException {
        try (Session session = poolConnection.openSession()) {
            Transaction tran = session.beginTransaction();
            session.persist(entity);
            tran.commit();
        } catch (ConnectionPoolException e) {
            log.warn(e.getCause().getMessage());
            throw new DAOException(e);
        }
        return null;
    }

    @Override
    public void delete(T entity) throws DAOException {
        try (Session session = poolConnection.openSession();) {
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
        try (Session session = poolConnection.openSession();) {
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
        try (Session session = poolConnection.openSession();) {
            Transaction tran = session.beginTransaction();
            T entity = session.get(ReflectionHelperDAO.extractEntityClassFromDao(this.getClass()), id);
            tran.commit();
            return entity;
        } catch (ConnectionPoolException e) {
            log.warn(e.getCause().getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public List<T> findAll() throws DAOException {
        return null;
    }
}
