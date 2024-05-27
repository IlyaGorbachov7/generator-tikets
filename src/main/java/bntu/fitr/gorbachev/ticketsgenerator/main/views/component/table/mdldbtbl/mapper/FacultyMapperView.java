package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultySimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.FacultyModelTbl;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface FacultyMapperView {

    FacultyModelTbl facultyDtoToModelTbl(FacultySimpleDto facultyDto);

    FacultyCreateDto facultyMdlTblToCreateDto(FacultyModelTbl facultyDto);

    List<FacultyModelTbl> listFacultyDtoDtoModelTbl(List<FacultySimpleDto> list);

    FacultySimpleDto facultyMdlTblToSmpl(FacultyModelTbl facultyModelTbl);

    List<FacultySimpleDto> listFacultyMdlTblToSmpl(List<FacultyModelTbl> list);
}
