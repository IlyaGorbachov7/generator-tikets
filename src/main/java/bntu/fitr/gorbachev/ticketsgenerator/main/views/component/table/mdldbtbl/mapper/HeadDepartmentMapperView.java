package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.HeadDepartmentModelTbl;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface HeadDepartmentMapperView {

    HeadDepartmentModelTbl headDepartmentDtoToModelTbl(HeadDepartmentSimpleDto dto);

    HeadDepartmentCreateDto headDepartmentMdlTblToCreateDto(HeadDepartmentModelTbl dto);

    List<HeadDepartmentModelTbl> listHeadDepartmentDtoModelTbl(List<HeadDepartmentSimpleDto> list);

    HeadDepartmentSimpleDto headDepartmentMdlTblToSmpl(HeadDepartmentModelTbl dt);

    List<HeadDepartmentSimpleDto> listHeadDepartmentMdlTblToSmpl(List<HeadDepartmentModelTbl> toList);
}
