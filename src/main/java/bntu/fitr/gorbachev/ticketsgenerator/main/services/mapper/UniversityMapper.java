package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.UniversityDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UniversityMapper {

    UniversityDTO universityToUniversityDto(University entity);
}
