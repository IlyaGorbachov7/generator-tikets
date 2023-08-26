package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface UniversityMapper {

    UniversityDTO universityToUniversityDto(University entity);

    University universityDtoToUniversity(UniversityCreateDto universityCreateDto);

    University universityDtoToUniversity(UniversityDTO universityDTO);

    List<UniversityDTO> universityToUniversityDto(List<University> entities);

    List<University> universityDtoToUniversity(List<UniversityDTO> universityDTOList);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget University target, UniversityDTO source);
}
