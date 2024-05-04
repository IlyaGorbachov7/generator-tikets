package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.mdldbtbl.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.mdldbtbl.SpecializationModelTbl;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SpecializationMapperView {

    SpecializationModelTbl specializationDtoToModelTbl(SpecializationSimpleDto dto);

    List<SpecializationModelTbl> listSpecializationDtoToModelTbl(List<SpecializationSimpleDto> list);
}
