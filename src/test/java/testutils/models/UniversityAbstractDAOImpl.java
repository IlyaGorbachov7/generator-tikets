package testutils.models;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.AbstractDAOImpl;
import jakarta.persistence.Column;

public class UniversityAbstractDAOImpl<Td, URLD> extends AbstractDAOImpl<Td, URLD> {

    @Column
    private Object field;

    @Column
    private Object field1;

}
