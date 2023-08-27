package servises;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.FacultyDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.factory.impl.RepositoryFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.Faculty;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.FacultyService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.UniversityService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.fclt.FacultyDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

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

    @Test
    void update(){
        FacultyDto facultyDto = facultyService.getAny().orElseThrow();
        facultyDto.setName("RuSAAAAAOOOO");
        facultyService.update(facultyDto);
    }

    static class ProviderArgumentsForCreateTest implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(Arguments.of(
                    FacultyCreateDto.builder()
                            .name("Факультет пидоров fdfdfdf")
                            .universityId(universityService.getAny().orElseThrow().getId()).build()));
        }
    }
}
