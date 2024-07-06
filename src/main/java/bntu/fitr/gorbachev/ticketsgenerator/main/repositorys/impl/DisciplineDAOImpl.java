package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DisciplineDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Discipline;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DisciplineDAOImpl extends AppAreaAbstractDAOImpl<Discipline, UUID> implements DisciplineDAO {

    protected String SPECIALIZATION_ID_ARG = "specializationId_arg";

    protected String SPECIALIZATION_NAME_ARG = "specializationName_arg";

    // ------------ HQL entry ------------------------ //

    private final String HQL_FIND_BY_specializationId = String.format("""
                    %s
                    where %s.specialization.id=:%s
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            SPECIALIZATION_ID_ARG);

    private final String HQL_COUNT_FIND_BY_specializationId = String.format("""
                    select count(*)
                    %s
                    """,
            HQL_FIND_BY_specializationId);

    private final String HQL_FIND_BY_specializationName = String.format("""
                    %s
                    where %s.specialisation.name=:%s
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            SPECIALIZATION_NAME_ARG);

    private final String HQL_COUNT_FIND_BY_specializationName = String.format("""
                    select count(*)
                    %s
                    """,
            HQL_FIND_BY_specializationName);

    private final String HQL_FIND_BY_LIKE_NAME_AND_specializationId = String.format("""
                    %s
                    where %s.specialization.id=:%s
                    and lower(%s.name) like lower(:%s)
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            SPECIALIZATION_ID_ARG,
            ALLIES_TABLE,
            SPECIALIZATION_NAME_ARG);

    private final String HQL_COUNT_FIND_BY_LIKE_NAME_AND_specializationId = String.format("""
                    select count(*)
                    %s
                    """,
            HQL_FIND_BY_LIKE_NAME_AND_specializationId);

    private final String HQL_FIND_BY_specializationId_LIMIT = String.format("""
                    %s
                    %s
                    """,
            HQL_FIND_BY_specializationId,
            HQL_LIMIT);

    private final String HQL_FIND_BY_LIKE_NAME_AND_specializationId_LIMIT = String.format("""
                    %s
                    %s
                    """,
            HQL_FIND_BY_LIKE_NAME_AND_specializationId,
            HQL_LIMIT);

    @Override
    @SuppressWarnings("unchecked")
    public List<Discipline> findBySpecializationId(UUID specializationId) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_specializationId,
                ENTITY_CLAZZ,
                Map.entry(SPECIALIZATION_ID_ARG, specializationId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Discipline> findBySpecializationId(UUID specializationId, int page, int itemsOnPage) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_LIKE_NAME_AND_specializationId_LIMIT,
                ENTITY_CLAZZ,
                Map.entry(SPECIALIZATION_ID_ARG, specializationId),
                Map.entry(ITEMS_ON_PAGE_arg, itemsOnPage),
                Map.entry(OFFSET_arg, calculateOffset.apply(page, itemsOnPage)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countBySpecializationId(UUID specializationId) throws DAOException {
        return executor.executeLongResult(
                HQL_COUNT_FIND_BY_specializationId,
                Map.entry(SPECIALIZATION_ID_ARG, specializationId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Discipline> findBySpecializationName(String specializationName) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_specializationName,
                ENTITY_CLAZZ,
                Map.entry(SPECIALIZATION_NAME_ARG, specializationName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countBySpecializationName(String specializationName) throws DAOException {
        return executor.executeLongResult(
                HQL_COUNT_FIND_BY_specializationName,
                Map.entry(SPECIALIZATION_NAME_ARG, specializationName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Discipline> findByLikeNameAndSpecializationId(String name, UUID specializationId) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_LIKE_NAME_AND_specializationId,
                ENTITY_CLAZZ,
                Map.entry(SPECIALIZATION_ID_ARG, specializationId),
                Map.entry(SPECIALIZATION_NAME_ARG, String.join("", "%", name, "%")));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Discipline> findByLikeNameAndSpecializationId(String name, UUID specializationId, int page, int itemsOnPage) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_LIKE_NAME_AND_specializationId_LIMIT,
                ENTITY_CLAZZ,
                Map.entry(SPECIALIZATION_NAME_ARG, String.join("", "%", name, "%")),
                Map.entry(SPECIALIZATION_ID_ARG, specializationId),
                Map.entry(ITEMS_ON_PAGE_arg, itemsOnPage),
                Map.entry(OFFSET_arg, calculateOffset.apply(page, itemsOnPage)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByLikeNameAndSpecializationId(String name, UUID specializationId) throws DAOException {
        return executor.executeLongResult(
                HQL_COUNT_FIND_BY_LIKE_NAME_AND_specializationId,
                Map.entry(SPECIALIZATION_ID_ARG, specializationId),
                Map.entry(SPECIALIZATION_NAME_ARG, String.join("", "%", name, "%"))
        );
    }
}
