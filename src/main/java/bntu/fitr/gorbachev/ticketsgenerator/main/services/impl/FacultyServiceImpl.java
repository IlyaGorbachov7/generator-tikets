package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
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

    @Override
    public FacultyDto create(FacultyCreateDto facultyCreateDto) throws ServiceException {
        Faculty faculty = facultyMapper.facultyDtoToFaculty(facultyCreateDto);
        facultyRepo.create(faculty);
        return facultyMapper.facultyToFacultyDto(faculty);
    }

    @Override
    public FacultyDto update(FacultyDto facultyDto) throws ServiceException {
        Faculty faculty = facultyRepo.findById(facultyDto.getId())
                .orElseThrow(FacultyNoFoundByIdException::new);
        facultyMapper.update(faculty, facultyDto);
        facultyRepo.update(faculty);
        return facultyMapper.facultyToFacultyDto(faculty);
    }

    @Override
    public void delete(FacultyDto facultyDto) throws ServiceException {

    }

    @Override
    public Optional<FacultyDto> getAny() throws ServiceException {
        return facultyRepo.getExecutor()
                .executeSingleEntitySupplierQuery(() -> facultyRepo.findAny().map(facultyMapper::facultyToFacultyDto));
    }

    @Override
    public List<FacultyDto> getAll() throws ServiceException {
        return null;
    }
}
