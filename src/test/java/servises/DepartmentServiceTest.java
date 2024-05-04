package servises;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.DepartmentService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentSimpleDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DepartmentServiceTest {
    static private final FacultyDAO facultyDAO = RepositoryFactoryImpl.getInstance().repositoryFaculty();
    private final DepartmentService departmentService = ServiceFactoryImpl.getInstance().departmentService();

    @ParameterizedTest
    @ArgumentsSource(value = ProviderArgumentsForCreateTestDNTYAutotractorFac.class)
    void createAutoTrac(DepartmentCreateDto depCreateDto) {
        DepartmentDto departmentDto = departmentService.create(depCreateDto);
        System.out.println(departmentDto);
    }

    @ParameterizedTest
    @ArgumentsSource(value = ProviderArgumentsForCreateTestDNTYGornonDelaFac.class)
    void createGornongo(DepartmentCreateDto depCreateDto) {
        DepartmentDto departmentDto = departmentService.create(depCreateDto);
        System.out.println(departmentDto);
    }

    @ParameterizedTest
    @ArgumentsSource(value = ProviderArgumentsForCreateTestDNTYfitr.class)
    void createfitr(DepartmentCreateDto depCreateDto) {
        DepartmentDto departmentDto = departmentService.create(depCreateDto);
        System.out.println(departmentDto);
    }

    @Test
    void textGetAny(){
        DepartmentSimpleDto deps = departmentService.getSmplAny().get();
        System.out.println(deps);
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


    private static class ProviderArgumentsForCreateTestDNTYAutotractorFac implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            Faculty faculty = facultyDAO.findByName("Автотракторный факультет").orElseThrow();
            return Stream.of(
                    Arguments.of(DepartmentCreateDto.builder().name("Автомобили").facultyId(faculty.getId()).build()),
                    Arguments.of(DepartmentCreateDto.builder().name("Гидропневмоавтоматика и гидропневмопривод").facultyId(faculty.getId()).build()),
                    Arguments.of(DepartmentCreateDto.builder().name("Двигатели внутреннего сгорания").facultyId(faculty.getId()).build()),
                    Arguments.of(DepartmentCreateDto.builder().name("Инженерная графика машиностроительного профиля").facultyId(faculty.getId()).build())
            );
        }
    }
    private static class ProviderArgumentsForCreateTestDNTYGornonDelaFac implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            Faculty faculty = facultyDAO.findByName("Факультет горного дела и инженерной экологии").orElseThrow();
            return Stream.of(
                    Arguments.of(DepartmentCreateDto.builder().name("Горные машины").facultyId(faculty.getId()).build()),
                    Arguments.of(DepartmentCreateDto.builder().name("Горные работы").facultyId(faculty.getId()).build()),
                    Arguments.of(DepartmentCreateDto.builder().name("Инженерная экология").facultyId(faculty.getId()).build())
            );
        }
    }

    private static class ProviderArgumentsForCreateTestDNTYfitr implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            Faculty faculty = facultyDAO.findByName("Факультет информационных технологий и робототехники").orElseThrow();
            return Stream.of(
                    Arguments.of(DepartmentCreateDto.builder().name("Программное обеспечение информационных систем и технологий").facultyId(faculty.getId()).build()),
                    Arguments.of(DepartmentCreateDto.builder().name("Робототехнические системы").facultyId(faculty.getId()).build()),
                    Arguments.of(DepartmentCreateDto.builder().name("Электропривод и автоматизация промышленных установок и технологических комплексов").facultyId(faculty.getId()).build()),
                    Arguments.of(DepartmentCreateDto.builder().name("Техническая физика").facultyId(faculty.getId()).build()),
                    Arguments.of(DepartmentCreateDto.builder().name("Высшая математика").facultyId(faculty.getId()).build())
            );
        }
    }

}
