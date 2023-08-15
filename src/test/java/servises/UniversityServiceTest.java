package servises;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.UniversityService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.UniversityCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

public class UniversityServiceTest {
    UniversityService serviceUniv = ServiceFactoryImpl.getInstance().universityService();

    @ParameterizedTest
    @ArgumentsSource(value = ArgumentProviderTestCreate.class)
    void testCreate(UniversityCreateDto unvCreateDto) {
        UniversityDTO universityDTO = serviceUniv.create(unvCreateDto);
        System.out.println(universityDTO);
    }

    @Test
    void testUpdate() {
        UniversityDTO universityDTO = serviceUniv.getAny().orElseThrow();
        UniversityDTO un = UniversityDTO.builder().id(universityDTO.getId()).name("Belarusian Technic University").build();
        UniversityDTO res= serviceUniv.update(un);
        Assertions.assertEquals(un.getName(), res.getName());
    }

    private static class ArgumentProviderTestCreate implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(Arguments.arguments(UniversityCreateDto.builder().name("Белорусский университет право и руководства").build()));
        }
    }
}
