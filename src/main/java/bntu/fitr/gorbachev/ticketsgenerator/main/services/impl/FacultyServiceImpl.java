package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.FacultyService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.fclt.FacultyNoFoundByIdException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.FacultyMapper;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl.MapperFactoryImpl;

import java.util.List;
import java.util.Optional;

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
    public Optional<FacultyDto> getAny() throws ServiceException {
        return executor.wrapTransactionalEntitySingle(() -> facultyRepo.findAny().map(facultyMapper::facultyToFacultyDto));
    }

    @Override
    public List<FacultyDto> getAll() throws ServiceException {
        return executor.wrapTransactionalResultList(() -> facultyMapper.facultyToFacultyDto(facultyRepo.findAll()));
    }
}
