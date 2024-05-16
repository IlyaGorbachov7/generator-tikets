package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineSimpledDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.DisciplineModelTbl;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DisciplineMapperView {

    DisciplineModelTbl disciplineDtoToModelTbl(DisciplineSimpledDto dto);

    List<DisciplineModelTbl> listDisciplineDtoToModelTbl(List<DisciplineSimpledDto> list);
}