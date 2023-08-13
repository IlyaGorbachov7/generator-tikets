package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.AppAreaAbstractDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AppAreaAbstractDAOImpl<T, ID> extends AbstractDAOImpl<T, ID> implements AppAreaAbstractDAO<T, ID> {

    private final String ENTITY_NAME_ARG = "entityName_arg";

    // ------------ HQL entry ------------------------ //

    private final String HQL_FIND_BY_NAME = String.format("""
                    %s
                    where lower(%s.name)=lower(:%s)
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            ENTITY_NAME_ARG);

    private final String HQL_FIND_LIKE_BY_NAME = String.format("""
                    %s
                    where lower(%s.name) like lower(:%s)
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            ENTITY_NAME_ARG);

    @Override
    public Optional<T> findByName(String name) throws DAOException {
        return executor.executeSingleEntityQuery(
                HQL_FIND_BY_NAME,
                ENTITY_CLAZZ,
                Map.entry(ENTITY_NAME_ARG, name));
    }

    @Override
    public List<T> findLikeByName(String name) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_LIKE_BY_NAME,
                ENTITY_CLAZZ,
                Map.entry(ENTITY_NAME_ARG, String.join("", "%", name, "%"))
        );
    }
}
