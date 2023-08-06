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
    protected final String DEPARTMENT_ID_ARG = "departmentId_arg";

    protected final String DEPARTMENT_NAME_ARG = "departmentName_arg";

    @Override
    public List<Specialization> findByDepartmentId(UUID departmentId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Specialization> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as s
                                    where s.department.id=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Specialization.class),
                            DEPARTMENT_ID_ARG)
                    , Specialization.class);
            selectionQuery.setParameter(DEPARTMENT_ID_ARG, departmentId);
            List<Specialization> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Specialization> findByDepartmentName(String departmentName) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Specialization> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as s
                                    where s.department.name=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Specialization.class),
                            DEPARTMENT_NAME_ARG),
                    Specialization.class);
            selectionQuery.setParameter(DEPARTMENT_NAME_ARG, departmentName);
            List<Specialization> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Specialization> findByLikeNameAndDepartmentId(String name, UUID departmentId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Specialization> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as s
                                    where s.department.id=:%s
                                    and lower(f.name) like lower(:%s)
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Specialization.class),
                            DEPARTMENT_ID_ARG,
                            DEPARTMENT_NAME_ARG),
                    Specialization.class);
            selectionQuery.setParameter(DEPARTMENT_ID_ARG, departmentId)
                    .setParameter(DEPARTMENT_NAME_ARG, String.join("", "%", name, "%"));
            List<Specialization> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }
}
