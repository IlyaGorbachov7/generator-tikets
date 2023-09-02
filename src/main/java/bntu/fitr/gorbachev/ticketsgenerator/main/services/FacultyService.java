package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FacultyService {

    FacultyDto create(FacultyCreateDto facultyCreateDto) throws ServiceException;

    FacultyDto update(FacultyDto facultyDto) throws ServiceException;

    void delete(FacultyDto facultyDto) throws ServiceException;

    Optional<FacultyDto> getAny() throws ServiceException;

    List<FacultyDto> getAll() throws ServiceException;

    List<FacultyDto> getByUniversityId(UUID universityId) throws ServiceException;

    List<FacultyDto> getByUniversityName(String universityName) throws ServiceException;

    List<FacultyDto> getByLikeNameAndUniversity(String likeName, UUID universityId) throws ServiceException;
}