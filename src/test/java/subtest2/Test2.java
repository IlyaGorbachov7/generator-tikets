package subtest2;

import org.junit.jupiter.api.Test;

public class Test2 {

    @Test
    void test2() {
        int y = 0;
        int x = 0;
        y = x++;
        System.out.println("y: " + y + " x: " + x);
    }

    @Test
    void test3() {
        int totalImtes = 16;
        int itmesOnPage = 15;
        int totalPage = (int) (((totalImtes % itmesOnPage) == 0.0) ? (totalImtes / itmesOnPage) : (totalImtes / itmesOnPage) + 1);
        System.out.println(totalPage);
    }

    @Test
    void test4() {
        System.getProperties().forEach((k, v) -> {
            System.out.println("k =" + k + "v = " + v);
        });
    }
}
