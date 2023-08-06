package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DisciplineDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Discipline;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;

import java.util.Dictionary;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.extractEntityNameFromJakartaAnnEntity;

public class DisciplineDAOImpl extends AbstractDAOImpl<Discipline, UUID> implements DisciplineDAO {

    protected String SPECIALIZATION_ID_ARG = "specializationId_arg";

    protected String SPECIALIZATION_NAME_ARG = "specializationName_arg";

    @Override
    public List<Discipline> findBySpecializationId(UUID specializationId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Discipline> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as dis
                                    where dis.specialization.id=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Discipline.class),
                            SPECIALIZATION_ID_ARG),
                    Discipline.class);
            selectionQuery.setParameter(SPECIALIZATION_ID_ARG, specializationId);
            List<Discipline> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Discipline> finBySpecializationName(String specializationName) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Discipline> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as dis
                                    where dis.specialisation.name=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Discipline.class),
                            SPECIALIZATION_NAME_ARG),
                    Discipline.class);
            selectionQuery.setParameter(SPECIALIZATION_NAME_ARG, specializationName);
            List<Discipline> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Discipline> findByLikeNameAndSpecializationId(String name, UUID specializationId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Discipline> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as dis
                                    where dis.specialization.id=:%s
                                    and lower(dis.name) like lower(:%s)
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Discipline.class),
                            SPECIALIZATION_ID_ARG,
                            SPECIALIZATION_NAME_ARG),
                    Discipline.class);
            selectionQuery.setParameter(SPECIALIZATION_ID_ARG, specializationId)
                    .setParameter(SPECIALIZATION_NAME_ARG, String.join("", "%", name, "%"));
            List<Discipline> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }
}
