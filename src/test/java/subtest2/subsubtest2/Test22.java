package subtest2.subsubtest2;

import ann.VariableSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.*;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class Test22 {

    private static final List<Arguments> argumentsList = List.of(
            Arguments.arguments(new ArrayDeque<String>())
    );

    @Order(323)
    @ParameterizedTest
    @VariableSource(value = "argumentsList")
    void test22(Deque<String> dequeEmpty) {
        Assertions.assertTrue(dequeEmpty.isEmpty());

        Assertions.assertThrows(Exception.class, dequeEmpty::remove);

        Assertions.assertDoesNotThrow(() -> {
            dequeEmpty.remove(null); // these methods don't throw exception even if deque is empty.
            // This method has behaviour as Collection Interface
            dequeEmpty.remove("string any");

            dequeEmpty.removeFirstOccurrence("String any"); // don't throw execution,
            // because this method remove element by given object and return true/false

        });
    }

    @Order(12)
    @Test
    void test22() throws InterruptedException, ExecutionException {
        ScheduledExecutorService executor =
                Executors.newScheduledThreadPool(1);
        Runnable task = () -> System.out.println("Scheduling: " +
                                                 System.nanoTime());
        System.out.println("start");
        ScheduledFuture<?> future = executor.schedule(task, 10,
                TimeUnit.SECONDS);
        TimeUnit.MILLISECONDS.sleep(1337);
        long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
        System.out.printf("Remaining Delay: %sms", remainingDelay);
    }


    @Test
    void test3() {
        System.out.println("1");
        Assertions.assertTrue(false);
        System.out.println("2");
        Assertions.assertTrue(true);
        System.out.println(3);
        Assertions.assertTrue(false);
        System.out.println(4);
    }


    @Test
    void test4() {
        System.out.println(1);
        Assumptions.assumeTrue(false);
        System.out.println(2);
    }

    @Test
    void testAssumeThat() {
        System.out.println(1);
        Assumptions.assumingThat(true /*false*/, () -> {
            System.out.println("Hi");
            Assumptions.assumeFalse(/*true*/ false);
        });
    }


    @Test
    void testFlatMap() {
        List<List<Integer>> listlist = List.of(
                List.of(1, 2, 3, 4, 5),
                List.of(6, 7, 8, 9, 0, 10),
                List.of(12, 13, 14, 15, 16, 17)
        );

        listlist.stream().flatMap(list -> {
            System.out.println("flatMap: " + list);
            return list.stream();
        }).peek(i->{
            System.out.print(" i: "+i);
        }).forEach(i-> System.out.println(" ternar :: i : "+ i));
    }

}
