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
    private SessionFactory connectionFactory;
    private final Logger logger = LogManager.getLogger(PoolConnection.class);

    public static PoolConnection getInstance() {
        return instance;
    }

    private PoolConnection() {
    }

    public synchronized PoolConnection buildConnectionPool() {
        if (connectionFactory == null) {
            connectionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            logger.info("connection factory is build");
        } else {
            logger.warn("connection pool already is build");
        }
        return this;
    }

    public synchronized Session giveConnection() throws ConnectionPoolException {
        try {
            return connectionFactory.openSession();
        } catch (HibernateException ex) {
            throw new ConnectionPoolException(ex);
        }
    }

    public synchronized void destroyConnectionPool() throws ConnectionPoolException {
        try {
            connectionFactory.close();
        } catch (HibernateException ex) {
            throw new ConnectionPoolException(ex);
        }
    }
}
