import ann.VariableSource;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Ticket;
import jakarta.persistence.Table;
import model.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.params.ParameterizedTest.*;

/**
 * This test class testing created custom annotation: {@link VariableSource}
 * <p>
 * For handling this annotation created classs {@link handlerann.VariableSourceProvider}
 *
 * <b>Obout annotation: </b>
 * <p>
 * This annotation permit your determine static variable inside class.
 * <p>
 * This variable  may have the following type:
 * <p>
 * 1) Any collection extends {@link java.util.Collection}
 * <p>
 * 2) generic type of collection will be or {@link Arguments} or any types.
 * <p>
 * Condition for test method: observe the type source values and arguments method.
 * For list Arguments.class should match arguments written in Arugments.of(, , ,)
 */
@Tag(value = "production")
public class TestVariableSourceMyAnnotation {


    /**
     * case 1 - {@link #test5MySelfAnnotation(int, String, LocalDate)}
     */
    private static Set<Arguments> variableArguments = Stream.of(
            // Junit will be converted: 2022-04-12 to LocalDate
            Arguments.of(23, "Iya", "2022-04-12"),
            Arguments.of(42, "jopa", "2032-02-12")
    ).collect(Collectors.toSet());

    /**
     * case 2 - {@link #test6MySelfAnnotation(Ticket)}
     */
    private static List<Ticket> tickets = List.of(
            Ticket.of("BNTU", "FITR", "POIT", "Inginer-programmer", "trax",
                    "Hu", "sdf", Ticket.SessionType.AUTUMN, "22.06", "123"),
            Ticket.of(), Ticket.of());

    /**
     * case 3 - {@link #test7MySelfAnnotation(Person)}
     */
    private static Stream<Person> variablePersonStream = Stream.of(
            new Person("Ilya Gorabache", 21),
            new Person("Holla Gocahrngta", 32),
            new Person("Nasya", 1)
    );

    @ParameterizedTest
    @CsvSource(value = {
            "husband; wos",
            "Ilya; Hasty",
            "Gorbachev; Sevastinovich"},
            delimiterString = ";", ignoreLeadingAndTrailingWhitespace = true, maxCharsPerColumn = 100, useHeadersInDisplayName = true)
    void test1(String v1, String v2) {
        System.out.println(v1 + " : " + v2);
    }


    @DisplayName(value = "test2-argumentsProvider")
    @ParameterizedTest(name = INDEX_PLACEHOLDER + DISPLAY_NAME_PLACEHOLDER + ARGUMENTS_PLACEHOLDER)
    @ArgumentsSources(value = {@ArgumentsSource(SourceData.class)})
    void test2(String s, int inter, long longer, Object obj) {
        System.out.println(s + " " + inter + " " + longer + " " + obj.getClass());
    }

    static class SourceData implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(Arguments.of("StringVaue", 12, 123L, new Object()),
                    Arguments.of("StringV2", 32, 91L, new Object()));
        }
    }


    @ParameterizedTest(name = INDEX_PLACEHOLDER + "=> argument = " + ARGUMENTS_WITH_NAMES_PLACEHOLDER)
    @ValueSource(shorts = {23, 231, 1, 0, 321})
    void test3(short shorter) {
        System.out.println(shorter);
    }

    @ParameterizedTest()
    @MethodSource(value = "test4MethodSource")
    @Tag("developed")
    void test4MethodSource(Integer inter) {
        System.out.println(inter);
    }

    public static List<Arguments> test4MethodSource() {
        return Stream.of(Arguments.of((Object) IntStream.of(323, 122, 11, 3, 675, 43).toArray())).toList();
    }


    @ParameterizedTest
    @VariableSource(value = "variableArguments")
    void test5MySelfAnnotation(int v, String vs, LocalDate date) {
        System.out.println(v + " " + vs + " " + date);
    }

    @ParameterizedTest
    @VariableSource(value = "tickets")
    void test6MySelfAnnotation(Ticket ticket) {
        System.out.println(ticket);
    }

    @ParameterizedTest
    @VariableSource(value = "variablePersonStream")
    void test7MySelfAnnotation(Person person) {
        System.out.println(person);
    }
}
