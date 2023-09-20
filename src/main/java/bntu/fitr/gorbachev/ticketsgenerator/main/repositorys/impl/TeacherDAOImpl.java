package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.TeacherDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeacherDAOImpl extends AppAreaAbstractDAOImpl<Teacher, UUID> implements TeacherDAO {
    protected final String FACULTY_ID_ARG = "facultyId_arg";

    protected final String FACULTY_NAME_ARG = "facultyName_arg";

    private final String HQL_FIND_BY_facultyId = String.format("""
                    %s
                    where %s.faculty.id=:%s
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            FACULTY_ID_ARG);

    private final String HQL_COUNT_BY_facultyId = String.format("""
                    select count(*)
                    %s
                    """,
            HQL_FIND_BY_facultyId);

    private final String HQL_FIND_BY_facultyName = String.format("""
                    %s
                    where %s.faculty.name=:%s
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            FACULTY_NAME_ARG);

    private final String HQL_COUNT_BY_facultyName = String.format("""
                    select count(*)
                    %s
                    """,
            HQL_FIND_BY_facultyName);

    private final String HQL_FIND_BY_NAME_AND_facultyId = String.format("""
                    %s
                    where %s.faculty.id=:%s and
                          lower(%s.name) like lower(:%s)
                    """,
            HQL_SELECT,
            ALLIES_TABLE,
            FACULTY_ID_ARG,
            ALLIES_TABLE,
            FACULTY_NAME_ARG);

    private final String HQL_COUNT_BY_NAME_AND_facultyId = String.format("""
                    select count(*)
                    %s
                    """,
            HQL_FIND_BY_NAME_AND_facultyId);

    @Override
    @SuppressWarnings("unchecked")
    public List<Teacher> findByFacultyId(UUID facultyId) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_facultyId,
                ENTITY_CLAZZ,
                Map.entry(FACULTY_ID_ARG, facultyId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByFacultyId(UUID facultyId) throws DAOException {
        return executor.executeLongResult(HQL_COUNT_BY_facultyId,
                Map.entry(FACULTY_ID_ARG, facultyId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Teacher> findByFacultyName(String facultyName) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_facultyName,
                ENTITY_CLAZZ,
                Map.entry(FACULTY_NAME_ARG, facultyName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByFacultyName(String facultyName) throws DAOException {
        return executor.executeLongResult(HQL_COUNT_BY_facultyName,
                Map.entry(FACULTY_NAME_ARG, facultyName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Teacher> findByLikeNameAndFacultyId(String name, UUID facultyId) throws DAOException {
        return executor.executeQuery(
                HQL_FIND_BY_NAME_AND_facultyId,
                ENTITY_CLAZZ,
                Map.entry(FACULTY_ID_ARG, facultyId),
                Map.entry(FACULTY_NAME_ARG, String.join("", "%", name, "%")));
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countByLikeNameAndFacultyId(String name, UUID facultyId) throws DAOException {
        return executor.executeLongResult(HQL_COUNT_BY_NAME_AND_facultyId,
                Map.entry(FACULTY_ID_ARG, facultyId),
                Map.entry(FACULTY_NAME_ARG, String.join("", "%", name, "%")));
    }
}
