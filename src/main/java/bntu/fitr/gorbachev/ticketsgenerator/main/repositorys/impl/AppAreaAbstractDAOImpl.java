package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.AppAreaAbstractDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public abstract class AppAreaAbstractDAOImpl<T, ID> extends AbstractDAOImpl<T, ID> implements AppAreaAbstractDAO<T, ID> {

    private final String ENTITY_NAME_ARG = "entityName_arg";

    protected final String OFFSET_arg = "page_arg";

    protected final String ITEMS_ON_PAGE_arg = "itemsOnPage_arg";

    protected final BiFunction<Integer, Integer, Integer> calculateOffset = (page, itemsOnPage) -> itemsOnPage * (page - 1);

    // ------------ HQL entry ------------------------ //

    protected final String HQL_LIMIT = String.format("""
            LIMIT :%s OFFSET :%s
            """, ITEMS_ON_PAGE_arg, OFFSET_arg);

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

    private final String HQL_COUNT_FIND_LIKE_BY_NAME = String.format("""
            select count(*)
            %s
            """, HQL_FIND_LIKE_BY_NAME);

    private final String HQL_FIND_LIKE_BY_NAME_LIMIT = String.format("""
                    %s
                    %s
                    """,
            HQL_FIND_LIKE_BY_NAME,
            HQL_LIMIT);

    @Override
    @SuppressWarnings("unchecked")
    public Optional<T> findByName(String name) throws DAOException {
        return executor.executeSingleEntityQuery(
                HQL_FIND_BY_NAME,
                ENTITY_CLAZZ,
                Map.entry(ENTITY_NAME_ARG, name));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findLikeByName(String name) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_LIKE_BY_NAME,
                ENTITY_CLAZZ,
                Map.entry(ENTITY_NAME_ARG, String.join("", "%", name, "%"))
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countLikeByName(String name) throws DAOException {
        return executor.executeLongResult(HQL_COUNT_FIND_LIKE_BY_NAME,
                Map.entry(ENTITY_NAME_ARG, String.join("", "%", name, "%")));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findLikeByName(String name, int page, int itemsOnPage) throws DAOException {
        return executor.executeQuery(HQL_FIND_LIKE_BY_NAME_LIMIT, ENTITY_CLAZZ,
                Map.entry(ENTITY_NAME_ARG, String.join("", "%", name, "%")),
                Map.entry(ITEMS_ON_PAGE_arg, itemsOnPage),
                Map.entry(OFFSET_arg, calculateOffset.apply(page, itemsOnPage)));
    }
}
