package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Discipline;

import java.util.List;
import java.util.UUID;

public interface DisciplineDAO extends AbstractDAO<Discipline, UUID> {
    Discipline findByName(String name) throws DAOException;

    List<Discipline> findBySpecializationId(UUID specializationId) throws DAOException;
}
