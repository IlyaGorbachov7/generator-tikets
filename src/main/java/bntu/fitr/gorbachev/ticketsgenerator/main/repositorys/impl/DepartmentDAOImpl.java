package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DepartmentDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.extractEntityNameFromJakartaAnnEntity;

public class DepartmentDAOImpl extends AbstractDAOImpl<Department, UUID> implements DepartmentDAO {

    protected final String FACULTY_ID_ARG = "facultyId_arg";

    protected final String FACULTY_NAME_ARG = "facultyName_arg";

    @Override
    public List<Department> findByFacultyId(UUID facultyId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Department> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as d
                                    where d.faculty.id=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Department.class),
                            FACULTY_ID_ARG),
                    Department.class);
            selectionQuery.setParameter(FACULTY_ID_ARG, facultyId);
            List<Department> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Department> findByFacultyName(String facultyName) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Department> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as d
                                    where d.faculty.name=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Department.class),
                            FACULTY_NAME_ARG),
                    Department.class);
            selectionQuery.setParameter(FACULTY_NAME_ARG, facultyName);
            List<Department> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }


    }

    @Override
    public List<Department> findByLikeNameAndFacultyId(String name, UUID facultyId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Department> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as d
                                    where d.faculty.id=:%s and
                                          lower(d.name) like lower(:%s)
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Department.class),
                            FACULTY_ID_ARG,
                            FACULTY_NAME_ARG),
                    Department.class);
            selectionQuery.setParameter(FACULTY_ID_ARG, facultyId)
                    .setParameter(FACULTY_NAME_ARG, String.join("", "%", name, "%"));
            List<Department> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }
}
