package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;

import java.util.List;
import java.util.Optional;

/**
 * Basic apparition for any repositories independent of the application subject
 */
public interface AbstractDAO<T, ID> {
    ID create(T entity) throws DAOException;

    void delete(T entity) throws DAOException;

    void update(T entity) throws DAOException;

    Optional<T> findById(ID id) throws DAOException;

    List<T> findAll() throws DAOException;

    Optional<T> findAny() throws DAOException;

    long count() throws DAOException;

    HQueryMaster<T> getExecutor();
}
