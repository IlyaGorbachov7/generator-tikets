package testutils;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.UniversityDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.utils.ReflectionHelperDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testutils.models.UniversityAbstractDAOImpl3;
import testutils.models.UniversityAbstractDAPImpl2;

public class TestReflectionAbstractDAOEntityMethods {

    @Test
    void test1() {
        Assertions.assertSame(ReflectionHelperDAO.extractEntityClassFromDao(UniversityDAOImpl.class), University.class);
    }

    @Test
    void test2() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> ReflectionHelperDAO.extractEntityClassFromDao(UniversityAbstractDAPImpl2.class));
}

    @Test
    void test3() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> ReflectionHelperDAO.extractEntityClassFromDao(UniversityAbstractDAOImpl3.class));
    }


}
