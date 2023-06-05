import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.PoolConnection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Test1CreateDB {

    /**
     * Initialization database from Hibernate.
     */
    @Test
    void testLaunchHibernate() throws ConnectionPoolException {
        SessionFactory sessionFactory = PoolConnection.Builder.build().getSessionFactory();
        Assertions.assertTrue( sessionFactory instanceof EntityManagerFactory);

        sessionFactory.close();
    }

    /**
     * Initialization database from JPA specification.
     */
    @Test
    void testLaunchJPA() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SQLite");
        System.out.println(entityManagerFactory); // object class is from Hibernate : SessionFactoryImpl
        EntityManager entityManager  = entityManagerFactory.createEntityManager();

        entityManager.close();
        entityManagerFactory.close();
    }
}
