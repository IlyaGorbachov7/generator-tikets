package bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.TeacherModelTbl;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TeacherMapperView {

    TeacherModelTbl teacherDtoToModelTbl(TeacherSimpleDto dto);

    List<TeacherModelTbl> listTeacherDtoToModelTbl(List<TeacherSimpleDto> list);
}