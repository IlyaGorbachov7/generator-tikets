package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.AbstractDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.PoolConnection;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO;
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
        try (Session session = poolConnection.openSession();) {
            Transaction tran = session.beginTransaction();
            Class<?> entityClazz = extractEntityClassFromDao(this.getClass());
            SelectionQuery<?> selectionQuery = session.createSelectionQuery(String.format("from %s",
                    Objects.requireNonNull(extractEntityNameFromJakartaAnnEntity(entityClazz)), entityClazz));
            tran.commit();
            return (List<T>) selectionQuery.list();
        } catch (ConnectionPoolException e) {
            log.warn(e.getCause().getMessage());
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<T> findAny() throws DAOException {
        try (Session session = poolConnection.openSession()) {
            Transaction trans = session.beginTransaction();
            Class<?> entityClazz = extractEntityClassFromDao(this.getClass());
            SelectionQuery<?> selectionQuery = session.createSelectionQuery(String.format("""
                            from %s
                            limit 1
                            """,
                    Objects.requireNonNull(extractEntityNameFromJakartaAnnEntity(entityClazz)), entityClazz));
            trans.commit();
            return (Optional<T>) selectionQuery.stream().findAny();
        } catch (ConnectionPoolException e) {
            log.warn(e.getCause().getMessage());
            throw new DAOException(e);
        }
    }
}
