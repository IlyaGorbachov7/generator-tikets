package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.HeadDepartmentModelTbl;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface HeadDepartmentMapperView {

    HeadDepartmentModelTbl headDepartmentDtoToModelTbl(HeadDepartmentSimpleDto dto);

    List<HeadDepartmentModelTbl> listHeadDepartmentDtoModelTbl(List<HeadDepartmentDto> list);
}
