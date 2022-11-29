package bntu.fitr.gorbachev.ticketsgenerator.main.dto;

import bntu.fitr.gorbachev.ticketsgenerator.main.dto.exception.DAOException;

import java.util.List;

public interface AbstractDAO<T> {
    Integer create(T entity) throws DAOException;

    Integer delete(Integer id) throws DAOException;

    Integer delete(T entity) throws DAOException;

    boolean update(T entity) throws DAOException;

    T findById(Integer id) throws DAOException;

    List<T> findAll() throws DAOException;

}
