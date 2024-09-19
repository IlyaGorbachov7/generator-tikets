package repository;

import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.UniversityDAO;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.exception.DAOException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.impl.UniversityDAOImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.ConnectionPoolException;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon.PoolConnection;
import bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.tablentity.University;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class TestPoolConnection {
    static PoolConnection poolConnection;

    @BeforeAll
    static void init() {
        poolConnection = PoolConnection.Builder.build();
    }

    @AfterAll
    static void destroy() throws ConnectionPoolException {
        poolConnection.destroy();
    }

    /**
     * Initialization database from Hibernate.
     */
    @Test
    void testLaunchHibernate() throws ConnectionPoolException {
        SessionFactory sessionFactory = PoolConnection.Builder.build().getSessionFactory();
        Assertions.assertTrue(sessionFactory instanceof EntityManagerFactory);

        sessionFactory.close();
    }

    /**
     * Initialization database from JPA specification.
     */
    @Test
    void testLaunchJPA() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SQLite");
        System.out.println(entityManagerFactory); // object class is from Hibernate : SessionFactoryImpl
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    void testGivenSession() throws ConnectionPoolException {
        Session session = poolConnection.getSession();
        Session sessionExplicit = poolConnection.getSession();

        Assertions.assertSame(session, sessionExplicit);

    }

    /**
     * Testing method: {@link PoolConnection#getSession()} in compatibility with {@link SessionFactory#getCurrentSession()}}.
     * <p>
     * It is true, that each thread getting yourself Session, even if session was opened already, thread
     * getting the same session. It's possible via using: <b>sessionFactory.getCurrentSession()</b>, which
     * open session if it doesn't open or return early opened session.
     *
     * @apiNote Inside file: <b>hibernate.cfg.xml</b> necessary to set the property:<i>current_session_context_class</i>
     * with value = <b>org.hibernate.context.internal.ThreadLocalSessionContext</b>
     */
    @Test
    void testGettingSessionDifferentThreads() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Stream.Builder<Future<Session>> streamBuilder = Stream.builder();
        UniversityDAOImpl universityRepository = new UniversityDAOImpl();

        List<Future<Session>> tasks = streamBuilder.add(
                        // Thread 1
                        service.submit(() -> {
                            Session session = poolConnection.getSession();
                            Session session1 = poolConnection.getSession();
                            Thread.sleep(2000);

                            Assertions.assertSame(session, session1); // assert same object
//                            universityRepository.findAny(); // request ot database is performed
                            return session;
                        })).add(
                        // Thread 2
                        service.submit(() -> {
                            Session session = poolConnection.getSession();
                            Session session1 = poolConnection.getSession();
                            Thread.sleep(2000);
                            Assertions.assertSame(session, session1);
                            return session;
                        }))
                .build().toList();

        // Receive from 2 threads Session
        Session sessionFromThread1 = tasks.get(0).get();
        Session sessionFromThread2 = tasks.get(1).get();

        // and these Sessions should be not same (different objects)
        // Because each threads getting yourself Session.
        Assertions.assertNotSame(sessionFromThread1, sessionFromThread2);
    }


    /**
     * Testing method {@link PoolConnection#getSession()} in compatibility with {@link SessionFactory#getCurrentSession()}
     *
     * @apiNote inside file: <b>hibernate.cfg.xml</b> necessary to set the property: <b>current_session_context_class</b>
     * with value = <b>org.hibernate.context.internal.ThreadLocalSessionContext</b>
     */
    @Test
    void testGettingSessionJTATransaction() throws ConnectionPoolException, DAOException {

        Session session = poolConnection.getSession(); // sessionFactory.getCurrentSession();
        Session session1 = poolConnection.getSession();// sessionFactory.getCurrentSession();


    }

    @Test
    void testSaveUniversityAfterAbort() throws DAOException, ConnectionPoolException {
        UniversityDAO universityRepository = new UniversityDAOImpl();
        University univ = new University();
        univ.setName("Белорусский экономический университет");
        Session session = poolConnection.getSession();
        try{
            Transaction trans = session.beginTransaction();
            universityRepository.create(univ);
            trans.rollback();  // intend cancel changes
        }catch (HibernateException | DAOException ex){
            ex.printStackTrace();
        } finally {
            session.close();
        }

        Assertions.assertEquals(3, universityRepository.findAll().size());
    }
}
