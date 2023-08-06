package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.TeacherDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Specialization;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO.extractEntityNameFromJakartaAnnEntity;

public class TeacherDAOImpl extends AbstractDAOImpl<Teacher, UUID> implements TeacherDAO {
    protected final String FACULTY_ID_ARG = "facultyId_arg";

    protected final String FACULTY_NAME_ARG = "facultyName_arg";

    @Override
    public List<Teacher> findByFacultyId(UUID facultyId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Teacher> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as th
                                    where th.faculty.id=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Teacher.class),
                            FACULTY_ID_ARG)
                    , Teacher.class);
            selectionQuery.setParameter(FACULTY_ID_ARG, facultyId);
            List<Teacher> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }

    }

    @Override
    public List<Teacher> finByFacultyName(String facultyName) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Teacher> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as th
                                    where th.faculty.name=:%s
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Teacher.class),
                            FACULTY_NAME_ARG)
                    , Teacher.class);
            selectionQuery.setParameter(FACULTY_NAME_ARG, facultyName);
            List<Teacher> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Teacher> findByNameAndFacultyId(String name, UUID facultyId) throws DAOException {
        Session session = getSession();
        boolean isActiveTrans = isActiveTransaction(session);
        try {
            beginTransaction(isActiveTrans, session);
            SelectionQuery<Teacher> selectionQuery = session.createSelectionQuery(String.format("""
                                    from %s as t
                                    where t.faculty.id=:%s and
                                          lower(t.name) like lower(:%s)
                                    """,
                            extractEntityNameFromJakartaAnnEntity(Teacher.class),
                            FACULTY_ID_ARG,
                            FACULTY_NAME_ARG),
                    Teacher.class);
            selectionQuery.setParameter(FACULTY_ID_ARG, facultyId)
                    .setParameter(FACULTY_NAME_ARG, String.join("", "%", name, "%"));
            List<Teacher> res = selectionQuery.getResultList();
            commitTransaction(isActiveTrans, session);
            return res;
        } catch (PersistenceException e) {
            rollbackTransaction(isActiveTrans, session);
            throw new DAOException(e);
        }    }
}
