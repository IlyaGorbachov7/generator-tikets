package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.mdldbtbl.UniversityModelTbl;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UniversityMapperView {

    UniversityModelTbl universityDtoToModelTbl(UniversityDTO universityDTO);

    List<UniversityModelTbl> listUniversityDtoToModelTbl(List<UniversityDTO> list);
}
