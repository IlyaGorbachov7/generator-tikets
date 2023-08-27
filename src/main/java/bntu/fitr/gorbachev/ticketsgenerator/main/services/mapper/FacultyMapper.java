package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FacultyMapper {

    Faculty facultyDtoToFaculty(FacultyCreateDto facultyCreateDto);

    FacultyDto facultyToFacultyDto(Faculty faculty);
}
