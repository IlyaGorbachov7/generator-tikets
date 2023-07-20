package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;

import java.util.List;

public interface AbstractDAO<T, ID> {
    ID create(T entity) throws DAOException;

    void delete(T entity) throws DAOException;

    void update(T entity) throws DAOException;

    T findById(ID id) throws DAOException;

    List<T> findAll() throws DAOException;

}
