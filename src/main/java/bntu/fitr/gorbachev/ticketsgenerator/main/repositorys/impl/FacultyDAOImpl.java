package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FacultyDAOImpl extends AbstractDAOImpl<Faculty, UUID> implements FacultyDAO {

    @Override
    public List<Faculty> findByUniversityId(UUID universityId) throws DAOException {
        return null;
    }
}
