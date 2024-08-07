package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.other.PaginationParam;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeacherService {
    TeacherDto create(TeacherCreateDto teacherCreateDto) throws ServiceException;

    TeacherSimpleDto createSmpl(TeacherCreateDto teacherCreateDto) throws ServiceException;

    TeacherDto update(TeacherDto teacherDto) throws ServiceException;

    TeacherSimpleDto update(TeacherSimpleDto dto) throws ServiceException;

    void delete(TeacherDto teacherDto) throws ServiceException;

    void deleteSmpl(TeacherSimpleDto dto) throws ServiceException;

    void deleteSmpl(List<TeacherSimpleDto> list) throws ServiceException;

    Optional<TeacherDto> getAny() throws ServiceException;

    Optional<TeacherSimpleDto> getSmplAny() throws ServiceException;

    List<TeacherDto> getAll() throws ServiceException;

    Optional<TeacherDto> getByName(String name) throws ServiceException;

    Optional<TeacherSimpleDto> getSmplByName(String name) throws ServiceException;

    List<TeacherDto> getByFacultyId(UUID facultyId) throws ServiceException;

    List<TeacherDto> getByFacultyId(UUID facultyId, int page, int itemsOnPage) throws ServiceException;

    List<TeacherSimpleDto> getSmplByFacultyId(UUID facultyID, int page, int itemsOnPage) throws ServiceException;

    long countByFacultyId(UUID facultyId) throws ServiceException;

    List<TeacherDto> getByFacultyName(String facultyName) throws ServiceException;

    long countByFacultyName(String facultyName) throws ServiceException;

    List<TeacherDto> getByLikeNameAndFacultyId(String likeName, UUID facultyId) throws ServiceException;

    List<TeacherDto> getByLikeNameAndFacultyId(String likeName, UUID facultyId, int page, int itemsOnPage) throws ServiceException;

    List<TeacherSimpleDto> getSmplByLikeNameAndFacultyId(String likeName, UUID facultyId, int page, int itemsOnPage) throws ServiceException;

    long countByLikeNameAndFacultyId(String likeName, UUID facultyId) throws ServiceException;

    List<TeacherSimpleDto> getSmplByFacultyId(UUID id);

    PaginationParam calculatePageParam(int itemsOnPage, int currentPage, String filterText, UUID facultyId);
}
