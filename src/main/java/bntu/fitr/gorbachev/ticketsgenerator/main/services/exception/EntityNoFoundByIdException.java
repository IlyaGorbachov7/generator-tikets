package bntu.fitr.gorbachev.ticketsgenerator.main.services.exception;

public class EntityNoFoundByIdException extends ServiceException {
    public EntityNoFoundByIdException() {
        super("Entity don't found by ID");
    }

    public EntityNoFoundByIdException(String entityName) {
        super(String.format("%s don't found by ID", entityName));
    }
}
