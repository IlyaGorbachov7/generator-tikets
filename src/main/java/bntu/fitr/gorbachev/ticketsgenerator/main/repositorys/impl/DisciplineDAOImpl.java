package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DisciplineDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Discipline;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DisciplineDAOImpl extends AbstractDAOImpl<Discipline, UUID> implements DisciplineDAO {

    @Override
    public List<Discipline> findBySpecializationId(UUID specializationId) throws DAOException {
        return null;
    }
}
