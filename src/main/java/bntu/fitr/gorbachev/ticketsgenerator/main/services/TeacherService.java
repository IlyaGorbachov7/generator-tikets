package bntu.fitr.gorbachev.ticketsgenerator.main.services;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    TeacherDto create(TeacherCreateDto teacherCreateDto) throws ServiceException;

    TeacherDto update(TeacherDto teacherDto) throws ServiceException;

    void delete(TeacherDto teacherDto) throws ServiceException;

    Optional<TeacherDto> getAny() throws ServiceException;

    List<TeacherDto> getAll() throws ServiceException;

}
