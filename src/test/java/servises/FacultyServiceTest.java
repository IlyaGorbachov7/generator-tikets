package servises;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.FacultyService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.UniversityService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.stream.Stream;

public class FacultyServiceTest {
    final FacultyService facultyService = ServiceFactoryImpl.getInstance().facultyService();
    final FacultyDAO facultyRepo = RepositoryFactoryImpl.getInstance().repositoryFaculty();
    static final UniversityService universityService = ServiceFactoryImpl.getInstance().universityService();

    @ParameterizedTest
    @ArgumentsSource(value = ProviderArgumentsForCreateTest.class)
    void create(FacultyCreateDto facultyCreateDto) {
        FacultyDto facultyDto = facultyService.create(facultyCreateDto);
        System.out.println(facultyDto.getUniversityDto());
    }

    @ParameterizedTest
    @ArgumentsSource(value = ProviderArgumentsForCreateTestBGUIR.class)
    void createBGUIR(FacultyCreateDto facultyCreateDto) {
        FacultyDto facultyDto = facultyService.create(facultyCreateDto);
        System.out.println(facultyDto.getUniversityDto());
    }

    @ParameterizedTest
    @ArgumentsSource(value = ProviderArgumentsForCreateTestBGU.class)
    void createBGU(FacultyCreateDto facultyCreateDto) {
        FacultyDto facultyDto = facultyService.create(facultyCreateDto);
        System.out.println(facultyDto.getUniversityDto());
    }


    @Test
    void update() {
        FacultyDto facultyDto = facultyService.getAny().orElseThrow();
        facultyDto.setName("RuSAAAAAOOOO");
        facultyService.update(facultyDto);
        System.out.println(facultyDto);
    }

    @Test
    void delete() {
        FacultyDto facultyDto = facultyService.getAny().orElseThrow();
        facultyService.delete(facultyDto);
    }

    @Test
    void getAll() {
        List<FacultyDto> facultyDtos = facultyService.getAll();
        System.out.println(facultyDtos);
    }

    static class ProviderArgumentsForCreateTest implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            UniversityDTO univ = universityService.getByName("Белорусский национальный технический университет").orElseThrow();
            return Stream.of(
                    Arguments.of(FacultyCreateDto.builder().name("Автотракторный факультет").universityId(univ.getId()).build()),
                    Arguments.of(FacultyCreateDto.builder().name("Факультет горного дела и инженерной экологии").universityId(univ.getId()).build()),
                    Arguments.of(FacultyCreateDto.builder().name("Факультет информационных технологий и робототехники").universityId(univ.getId()).build())
            );
        }
    }

    static class ProviderArgumentsForCreateTestBGUIR implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            UniversityDTO univ = universityService.getByName("Белорусский государственный университет информатики и радиоэлектроники").orElseThrow();
            return Stream.of(
                    Arguments.of(FacultyCreateDto.builder().name("Факультет информационных технологий и управления").universityId(univ.getId()).build()),
                    Arguments.of(FacultyCreateDto.builder().name("Факультет радиотехники и электроники").universityId(univ.getId()).build()),
                    Arguments.of(FacultyCreateDto.builder().name("Факультет компьютерных систем и сетей").universityId(univ.getId()).build()),
                    Arguments.of(FacultyCreateDto.builder().name("Факультет информационной безопасности").universityId(univ.getId()).build())
            );
        }
    }

    static class ProviderArgumentsForCreateTestBGU implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            UniversityDTO univ = universityService.getByName("Белорусский государственный университет").orElseThrow();
            return Stream.of(Arguments.of(FacultyCreateDto.builder().name("БИОЛОГИЧЕСКИЙ ФАКУЛЬТЕТ").universityId(univ.getId()).build()),
                    Arguments.of(FacultyCreateDto.builder().name("ХИМИЧЕСКИЙ ФАКУЛЬТЕТ").universityId(univ.getId()).build()),
                    Arguments.of(FacultyCreateDto.builder().name("ФАКУЛЬТЕТ МЕЖДУНАРОДНЫХ ОТНОШЕНИЙ").universityId(univ.getId()).build()),
                    Arguments.of(FacultyCreateDto.builder().name("ФАКУЛЬТЕТ ПРИКЛАДНОЙ МАТЕМАТИКИ И ИНФОРМАТИКИ").universityId(univ.getId()).build()),
                    Arguments.of(FacultyCreateDto.builder().name("ИСТОРИЧЕСКИЙ ФАКУЛЬТЕТ").universityId(univ.getId()).build()),
                    Arguments.of(FacultyCreateDto.builder().name("ИНСТИТУТ БИЗНЕСА").universityId(univ.getId()).build())
            );
        }
    }


}
