package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeacherService {
    TeacherDto create(TeacherCreateDto teacherCreateDto) throws ServiceException;

    TeacherDto update(TeacherDto teacherDto) throws ServiceException;

    void delete(TeacherDto teacherDto) throws ServiceException;

    Optional<TeacherDto> getAny() throws ServiceException;

    List<TeacherDto> getAll() throws ServiceException;

    List<TeacherDto> getByFacultyId(UUID facultyId) throws ServiceException;

    List<TeacherDto> getByFacultyName(String facultyName) throws ServiceException;

    List<TeacherDto> getByLikeNameAndFacultyId(String likeName, UUID facultyId) throws ServiceException;

}
