package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Discipline;

import java.util.List;
import java.util.UUID;

public interface DisciplineDAO extends AppAreaAbstractDAO<Discipline, UUID> {

    List<Discipline> findBySpecializationId(UUID specializationId) throws DAOException;

    List<Discipline> finBySpecializationName(String specializationName) throws DAOException;

    List<Discipline> findByLikeNameAndSpecializationId(String name, UUID specializationId) throws DAOException;
}
