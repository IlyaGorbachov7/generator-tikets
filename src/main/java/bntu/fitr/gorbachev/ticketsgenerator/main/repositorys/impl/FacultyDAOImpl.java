package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.extractEntityNameFromJakartaAnnEntity;

public class FacultyDAOImpl extends AppAreaAbstractDAOImpl<Faculty, UUID> implements FacultyDAO {

    protected final String UNIVERSITY_ID_ARG = "universityId_arg";

    protected final String UNIVERSITY_NAME_ARG = "universityName_arg";

    // ------------ HQL entry ------------------------ //

    private final String HQL_FIND_BY_universityId = String.format("""
                    %s
                    where %s.university.id=:%s
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            UNIVERSITY_ID_ARG);

    private final String HQL_COUNT_FIND_BY_universityId = String.format("""
                    select count(*)
                    %s
                    """,
            HQL_FIND_BY_universityId);

    private final String HQL_FIND_BY_universityName = String.format("""
                    %s
                    where %s.university.id in
                                           (select u.id
                                            from %s as u
                                            where u.name=:%s)
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            extractEntityNameFromJakartaAnnEntity(University.class),
            UNIVERSITY_NAME_ARG);

    private final String HQL_COUNT_FIND_BY_universityName = String.format("""
                    select count(*)
                    %s
                    """,
            HQL_FIND_BY_universityName);

    private final String HQL_FIND_BY_NAME_AND_universityId = String.format("""
                    %s
                    where %s.university.id=:%s
                            and lower(%s.name) like lower(:%s)
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            UNIVERSITY_ID_ARG,
            ALLIES_TABLE,
            UNIVERSITY_NAME_ARG
    );

    private final String HQL_COUNT_FIND_BY_LIKE_NAME_AND_universityId = String.format("""
                    select count(*)
                    %s
                    """,
            HQL_FIND_BY_NAME_AND_universityId);

    @Override
    @SuppressWarnings("unchecked")
    public List<Faculty> findByUniversityId(UUID universityId) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_universityId,
                ENTITY_CLAZZ,
                Map.entry(UNIVERSITY_ID_ARG, universityId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByUniversityId(UUID universityId) throws DAOException {
        return executor.executeLongResult(HQL_COUNT_FIND_BY_universityId, Map.entry(UNIVERSITY_ID_ARG, universityId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Faculty> findByUniversityName(String universityName) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_universityName,
                ENTITY_CLAZZ,
                Map.entry(UNIVERSITY_NAME_ARG, universityName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByUniversityName(String universityName) throws DAOException {
        return executor.executeLongResult(HQL_COUNT_FIND_BY_universityName, Map.entry(UNIVERSITY_NAME_ARG, universityName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Faculty> findByLikeNameAndUniversityId(String name, UUID universityId) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_NAME_AND_universityId,
                ENTITY_CLAZZ,
                Map.entry(UNIVERSITY_ID_ARG, universityId),
                Map.entry(UNIVERSITY_NAME_ARG, String.join("", "%", name, "%"))
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByLikeNameAndUniversityId(String name, UUID universityId) throws DAOException {
        return executor.executeLongResult(HQL_COUNT_FIND_BY_LIKE_NAME_AND_universityId,
                Map.entry(UNIVERSITY_ID_ARG, universityId),
                Map.entry(UNIVERSITY_NAME_ARG, String.join("", "%", name, "%")));
    }
}
