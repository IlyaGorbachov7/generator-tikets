package bntu.fitr.gorbachev.ticketsgenerator.main.services.mapper;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Department;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.exception.fclt.FacultyNoFoundByIdException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public abstract class DepartmentMapper {
    private final FacultyDAO facultyRepo = RepositoryFactoryImpl.getInstance().repositoryFaculty();

    public Department departmentDtoToDepartment(DepartmentCreateDto departmentCreateDto) {
        return assembleEntity(
                facultyRepo.findById(departmentCreateDto.getFacultyId()).orElseThrow(FacultyNoFoundByIdException::new),
                departmentCreateDto);
    }

    public Department departmentDtoToDepartment(DepartmentDto departmentDto) {
        return assembleEntity(
                facultyRepo.findById(departmentDto.getFacultyDto().getId()).orElseThrow(FacultyNoFoundByIdException::new),
                departmentDto);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "departmentCreateDto.name")
    protected abstract Department assembleEntity(Faculty faculty, DepartmentCreateDto departmentCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "departmentDto.name")
    protected abstract Department assembleEntity(Faculty faculty, DepartmentDto departmentDto);
}
