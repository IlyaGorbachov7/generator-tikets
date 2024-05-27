package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.TeacherDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Teacher;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.TeacherService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.tchr.TeacherNoFoundByIdException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.TeacherMapper;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl.MapperFactoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TeacherServiceImpl implements TeacherService {

    TeacherDAO teacherRepo = RepositoryFactoryImpl.getInstance().repositoryTeacher();

    TeacherMapper teacherMapper = MapperFactoryImpl.getInstance().teacherMapper();

    HQueryMaster<Teacher> executor = teacherRepo.getExecutor();

    @Override
    public TeacherDto create(TeacherCreateDto teacherCreateDto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            Teacher entity = teacherMapper.teacherDtoToEntity(teacherCreateDto);
            teacherRepo.create(entity);
            return teacherMapper.teacherToDto(entity);
        });
    }

    @Override
    public TeacherSimpleDto createSmpl(TeacherCreateDto teacherCreateDto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            Teacher entity = teacherMapper.teacherDtoToEntity(teacherCreateDto);
            teacherRepo.create(entity);
            return teacherMapper.teacherToSimpleDto(entity);
        });
    }

    @Override
    public TeacherDto update(TeacherDto teacherDto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> {
            Teacher target = teacherRepo.findById(teacherDto.getId()).orElseThrow(TeacherNoFoundByIdException::new);
            teacherMapper.update(target, teacherDto);
            teacherRepo.update(target);
            return teacherMapper.teacherToDto(target);
        });
    }

    @Override
    public void delete(TeacherDto teacherDto) throws ServiceException {
        executor.wrapTransactional(() -> {
            Teacher entity = teacherRepo.findById(teacherDto.getId()).orElseThrow(TeacherNoFoundByIdException::new);
            teacherRepo.delete(entity);
        });
    }

    @Override
    public void deleteSmpl(TeacherSimpleDto dto) throws ServiceException {
        executor.wrapTransactional(() -> teacherRepo.delete(teacherRepo.findById(dto.getId())
                .orElseThrow(TeacherNoFoundByIdException::new)));
    }

    @Override
    public void deleteSmpl(List<TeacherSimpleDto> list) throws ServiceException {
        executor.wrapTransactional(()-> list.forEach(this::deleteSmpl));
    }

    @Override
    public Optional<TeacherDto> getAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                teacherRepo.findAny().map(teacherMapper::teacherToDto));
    }

    @Override
    public Optional<TeacherSimpleDto> getSmplAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                teacherRepo.findAny().map(teacherMapper::teacherToSimpleDto));
    }

    @Override
    public List<TeacherDto> getAll() throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                teacherRepo.findAll().stream().map(teacherMapper::teacherToDto).toList());
    }

    @Override
    public Optional<TeacherDto> getByName(String name) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                teacherRepo.findByName(name).map(teacherMapper::teacherToDto));
    }

    @Override
    public Optional<TeacherSimpleDto> getSmplByName(String name) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                teacherRepo.findByName(name).map(teacherMapper::teacherToSimpleDto));
    }

    @Override
    public List<TeacherDto> getByFacultyId(UUID facultyId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                teacherRepo.findByFacultyId(facultyId).stream().map(
                        teacherMapper::teacherToDto).toList());
    }

    @Override
    public long countByFacultyId(UUID facultyId) throws ServiceException {
        return teacherRepo.countByFacultyId(facultyId);
    }

    @Override
    public List<TeacherDto> getByFacultyName(String facultyName) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                teacherRepo.findByFacultyName(facultyName).stream().map(
                        teacherMapper::teacherToDto).toList());
    }

    @Override
    public long countByFacultyName(String facultyName) throws ServiceException {
        return teacherRepo.countByFacultyName(facultyName);
    }

    @Override
    public List<TeacherDto> getByLikeNameAndFacultyId(String likeName, UUID facultyId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                teacherRepo.findByLikeNameAndFacultyId(likeName, facultyId).stream().map(
                        teacherMapper::teacherToDto).toList());
    }

    @Override
    public long countByLikeNameAndFacultyId(String likeName, UUID facultyId) throws ServiceException {
        return teacherRepo.countByLikeNameAndFacultyId(likeName, facultyId);
    }

    @Override
    public List<TeacherSimpleDto> getSmplByFacultyId(UUID id) {
        return executor.wrapTransactionalResultList(() ->
                teacherRepo.findByFacultyId(id).stream().map(
                        teacherMapper::teacherToSimpleDto).toList());

    }
}
