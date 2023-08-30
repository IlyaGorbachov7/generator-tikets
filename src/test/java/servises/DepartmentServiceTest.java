package servises;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.DepartmentService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DepartmentServiceTest {
    static private final FacultyDAO facultyDAO = RepositoryFactoryImpl.getInstance().repositoryFaculty();
    private final DepartmentService departmentService = ServiceFactoryImpl.getInstance().departmentService();

    @ParameterizedTest
    @ArgumentsSource(value = ArgumentProviderForCreateTest.class)
    void create(DepartmentCreateDto depCreateDto) {
        DepartmentDto departmentDto = departmentService.create(depCreateDto);
        System.out.println(departmentDto);
    }


    @Test
    void update() {
        DepartmentDto departmentDto = departmentService.getAny().orElseThrow();
        departmentDto.setName("DLFJLJLDFKJ LSKJDFL JSDFLKDJ");
        departmentService.update(departmentDto);
    }

    @Test
    void delete() {
        DepartmentDto departmentDto = departmentService.getAny().orElseThrow();
        departmentService.delete(departmentDto);
    }

    @Test
    void getAll() {
        System.out.println(Stream.of(departmentService.getAll().toArray())
                .map(Object::toString)
                .collect(Collectors.joining("\n")));
    }

    private static class ArgumentProviderForCreateTest implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            UUID facultyId = facultyDAO.findAny().orElseThrow().getId();
            return Stream.of(
                    Arguments.of(DepartmentCreateDto.builder()
                            .name("Кафедра программного обеспечения и робототехники")
                            .facultyId(facultyId).build()),
                    Arguments.of(DepartmentCreateDto.builder()
                            .name("Кафедра пиздюков и иронии")
                            .facultyId(facultyId).build())

            );
        }
    }
}
