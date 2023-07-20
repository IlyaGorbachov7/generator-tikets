package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys;

import bntu.fitr.gorbachev.ticketsgenerator.main.models.Question2;
import bntu.fitr.gorbachev.ticketsgenerator.main.models.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;

import java.beans.Customizer;
import java.util.UUID;
import java.util.function.Supplier;

public interface UniversityDAO extends AbstractDAO<University, UUID>, Supplier<Ticket<Question2>>, Customizer {
    University findByName(String name) throws DAOException;
}
