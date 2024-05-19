package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.DepartmentModelTbl;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapperView {

    DepartmentModelTbl departmentDtoToModelTbl(DepartmentSimpleDto departmentDto);

    DepartmentCreateDto departmentMdlTblToCreateDto(DepartmentModelTbl departmentModelTbl);

    List<DepartmentModelTbl> listDepartmentDtoToModelTbl(List<DepartmentSimpleDto> list);
}
