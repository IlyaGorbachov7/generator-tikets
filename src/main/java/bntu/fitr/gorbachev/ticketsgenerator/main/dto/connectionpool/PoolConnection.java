package bntu.fitr.gorbachev.ticketsgenerator.main.dto.connectionpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

public class PoolConnection {
    private static final PoolConnection instance = new PoolConnection();
    private  final Logger logger = LogManager.getLogger(PoolConnection.class);

    private BlockingQueue<Connection> freeConnectionQueue;
    private BlockingQueue<Connection> givenConnectionQueue;

    private final String driver;
    private final String url;
    private final String user;
    private final String password;
    private int poolsize;

    public static PoolConnection getInstance() {
        return instance;
    }

    private PoolConnection() {
        DBResourceBundler dbResourceManager = DBResourceBundler.getInstance();
        driver = dbResourceManager.getProperty(DBProperties.DB_DRIVER);
        url = dbResourceManager.getProperty(DBProperties.DB_URL);
        user = dbResourceManager.getProperty(DBProperties.DB_USER);
        password = dbResourceManager.getProperty(DBProperties.DB_PASSWORD);
        try {
            poolsize = Integer.parseInt(dbResourceManager.getProperty(DBProperties.DB_POOLSIZE));
        } catch (NumberFormatException | MissingResourceException ex) {
            logger.error("Unable to get pool size value", ex);
            poolsize = 1;
        }
    }

    public void initPool() throws ConnectionPoolException {
        Locale.setDefault(Locale.ENGLISH);
        try {
            Class.forName(driver);
            freeConnectionQueue = new ArrayBlockingQueue<>(poolsize);
            givenConnectionQueue = new ArrayBlockingQueue<>(poolsize);
            for (int i = 0; i < poolsize; i++) {
                Connection connection = DriverManager.getConnection(url, user, password);
                freeConnectionQueue.add(this.new WrapperConnection(connection));
            }
        } catch (SQLException e) {
            logger.error("Unable to initialize connection pool", e);
            throw new ConnectionPoolException("SQLException in ConnectionPool", e);
        } catch (ClassNotFoundException e) {
            logger.error("Can't find database driver class", e);
            throw new ConnectionPoolException("Can't find database driver class", e);
        }
        logger.info("Connection pool is success");
        System.out.println("Connection pool is success");
    }

    public void destroyConnectionPool() {
        try {
            closeConnections(freeConnectionQueue);
            closeConnections(givenConnectionQueue);
            logger.info("Connection pool successful closed");
            System.out.println("Connection pool successful closed");
        } catch (SQLException throwables) {
            logger.error("Error closing the connection.", throwables);
        }
    }

    public Connection takeConnection() throws ConnectionPoolException {
        Connection conn;
        try {
            conn = freeConnectionQueue.take();
            givenConnectionQueue.add(conn);
        } catch (InterruptedException e) {
            logger.error("Error connecting to the data source.", e);
            throw new ConnectionPoolException("Error connecting to the data source.", e);
        }
        return conn;
    }

    private void closeConnections(BlockingQueue<Connection> blockingQueue) throws SQLException {
        Connection connection;
        while ((connection = blockingQueue.poll()) != null) {
            ((WrapperConnection) connection).reallyClose();
        }
    }

    private class WrapperConnection implements Connection {
        Connection conn;

        private WrapperConnection(Connection conn) throws SQLException {
            this.conn = conn;
            this.conn.setAutoCommit(true);
        }

        public void reallyClose() throws SQLException {
            conn.close();
        }

        @Override
        public void close() throws SQLException {
            if (conn.isClosed()) {
                throw new SQLException("Attempting to close closed connection.");
            }
            if (conn.isReadOnly()) {
                conn.setReadOnly(false);
            }
            if (!givenConnectionQueue.remove(this)) {
                throw new SQLException("Error deleting connection from the given away connections pool.");
            }
            if (!freeConnectionQueue.offer(this)) {
                throw new SQLException("Error allocation connection in the pool.");
            }
        }

        @Override
        public Statement createStatement() throws SQLException {
            return conn.createStatement();
        }

        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return conn.prepareStatement(sql);
        }

        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return conn.prepareCall(sql);
        }

        @Override
        public String nativeSQL(String sql) throws SQLException {
            return conn.nativeSQL(sql);
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            conn.setAutoCommit(autoCommit);
        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return conn.getAutoCommit();
        }

        @Override
        public void commit() throws SQLException {
            conn.commit();
        }

        @Override
        public void rollback() throws SQLException {
            conn.rollback();
        }

        @Override
        public boolean isClosed() throws SQLException {
            return conn.isClosed();
        }

        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return conn.getMetaData();
        }

        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            conn.setReadOnly(readOnly);
        }

        @Override
        public boolean isReadOnly() throws SQLException {
            return conn.isReadOnly();
        }

        @Override
        public void setCatalog(String catalog) throws SQLException {
            conn.setCatalog(catalog);
        }

        @Override
        public String getCatalog() throws SQLException {
            return conn.getCatalog();
        }

        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            conn.setTransactionIsolation(level);
        }

        @Override
        public int getTransactionIsolation() throws SQLException {
            return conn.getTransactionIsolation();
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            return conn.getWarnings();
        }

        @Override
        public void clearWarnings() throws SQLException {
            conn.clearWarnings();
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return conn.createStatement(resultSetType, resultSetConcurrency);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return conn.getTypeMap();
        }

        @Override
        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            conn.setTypeMap(map);
        }

        @Override
        public void setHoldability(int holdability) throws SQLException {
            conn.setHoldability(holdability);
        }

        @Override
        public int getHoldability() throws SQLException {
            return conn.getHoldability();
        }

        @Override
        public Savepoint setSavepoint() throws SQLException {
            return conn.setSavepoint();
        }

        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return conn.setSavepoint(name);
        }

        @Override
        public void rollback(Savepoint savepoint) throws SQLException {
            conn.rollback(savepoint);
        }

        @Override
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            conn.releaseSavepoint(savepoint);
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return conn.prepareStatement(sql, autoGeneratedKeys);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return conn.prepareStatement(sql, columnIndexes);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return conn.prepareStatement(sql, columnNames);
        }

        @Override
        public Clob createClob() throws SQLException {
            return conn.createClob();
        }

        @Override
        public Blob createBlob() throws SQLException {
            return conn.createBlob();
        }

        @Override
        public NClob createNClob() throws SQLException {
            return conn.createNClob();
        }

        @Override
        public SQLXML createSQLXML() throws SQLException {
            return conn.createSQLXML();
        }

        @Override
        public boolean isValid(int timeout) throws SQLException {
            return conn.isValid(timeout);
        }

        @Override
        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            conn.setClientInfo(name, value);
        }

        @Override
        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            conn.setClientInfo(properties);
        }

        @Override
        public String getClientInfo(String name) throws SQLException {
            return conn.getClientInfo(name);
        }

        @Override
        public Properties getClientInfo() throws SQLException {
            return conn.getClientInfo();
        }

        @Override
        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return conn.createArrayOf(typeName, elements);
        }

        @Override
        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return conn.createStruct(typeName, attributes);
        }

        @Override
        public void setSchema(String schema) throws SQLException {
            conn.setSchema(schema);
        }

        @Override
        public String getSchema() throws SQLException {
            return conn.getSchema();
        }

        @Override
        public void abort(Executor executor) throws SQLException {
            conn.abort(executor);
        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            conn.setNetworkTimeout(executor, milliseconds);
        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            return conn.getNetworkTimeout();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return conn.unwrap(iface);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return conn.isWrapperFor(iface);
        }
    }
}
