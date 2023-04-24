package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Discipline;

import java.util.List;

public interface DisciplineDAO extends AbstractDAO<Discipline> {
    Discipline findByName(String name) throws DAOException;

    List<Discipline> findBySpecializationId(Integer specializationId) throws DAOException;
}
