package bntu.fitr.gorbachev.ticketsgenerator.main.services;


import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.UniversityCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

// Hibernate Validator same should be used !!!!!!!!!
public interface UniversityService {

    UniversityDTO create(UniversityCreateDto universityCreateDto) throws ServiceException;

    void update(University universityDto) throws ServiceException;

    void delete(UniversityDTO universityDTO) throws ServiceException;

}
