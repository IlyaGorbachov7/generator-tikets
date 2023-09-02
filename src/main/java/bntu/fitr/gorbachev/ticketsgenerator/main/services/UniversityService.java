package bntu.fitr.gorbachev.ticketsgenerator.main.services;


import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;

// Hibernate Validator same should be used !!!!!!!!!
public interface UniversityService {

    UniversityDTO create(UniversityCreateDto universityCreateDto) throws ServiceException;

    UniversityDTO update(UniversityDTO universityDto) throws ServiceException;

    void delete(UniversityDTO universityDTO) throws ServiceException;

    Optional<UniversityDTO> getAny() throws ServiceException;

    List<UniversityDTO> getAll() throws ServiceException;

    List<UniversityDTO> getLimitedQuantity(int page, int quantity) throws ServiceException;

    List<UniversityDTO> getByLikeName(String likeName);
}
