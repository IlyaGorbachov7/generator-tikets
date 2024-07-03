package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;

import java.util.List;
import java.util.UUID;

public interface TeacherDAO extends AppAreaAbstractDAO<Teacher, UUID> {

    List<Teacher> findByFacultyId(UUID facultyId) throws DAOException;

    List<Teacher> findByFacultyId(UUID facultyId, int page, int itemsOnPage) throws DAOException;

    long countByFacultyId(UUID facultyId) throws DAOException;

    List<Teacher> findByFacultyName(String facultyName) throws DAOException;

    long countByFacultyName(String facultyName) throws DAOException;

    List<Teacher> findByLikeNameAndFacultyId(String name, UUID facultyId) throws DAOException;

    List<Teacher> findByLikeNameAndFacultyId(String name, UUID facultyId, int page, int itemsOnPage) throws DAOException;

    long countByLikeNameAndFacultyId(String name, UUID facultyId) throws DAOException;
}
