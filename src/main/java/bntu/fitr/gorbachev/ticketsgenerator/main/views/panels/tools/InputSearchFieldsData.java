package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.headdep.HeadDepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.tchr.TeacherDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InputSearchFieldsData {

    private UniversityDTO universityDto;

    private FacultyDto facultyDto;

    private DepartmentDto departmentDto;

    private SpecializationDto specializationDto;

    private DisciplineDto disciplineDto;

    private HeadDepartmentDto headDepartmentDto;

    private TeacherDto teacherDto;
}
