package testutils;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO;
import com.sun.istack.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import model.Person;
import model.PersonAnn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import testutils.models.UniversityAbstractDAOImpl;

import java.util.UUID;
import java.util.stream.Stream;

/**
 * 28/07/2023 у бабушки сижу делаю. 1 авг мне на работу
 */
public class TestReflectionHelperDAO2 {

    @CsvSource(value = {
            "name",
            "id"
    }, delimiterString = ":")
    @ParameterizedTest
    void testGetValueFromFieldByName(String fieldName) {
        University university = new University();
        university.setName("IlyaGorbachev");
        university.setId(UUID.randomUUID());
        System.out.println(ReflectionHelperDAO.<Object>getValueFromFieldFindByName(university, fieldName));
    }

    @ParameterizedTest
    @ArgumentsSource(value = UniversityArgumentsProvider.class)
    void testGEtValueFromFieldByAnnotation(University university) {
        System.out.println(ReflectionHelperDAO.<Object>getValueFromFieldFindByAnnotation(university, Column.class));

        System.out.println(ReflectionHelperDAO.<Object>getValueFromFieldFindByAnnotation(university, Id.class));

        Assertions.assertThrows(RuntimeException.class, () -> {
            ReflectionHelperDAO.getValueFromFieldFindByAnnotation(university, OneToOne.class);
        });

        ReflectionHelperDAO.getValueFromFieldFindByAnnotation(new UniversityAbstractDAOImpl(), Column.class);
        Assertions.assertThrows(RuntimeException.class, () -> {
        });
    }

    private static class UniversityArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            University university = new University();
            university.setName("Ilya Gorbachev 28.07.2023");
            university.setId(UUID.randomUUID());
            return Stream.of(Arguments.of(university));
        }
    }

    @ParameterizedTest
    @ValueSource(classes = PersonAnn.class)
    void testExtractColumnNameFromJakartaAnnColumn(Class<PersonAnn> clazz) {
        Assertions.assertEquals(
                "primary_key", ReflectionHelperDAO.extractColumnNameFromJakartaAnnColumn(clazz, "id"));

        Assertions.assertEquals(
                "name", ReflectionHelperDAO.extractColumnNameFromJakartaAnnColumn(clazz, "name")
        );

        Assertions.assertEquals(
                "age", ReflectionHelperDAO.extractColumnNameFromJakartaAnnColumn(clazz, "age")
        );

        Assertions.assertThrows(RuntimeException.class, ()->{
            ReflectionHelperDAO.extractColumnNameFromJakartaAnnColumn(clazz, "NpFoundField");
        });
    }
}
