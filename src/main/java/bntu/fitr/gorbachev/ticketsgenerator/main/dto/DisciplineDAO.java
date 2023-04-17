package bntu.fitr.gorbachev.ticketsgenerator.main.dto;

import bntu.fitr.gorbachev.ticketsgenerator.main.dto.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.dto.tablentity.Discipline;

import java.util.List;

public interface DisciplineDAO extends AbstractDAO<Discipline> {
    Discipline findByName(String name) throws DAOException;

    List<Discipline> findBySpecializationId(Integer specializationId) throws DAOException;
}
