package servises;

import bntu.fitr.gorbachev.ticketsgenerator.main.services.DepartmentService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.SpecializationService;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.deptm.DepartmentDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationCreateDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.dto.specl.SpecializationDto;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.ServiceFactory;
import bntu.fitr.gorbachev.ticketsgenerator.main.services.factory.impl.ServiceFactoryImpl;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpeciationServiceTest {
    private static final DepartmentService departmentService = ServiceFactoryImpl.getInstance().departmentService();
    private final SpecializationService specializationService = ServiceFactoryImpl.getInstance().specializationService();

    @ParameterizedTest
    @ArgumentsSource(value = ArgumentProviderForCreateTest.class)
    void create(SpecializationCreateDto scd) {
        System.out.println(specializationService.create(scd));
    }


    @Test
    void delete() {
        specializationService.delete(specializationService.getAny().orElseThrow());
    }

    @Test
    void update() {
        SpecializationDto specializationDto = specializationService.getAny().orElseThrow();
        specializationDto.setName("Специальность CEхой или ты что ");
        specializationService.update(specializationDto);
    }

    @Test
    void getAll() {
        System.out.println(specializationService.getAll()
                .stream().map(Object::toString).collect(Collectors.joining("\n")));
    }

    @Test
    void getByNameAndDeoId(){
        DepartmentDto departmentDto = ServiceFactoryImpl.getInstance().departmentService().getAny().orElseThrow();
        System.out.println(departmentDto);
        List<SpecializationDto> list = specializationService.getByLikeNameAndDepartmentId("", departmentDto.getId());
        System.out.println(list);
        list= specializationService.getByDepartmentId(departmentDto.getId());
        System.out.println(list);
    }

    static class ArgumentProviderForCreateTest implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            UUID depId = departmentService.getAny().orElseThrow().getId();
            return Stream.of(
                    Arguments.of(
                            SpecializationCreateDto.builder()
                                    .name("инжинер-программист (по направлению)")
                                    .departmentId(depId).build()),
                    Arguments.of(
                            SpecializationCreateDto.builder()
                                    .name("инжинер- пидоров")
                                    .departmentId(depId).build())
            );
        }
    }
}
