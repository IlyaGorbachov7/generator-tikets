package bntu.fitr.gorbachev.ticketsgenerator.main.services.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.UniversityService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.UniversityCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.ServiceException;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.UniversityMapper;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper.factory.impl.MapperFactoryImpl;

public class UniversityServiceImpl implements UniversityService {
    UniversityMapper universityMapper = MapperFactoryImpl.getInstance().universityMapper();

    @Override
    public UniversityDTO create(UniversityCreateDto universityCreateDto) throws ServiceException {

        return null;
    }

    @Override
    public void update(University universityDto) throws ServiceException {

    }

    @Override
    public void delete(UniversityDTO universityDTO) throws ServiceException {

    }
}
