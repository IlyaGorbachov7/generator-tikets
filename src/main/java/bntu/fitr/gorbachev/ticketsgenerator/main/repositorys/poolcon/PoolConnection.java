package bntu.fitr.gorbachev.ticketsgenerator.main.repositorys.poolcon;

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
public class PoolConnection {
    private static final PoolConnection instance = new PoolConnection();
    private static SessionFactory connectionFactory;
    private static final Logger logger = LogManager.getLogger(PoolConnection.class);

    private static PoolConnection getInstance() {
        return instance;
    }

    private PoolConnection() {
    }

    public synchronized Session openSession() throws ConnectionPoolException {
        try {
            return connectionFactory.openSession();
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

        public static synchronized PoolConnection build() {
            if (connectionFactory == null) {
                connectionFactory = new Configuration().configure("resources/hibernate.cfg.xml").buildSessionFactory();
                logger.info("connection factory is build");
            } else {
                logger.warn("connection pool already is build");
            }
            return getInstance();
        }
    }
}
