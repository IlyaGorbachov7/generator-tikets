package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.UniversityDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.UniversityService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.other.PaginationParam;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.univ.UniversityNoFoundByIdException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.UniversityMapper;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl.MapperFactoryImpl;

import java.util.List;
import java.util.Optional;

/**
 * Что нужно будет еще сделать.
 * <p>
 * 1) Сделать пагинацию запроса
 * 3) Сделать правильно поиск
 */
public class UniversityServiceImpl implements UniversityService {

    private final UniversityDAO universityRepository = RepositoryFactoryImpl.getInstance().repositoryUniversity();

    private final UniversityMapper universityMapper = MapperFactoryImpl.getInstance().universityMapper();

    @Override
    public UniversityDTO create(UniversityCreateDto universityCreateDto) throws ServiceException {
        University entity = universityMapper.universityDtoToUniversity(universityCreateDto);
        universityRepository.create(entity);
        return universityMapper.universityToUniversityDto(entity);
    }

    @Override
    public UniversityDTO update(UniversityDTO universityDto) throws ServiceException {
        University entity = universityRepository.getExecutor()
                .wrapTransactionalEntitySingle(() -> {
                    University en = universityRepository.findById(universityDto.getId())
                            .orElseThrow(UniversityNoFoundByIdException::new);
                    universityMapper.update(en, universityDto);
                    universityRepository.update(en);
                    return en;
                });
        return universityMapper.universityToUniversityDto(entity);
    }

    @Override
    public void delete(UniversityDTO universityDTO) throws ServiceException {
        universityRepository.getExecutor()
                .wrapTransactional(() -> {
                    University university = universityRepository.findById(universityDTO.getId())
                            .orElseThrow(UniversityNoFoundByIdException::new);
                    universityRepository.delete(university);
                });
    }

    @Override
    public void delete(List<UniversityDTO> universityDTO) throws ServiceException {
        universityRepository.getExecutor().wrapTransactional(() -> {
            universityDTO.forEach(udto -> universityRepository.delete(universityRepository.findById(udto.getId())
                    .orElseThrow(UniversityNoFoundByIdException::new)));
        });
    }

    @Override
    public Optional<UniversityDTO> getAny() throws ServiceException {
        return universityRepository.findAny().map(universityMapper::universityToUniversityDto);
    }

    @Override
    public List<UniversityDTO> getAll() throws ServiceException {
        return universityMapper.universityToUniversityDto(universityRepository.findAll());
    }

    @Override
    public long count() throws ServiceException {
        return universityRepository.count();
    }

    @Override
    public Optional<UniversityDTO> getByName(String name) throws ServiceException {
        return universityRepository.findByName(name).map(universityMapper::universityToUniversityDto);
    }

    @Override
    public List<UniversityDTO> getLimitedQuantity(int page, int quantity) throws ServiceException {
        return null;
    }

    @Override
    public List<UniversityDTO> getByLikeName(String likeName) {
        return universityMapper.universityToUniversityDto(universityRepository.findLikeByName(likeName));
    }

    @Override
    public List<UniversityDTO> getByLikeName(String likeName, int page, int itemsOnPage) {
        return universityMapper.universityToUniversityDto(universityRepository.findLikeByName(likeName, page, itemsOnPage));
    }

    @Override
    public long countByLikeName(String likeName) {
        return universityRepository.countLikeByName(likeName);
    }

    @Override
    public PaginationParam calculatePageParam(int itemsOnPage, int currentPage, String filterText) {
        long totalItems = filterText.isBlank() ? universityRepository.count() :
                universityRepository.countLikeByName(filterText);
        int totalPage = (int) (((totalItems % itemsOnPage) == 0.0) ? (totalItems / itemsOnPage) : (totalItems / itemsOnPage) + 1);
        return PaginationParam.builder()
                .currentPage((currentPage > totalPage) ? 1 : currentPage)
                .totalPage(totalPage)
                .itemsOnPage(itemsOnPage)
                .build();
    }
}
