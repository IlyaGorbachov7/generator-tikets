package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;

import java.util.List;
import java.util.UUID;

public interface FacultyDAO extends AppAreaAbstractDAO<Faculty, UUID> {

    List<Faculty> findByUniversityId(UUID universityId) throws DAOException;

    long countByUniversityId(UUID universityId) throws DAOException;

    List<Faculty> findByUniversityName(String universityName) throws DAOException;

    long countByUniversityName(String universityName) throws DAOException;

    List<Faculty> findByLikeNameAndUniversityId(String name, UUID universityId) throws DAOException;

    long countByLikeNameAndUniversityId(String name, UUID universityId) throws DAOException;
}
