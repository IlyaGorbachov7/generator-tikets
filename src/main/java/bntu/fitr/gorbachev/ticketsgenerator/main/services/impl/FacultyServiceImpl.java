package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.FacultyService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultySimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.fclt.FacultyNoFoundByIdException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.FacultyMapper;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl.MapperFactoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FacultyServiceImpl implements FacultyService {

    private final FacultyDAO facultyRepo = RepositoryFactoryImpl.getInstance().repositoryFaculty();
    private final FacultyMapper facultyMapper = MapperFactoryImpl.getInstance().facultyMapper();
    private final HQueryMaster<Faculty> executor = facultyRepo.getExecutor();

    @Override
    public FacultyDto create(FacultyCreateDto facultyCreateDto) throws ServiceException {
        Faculty faculty = facultyMapper.facultyDtoToFaculty(facultyCreateDto);
        facultyRepo.create(faculty);
        return facultyMapper.facultyToFacultyDto(faculty);
    }

    @Override
    public FacultyDto update(FacultyDto facultyDto) throws ServiceException {
        return facultyMapper.facultyToFacultyDto(
                executor.wrapTransactionalEntitySingle(() -> {
                    Faculty faculty = facultyRepo.findById(facultyDto.getId())
                            .orElseThrow(FacultyNoFoundByIdException::new);
                    facultyMapper.update(faculty, facultyDto);
                    facultyRepo.update(faculty);
                    return faculty;
                }));
    }

    @Override
    public void delete(FacultyDto facultyDto) throws ServiceException {
        executor.wrapTransactional(() -> {
            Faculty faculty = facultyRepo.findById(facultyDto.getId())
                    .orElseThrow(FacultyNoFoundByIdException::new);
            facultyRepo.delete(faculty);
        });
    }

    @Override
    public Optional<FacultySimpleDto> getSmplByName(String name) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                facultyRepo.findByName(name).map(facultyMapper::facultyToFacultySmplDto));
    }

    @Override
    public Optional<FacultyDto> getAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> facultyRepo.findAny().map(facultyMapper::facultyToFacultyDto));
    }

    @Override
    public Optional<FacultySimpleDto> getSmplAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> facultyRepo.findAny().map(facultyMapper::facultyToFacultySmplDto));
    }

    @Override
    public List<FacultyDto> getAll() throws ServiceException {
        return executor.wrapTransactionalResultList(() -> facultyMapper.facultyToFacultyDto(facultyRepo.findAll()));
    }

    @Override
    public long count() throws ServiceException {
        return facultyRepo.count();
    }

    @Override
    public List<FacultyDto> getByUniversityId(UUID universityId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                facultyMapper.facultyToFacultyDto(
                        facultyRepo.findByUniversityId(universityId)));
    }

    @Override
    public long countByUniversityId(UUID universityId) throws ServiceException {
        return facultyRepo.countByUniversityId(universityId);
    }

    @Override
    public List<FacultyDto> getByUniversityName(String universityName) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                facultyMapper.facultyToFacultyDto(
                        facultyRepo.findByUniversityName(universityName)));
    }

    @Override
    public long countByUniversityName(String universityName) throws ServiceException {
        return facultyRepo.countByUniversityName(universityName);

    }

    @Override
    public List<FacultyDto> getByLikeNameAndUniversity(String likeName, UUID universityId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                facultyMapper.facultyToFacultyDto(
                        facultyRepo.findByLikeNameAndUniversityId(likeName, universityId)));
    }

    @Override
    public Optional<FacultyDto> getByName(String name) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() ->
                facultyRepo.findByName(name).map(facultyMapper::facultyToFacultyDto));
    }

    @Override
    public long countByName(String name) throws ServiceException {
        return facultyRepo.countLikeByName(name);
    }

    @Override
    public long countByLikeNameAndUniversity(String likeName, UUID universityId) throws ServiceException {
        return facultyRepo.countByLikeNameAndUniversityId(likeName, universityId);
    }
}
