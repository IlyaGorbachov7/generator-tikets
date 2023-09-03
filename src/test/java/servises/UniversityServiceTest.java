package servises;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.UniversityService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.univ.UniversityDTO;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.stream.Collectors;
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
        System.out.println("old entity : " + universityDTO);

        universityDTO.setName("Belarusian Technic University");
        UniversityDTO res = serviceUniv.update(universityDTO);
        System.out.println("updated entity : " + res);
    }

    @Test
    void testDelete() {
        UniversityDTO universityDTO = serviceUniv.getAny().orElseThrow();
        serviceUniv.delete(universityDTO);
    }


    @Test
    void testGetAll() {
        List<UniversityDTO> universityList = serviceUniv.getAll();
        universityList.forEach(System.out::println);
    }

    @Test
    void testGetAny() {
        UniversityDTO universityDto = serviceUniv.getAny().orElse(null);
        System.out.println(universityDto);
    }

    @Test
    void testFindByLikeName(){
        System.out.println(serviceUniv.getByLikeName("бело").stream().map(UniversityDTO::getName).collect(Collectors.joining("\n")));
    }

    private static class ArgumentProviderTestCreate implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.arguments(UniversityCreateDto.builder().name("Белорусский государственный университет информатики и радиоэлектроники").build()),
                    Arguments.arguments(UniversityCreateDto.builder().name("Белорусский национальный технический университет").build()),
                    Arguments.arguments(UniversityCreateDto.builder().name("Белорусский государственный университет").build()),
                    Arguments.arguments(UniversityCreateDto.builder().name("Беларусский аграрно-технический университет").build())

            );
        }
    }
}
