package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultySimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FacultyService {

    FacultyDto create(FacultyCreateDto facultyCreateDto) throws ServiceException;

    FacultySimpleDto createSmpl(FacultyCreateDto facultyCreateDto) throws ServiceException;

    FacultyDto update(FacultyDto facultyDto) throws ServiceException;

    void delete(FacultyDto facultyDto) throws ServiceException;

    Optional<FacultySimpleDto> getSmplByName(String name) throws ServiceException;

    Optional<FacultyDto> getByName(String name) throws ServiceException;

    long countByName(String name) throws ServiceException;

    Optional<FacultySimpleDto> getSmplAny() throws ServiceException;

    Optional<FacultyDto> getAny() throws ServiceException;

    List<FacultyDto> getAll() throws ServiceException;

    long count() throws ServiceException;

    List<FacultyDto> getByUniversityId(UUID universityId) throws ServiceException;

    List<FacultySimpleDto> getSmplByUniversityId(UUID university) throws ServiceException;

    long countByUniversityId(UUID universityId) throws ServiceException;

    List<FacultyDto> getByUniversityName(String universityName) throws ServiceException;

    long countByUniversityName(String universityName) throws ServiceException;

    List<FacultyDto> getByLikeNameAndUniversity(String likeName, UUID universityId) throws ServiceException;

    long countByLikeNameAndUniversity(String likeName, UUID universityId) throws ServiceException;
}