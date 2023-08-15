package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.UniversityCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.UniversityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface UniversityMapper {

    UniversityDTO universityToUniversityDto(University entity);

    University universityDtoToUniversity(UniversityCreateDto universityCreateDto);

    University universityDtoToUniversity(UniversityDTO universityDTO);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget University target, UniversityDTO source);
}
