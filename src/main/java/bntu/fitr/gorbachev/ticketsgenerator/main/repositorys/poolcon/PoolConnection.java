package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * As connection pool, used
 * <p>
 * {@see  <a href="https://www.mastertheboss.com/hibernate-jpa/hibernate-configuration/configure-a-connection-pool-with-hibernate/">Connection pool</a>}
 */
@Log4j2
public class PoolConnection {
    /**
     * This is system property, which you may specify in JVM options for provide configure file for Hibernate framework.
     */
    public static String SYS_PROP_JPA_CONFIG = "configuration.hibernate";

    public static String DEF_CONFIG_FILE_PATH = "resources/hibernate.cfg.xml";
    private static final PoolConnection instance = new PoolConnection();
    private static SessionFactory connectionFactory;
    private static final Logger logger = LogManager.getLogger(PoolConnection.class);

    private static PoolConnection getInstance() {
        return instance;
    }

    private PoolConnection() {
    }

    /**
     * Inside xml file to set property: <b>current_session_context_class</b> with value: <B>thread</B>
     * <p>
     * So method {@link SessionFactory#getCurrentSession()} will be return session object managed hibernate context.
     * Hibernate context may be two types: <b>thread</b> or <b>jpa</b>.
     * <p>
     * <b>In this case,</b> I'm used <b>thread-context.</b> The method {@code SessionFactory.getCurrentSession()} will be
     * creat Session object if it doesn't exist, else return same Session object, that was oped early <i>in within the same thread</i>
     * Simply put, if create two and more threads, then each thread to invoke method {@code getCurrentSession} getting own object Session
     * <p>
     * <b>Important:</b> Session object, that is controlled by hibernate context, will be implicitly flush and close, after
     * commit transaction. So us don't explicitly flush and close Session object. Also, Session object may only open the one
     * transaction at a time, in within commit. If try again open transaction, although transaction was already open, then throw exception.
     *
     * @throws ConnectionPoolException
     */
    public synchronized Session getSession() throws ConnectionPoolException {
        try {
            return connectionFactory.getCurrentSession();
        } catch (HibernateException ex) {
            throw new ConnectionPoolException(ex);
        }
    }

    public synchronized void destroy() throws ConnectionPoolException {
        try {
            connectionFactory.close();
        } catch (HibernateException ex) {
            throw new ConnectionPoolException(ex);
        }
    }

    public SessionFactory getSessionFactory() {
        return connectionFactory;
    }

    public static class Builder {

        public static PoolConnection build() {
            if (connectionFactory == null) {
                synchronized (PoolConnection.Builder.class) {
                    if (connectionFactory == null) {
                        connectionFactory = new Configuration()
                                .configure(System.getProperty(SYS_PROP_JPA_CONFIG, DEF_CONFIG_FILE_PATH))
                                .buildSessionFactory();
                        log.info("connection factory is build");
                    } else {
                        log.warn("connection pool already is build");
                    }
                }
            }
            return getInstance();
        }
    }
}
