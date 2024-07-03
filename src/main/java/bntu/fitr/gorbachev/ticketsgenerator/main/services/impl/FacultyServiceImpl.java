package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.FacultyService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultySimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.other.PaginationParam;
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
    public FacultySimpleDto createSmpl(FacultyCreateDto facultyCreateDto) throws ServiceException {
        Faculty faculty = facultyMapper.facultyDtoToFaculty(facultyCreateDto);
        facultyRepo.create(faculty);
        return facultyMapper.facultyToFacultySmplDto(faculty);
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
    public FacultySimpleDto update(FacultySimpleDto dto) throws ServiceException {
        return executor.wrapTransactionalEntitySingle(()->{
           Faculty entity = facultyRepo.findById(dto.getId()).orElseThrow(FacultyNoFoundByIdException::new);
           facultyMapper.update(entity, dto);
           /*
            I should necessarily perform repo.update, because current transaction still don't committed.
            However you remember, update will be done after commit of the transaction.
            However, here directly entity convert to DTO.
            So I must implicitly perform update of operation, that this reflected on the result mapping.
            */
           facultyRepo.update(entity);
           return facultyMapper.facultyToFacultySmplDto(entity);
        });
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
    public void deleteSmpl(FacultySimpleDto facultySimpleDto) throws ServiceException {
        executor.wrapTransactional(()->{
            facultyRepo.delete(facultyRepo.findById(facultySimpleDto.getId())
                    .orElseThrow(FacultyNoFoundByIdException::new));
        });
    }

    @Override
    public void deleteSmpl(List<FacultySimpleDto> list) throws ServiceException {
        executor.wrapTransactional(()->{
            list.forEach(this::deleteSmpl);
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
    public List<FacultyDto> getByUniversityId(UUID universityId, int page, int itemsOnPage) throws ServiceException {
        return executor.wrapTransactionalResultList(()->
                facultyMapper.facultyToFacultyDto(
                        facultyRepo.findByUniversityId(universityId, page , itemsOnPage)));
    }

    @Override
    public List<FacultySimpleDto> getSmplByUniversityId(UUID universityId) throws ServiceException {
        return executor.wrapTransactionalResultList(()->
                facultyMapper.facultyToFacultySimpleDto(
                        facultyRepo.findByUniversityId(universityId)
                ));
    }

    @Override
    public List<FacultySimpleDto> getSmplByUniversityId(UUID universityId, int page, int itemsOnPage) throws ServiceException {
        return executor.wrapTransactionalResultList(()->
                facultyMapper.facultyToFacultySimpleDto(
                        facultyRepo.findByUniversityId(universityId, page , itemsOnPage)));
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
    public List<FacultyDto> getByLikeNameAndUniversityId(String likeName, UUID universityId) throws ServiceException {
        return executor.wrapTransactionalResultList(() ->
                facultyMapper.facultyToFacultyDto(
                        facultyRepo.findByLikeNameAndUniversityId(likeName, universityId)));
    }

    @Override
    public List<FacultyDto> getByLikeNameAndUniversityId(String likeName, UUID universityId, int page, int itemsOnPage) throws ServiceException {
        return executor.wrapTransactionalResultList(()->
                facultyMapper.facultyToFacultyDto(
                        facultyRepo.findByLikeNameAndUniversityId(likeName, universityId, page , itemsOnPage)));
    }

    @Override
    public List<FacultySimpleDto> getSmplByLikeNameAndUniversityId(String likeName, UUID universityId, int page, int itemsOnPage) throws ServiceException {
        return executor.wrapTransactionalResultList(()->
                facultyMapper.facultyToFacultySimpleDto(
                        facultyRepo.findByLikeNameAndUniversityId(likeName, universityId, page , itemsOnPage)));
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

    @Override
    public PaginationParam calculatePageParam(int itemsOnPage, int currentPage, String filterText, UUID universityId) {
        long totalItems = filterText.isBlank() ? facultyRepo.countByUniversityId(universityId) :
                facultyRepo.countByLikeNameAndUniversityId(filterText, universityId);
        int totalPage = (int) (((totalItems % itemsOnPage) == 0.0) ? (totalItems / itemsOnPage) : (totalItems / itemsOnPage) + 1);
        return PaginationParam.builder()
                .currentPage((currentPage > totalPage) ? 1 : currentPage)
                .totalPage(totalPage)
                .itemsOnPage(itemsOnPage)
                .build();
    }
}
