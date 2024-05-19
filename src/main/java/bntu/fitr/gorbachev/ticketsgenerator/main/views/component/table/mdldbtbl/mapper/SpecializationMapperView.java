package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.SpecializationModelTbl;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SpecializationMapperView {

    SpecializationModelTbl specializationDtoToModelTbl(SpecializationSimpleDto dto);

    SpecializationCreateDto specializationMdlTblToCreateDto(SpecializationModelTbl dto);

    List<SpecializationModelTbl> listSpecializationDtoToModelTbl(List<SpecializationSimpleDto> list);
}
