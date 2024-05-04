package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;

import java.util.List;
import java.util.Optional;

/**
 * Repository presents base operations in within of subject of the application
 */
public interface AppAreaAbstractDAO<T, ID> extends AbstractDAO<T, ID> {
    Optional<T> findByName(String name) throws DAOException;

    List<T> findLikeByName(String name) throws DAOException;

    long countLikeByName(String name) throws DAOException;
}