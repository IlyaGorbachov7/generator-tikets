package propmanag;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver.*;
import model.Person;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

public class ResolverTest {
    private static final DeserializeResolverToObject resolverToObject = new DeserializeResolverToObject();
    private static final ResolverToArrayInt resolverToArrayInt = ResolverToArrayInt.builder().build();
    public static ResolverToArrayLong resolverToArrayLong = ResolverToArrayLong.builder().build();
    private static ResolverToMap resolverToMap = ResolverToMap.builder().build();

    @Test
    void testObjectdeserializeSerialize() {
        Person person = new Person("Ilya", 22);
        String result = resolverToObject.assembleToString(person);
        System.out.println(result);

        Serializable resultPerson = resolverToObject.assemble(result);
        System.out.println(resultPerson);
    }

    @Test
    void testInt() {
        int[] result = resolverToArrayInt.assemble("1,3,5,345,3453,345,2");
        System.out.println(Arrays.toString(result));

        String stringResult = resolverToArrayInt.assembleToString(result);
        System.out.println(stringResult);
    }

    @Test
    void testLong() {
        long[] result = resolverToArrayLong.assemble("23,423,3,324,324,32,4,24,23,423,3");
        System.out.println(Arrays.toString(result));

        String stringRes = resolverToArrayLong.assembleToString(result);
        System.out.println(stringRes);
    }

    @Test
    void testToMap() {
        Map<String, String> result = resolverToMap.assemble("kay1=value1, key2=value2, key3=value3");
        result.forEach((key, value) -> System.out.println(("key: " + key + " value: " + value)));

        String stringRes = resolverToMap.assembleToString(result);
        System.out.println(stringRes);
    }
}
