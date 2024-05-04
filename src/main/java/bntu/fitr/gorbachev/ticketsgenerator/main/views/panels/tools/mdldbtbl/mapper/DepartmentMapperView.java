package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.mdldbtbl.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools.mdldbtbl.DepartmentModelTbl;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapperView {

    DepartmentModelTbl departmentDtoToModelTbl(DepartmentSimpleDto departmentDto);

    List<DepartmentModelTbl> listDepartmentDtoToModelTbl(List<DepartmentSimpleDto> list);
}
