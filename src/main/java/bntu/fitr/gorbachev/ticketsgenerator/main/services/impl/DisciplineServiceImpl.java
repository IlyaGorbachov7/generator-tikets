package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.DisciplineDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.query.HQueryMaster;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Discipline;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.DisciplineService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.DisciplineMapper;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl.MapperFactoryImpl;

import java.util.List;
import java.util.Optional;

public class DisciplineServiceImpl implements DisciplineService {

    private final DisciplineDAO disciplineRepo = RepositoryFactoryImpl.getInstance().repositoryDiscipline();

    private final DisciplineMapper disciplineMapper = MapperFactoryImpl.getInstance().disciplineMapper();

    private final HQueryMaster<Discipline> executor = disciplineRepo.getExecutor();

    @Override
    public DisciplineDto create(DisciplineCreateDto disciplineCreateDto) throws ServiceException {
        return null;
    }

    @Override
    public DisciplineDto update(DisciplineDto disciplineDto) throws ServiceException {
        return null;
    }

    @Override
    public void delete(DisciplineDto disciplineDto) throws ServiceException {

    }

    @Override
    public Optional<DisciplineDto> getAny() throws ServiceException {
        return Optional.empty();
    }

    @Override
    public List<DisciplineDto> getAll() throws ServiceException {
        return null;
    }
}
