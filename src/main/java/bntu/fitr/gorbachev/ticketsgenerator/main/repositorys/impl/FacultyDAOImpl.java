package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.extractEntityNameFromJakartaAnnEntity;

public class FacultyDAOImpl extends AbstractDAOImpl<Faculty, UUID> implements FacultyDAO {

    protected final String UNIVERSITY_ID_ARG = "universityId_arg";

    protected final String UNIVERSITY_NAME_ARG = "universityName_arg";

    @Override
    public List<Faculty> findByUniversityId(UUID universityId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Faculty> selectionQuery = session.createSelectionQuery(
                    String.format("""
                                    from %s as f
                                    where f.university.id=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Faculty.class), UNIVERSITY_ID_ARG),
                    Faculty.class);
            selectionQuery.setParameter(UNIVERSITY_ID_ARG, universityId);
            List<Faculty> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Faculty> findByUniversityName(String universityName) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Faculty> selectionQuery = session.createSelectionQuery(
                    String.format("""
                                    from %s as f
                                    where f.university.id in
                                                           (select u.id
                                                            from %s as u
                                                            where u.name=:%s)
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Faculty.class),
                            extractEntityNameFromJakartaAnnEntity(University.class), UNIVERSITY_NAME_ARG),
                    Faculty.class
            );
            selectionQuery.setParameter(UNIVERSITY_NAME_ARG, universityName);
            List<Faculty> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Faculty> findByLikeNameAndUniversityId(String name, UUID universityId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Faculty> selectionQuery = session.createSelectionQuery(String.format("""
                            from %s as f
                            where f.university.id=:%s
                                    and lower(f.name) like lower(:%s)
                            """,
                    extractEntityNameFromJakartaAnnEntity(Faculty.class),
                    UNIVERSITY_ID_ARG, UNIVERSITY_NAME_ARG
            ), Faculty.class);
            selectionQuery.setParameter(UNIVERSITY_ID_ARG, universityId)
                    .setParameter(UNIVERSITY_NAME_ARG, String.join("", "%", name, "%"));
            List<Faculty> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }
}
