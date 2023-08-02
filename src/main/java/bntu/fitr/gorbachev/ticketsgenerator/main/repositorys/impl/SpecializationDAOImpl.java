package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.SpecializationDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Specialization;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;

import java.util.List;
import java.util.UUID;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.extractEntityNameFromJakartaAnnEntity;

public class SpecializationDAOImpl extends AbstractDAOImpl<Specialization, UUID> implements SpecializationDAO {
    protected final String FACULTY_ID_ARG = "facultyId_arg";

    protected final String FACULTY_NAME_ARG = "facultyName_arg";

    @Override
    public List<Specialization> findByFacultyId(UUID facultyId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Specialization> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as s
                                    where s.faculty.id=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Specialization.class),
                            FACULTY_ID_ARG)
                    , Specialization.class);
            selectionQuery.setParameter(FACULTY_ID_ARG, facultyId);
            List<Specialization> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Specialization> findByFacultyName(String facultyName) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Specialization> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as s
                                    where s.faculty.name=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Specialization.class),
                            FACULTY_NAME_ARG)
                    , Specialization.class);
            selectionQuery.setParameter(FACULTY_NAME_ARG, facultyName);
            List<Specialization> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }
}
