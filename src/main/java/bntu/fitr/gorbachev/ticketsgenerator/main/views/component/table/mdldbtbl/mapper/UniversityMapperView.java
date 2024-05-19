package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.UniversityModelTbl;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UniversityMapperView {

    UniversityModelTbl universityDtoToModelTbl(UniversityDTO universityDTO);

    UniversityCreateDto universityModelTblToCreateDto(UniversityModelTbl universityModelTbl);

    List<UniversityModelTbl> listUniversityDtoToModelTbl(List<UniversityDTO> list);

}
