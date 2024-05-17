package com.asdeire.persistence.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manages database connections using connection pooling.
 */
@Component
public final class ConnectionManager {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final int DEFAULT_POOL_SIZE = 10;

    private final PropertyManager propertyManager;
    private BlockingQueue<Connection> pool;
    private List<Connection> sourceConnections;
    private final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    /**
     * Constructs a ConnectionManager with the specified PropertyManager.
     *
     * @param propertyManager The PropertyManager used to retrieve database properties.
     */
    @Autowired
    public ConnectionManager(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
        initConnectionPool();
    }

    /**
     * Retrieves a connection from the connection pool.
     *
     * @return A database connection.
     * @throws RuntimeException if unable to retrieve a connection.
     */
    public Connection get() {
        try {
            LOGGER.info("Connection received from pool [%d]".formatted(pool.size()));
            Connection connection = pool.take();
            connection.setAutoCommit(true);
            return connection;
        } catch (InterruptedException | SQLException e) {
            LOGGER.error("Failed to take connection from pool: %s".formatted(e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the connection pool.
     *
     * @throws RuntimeException if unable to close all connections.
     */
    public void closePool() {
        for (Connection sourceConnection : sourceConnections) {
            try {
                sourceConnection.close();
            } catch (SQLException e) {
                LOGGER.error("Failed to close connection: %s".formatted(e.getMessage()));
                throw new RuntimeException(e);
            }
        }
        LOGGER.info("All connections successfully closed");
    }

    private Connection open() {
        try {
            return DriverManager.getConnection(
                    propertyManager.get(URL_KEY),
                    propertyManager.get(USERNAME_KEY),
                    propertyManager.get(PASSWORD_KEY));
        } catch (SQLException e) {
            LOGGER.error("Failed to open connection: %s".formatted(e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    private void initConnectionPool() {
        String poolSize = propertyManager.get(POOL_SIZE_KEY);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        LOGGER.info("Connection pool size: %s".formatted(size));
        pool = new ArrayBlockingQueue<>(size);
        sourceConnections = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            Connection connection = open();
            Connection proxyConnection = createProxyConnection(connection);
            pool.add(proxyConnection);
            sourceConnections.add(connection);
            LOGGER.info("Connection %d opened".formatted(i + 1));
        }
    }

    private Connection createProxyConnection(Connection connection) {
        return (Connection) Proxy.newProxyInstance(
                ConnectionManager.class.getClassLoader(),
                new Class[]{Connection.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("close")) {
                            pool.add((Connection) proxy);
                            return null;
                        } else {
                            return method.invoke(connection, args);
                        }
                    }
                });
    }
}
