package servises;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.DisciplineService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.SpecializationService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.displn.DisciplineDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DisciplineServiceTest {
    private final DisciplineService disciplineService = ServiceFactoryImpl.getInstance().disciplineService();
    private static final SpecializationService specializationService = ServiceFactoryImpl.getInstance().specializationService();

    @ParameterizedTest
    @ArgumentsSource(value = ArgumentProviderForCreateTest.class)
    void create(DisciplineCreateDto dto) {
        System.out.println(disciplineService.create(dto));
    }

    @Test
    void delete() {
        DisciplineDto dto = disciplineService.getAny().orElseThrow();
        disciplineService.delete(dto);
    }

    @Test
    void update() {
        DisciplineDto dto = disciplineService.getAny().orElseThrow();
        dto.setName("A[ ][ ][][ ][ ] [] []  Ghhh");
        disciplineService.update(dto);
    }


    @Test
    void getAll() {
        System.out.println(
                disciplineService.getAll().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n"))
        );
    }

    static private class ArgumentProviderForCreateTest implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            UUID id = specializationService.getAny().orElseThrow().getId();
            return Stream.of(
                    Arguments.of(DisciplineCreateDto.builder()
                            .name("Программирование на Java")
                            .specializationId(id).build()),
                    Arguments.of(DisciplineCreateDto.builder()
                            .name("Предмет пидоров")
                            .specializationId(id).build()));
        }
    }
}
