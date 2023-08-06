package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.HeadDepartmentDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.HeadDepartment;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Specialization;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;

import java.util.List;
import java.util.UUID;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.extractEntityNameFromJakartaAnnEntity;

public class HeadDepartmentDAOImpl extends AbstractDAOImpl<HeadDepartment, UUID> implements HeadDepartmentDAO {
    protected final String DEPARTMENT_ID_ARG = "departmentId_arg";

    protected final String DEPARTMENT_NAME_ARG = "departmentName_arg";

    @Override
    public List<HeadDepartment> findByDepartmentId(UUID departmentId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<HeadDepartment> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as hd
                                    where hd.department.id=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(HeadDepartment.class),
                            DEPARTMENT_ID_ARG)
                    , HeadDepartment.class);
            selectionQuery.setParameter(DEPARTMENT_ID_ARG, departmentId);
            List<HeadDepartment> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<HeadDepartment> findByDepartmentName(String departmentName) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<HeadDepartment> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as hd
                                    where hd.department.name=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(HeadDepartment.class),
                            DEPARTMENT_NAME_ARG)
                    , HeadDepartment.class);
            selectionQuery.setParameter(DEPARTMENT_NAME_ARG, departmentName);
            List<HeadDepartment> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<HeadDepartment> findByLikeNameAndDepartmentName(String name, UUID departmentId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<HeadDepartment> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as hd
                                    where hd.department.id=:%s
                                    and lower(f.name) like lower(:%s)
                                    """,
                            extractEntityNameFromJakartaAnnEntity(HeadDepartment.class),
                            DEPARTMENT_ID_ARG,
                            DEPARTMENT_NAME_ARG),
                    HeadDepartment.class);
            selectionQuery.setParameter(DEPARTMENT_ID_ARG, departmentId)
                    .setParameter(DEPARTMENT_NAME_ARG, String.join("", "%", name, "%"));
            List<HeadDepartment> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }


}
