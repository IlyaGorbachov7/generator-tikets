package subtest1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Test1 {

    @Test
    public void test1() {
        Assertions.assertTrue(Panel.class.hashCode() != JPanel.class.hashCode());
        System.out.println(Panel.class.hashCode() + " " + JPanel.class.hashCode());
        System.out.println(Panel.class.equals(JPanel.class));
    }

    /**
     * Хотел проверить, что будет если 1) если ислючкние выбрашено в try{}
     * Как и ожидалось finaly выполяется в независимости от рельузьтата в try.
     * <p>
     * А что если в catch будет тоже выбрашено ислелючне? Будет ли final завершен ?
     * Я провеил  finaly будет ТАК же вызываться. То есть в любом случаи finaly отработает
     */
    @Test
    public void test2() {
        try {
            throwm();
        } catch (IOException e) {
            System.out.println("catch");
            throw new RuntimeException(e);
        } finally {
            System.out.println("finaly");
            throw new RuntimeException("from finaly");
//            return;
        }
    }

    void throwm() throws IOException {
        throw new IOException("from method");
    }
}
