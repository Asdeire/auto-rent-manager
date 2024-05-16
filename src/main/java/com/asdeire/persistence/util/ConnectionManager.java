package com.asdeire.persistence.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public final class ConnectionManager {
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final Integer DEFAULT_POOL_SIZE = 10;
    private final PropertyManager propertyManager;
    private BlockingQueue<Connection> pool;
    private List<Connection> sourceConnections;
    final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    public ConnectionManager(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
        initConnectionPool();
    }

    public Connection get() {
        try {
            LOGGER.info("connection received from pool[%d]".formatted(pool.size()));
            Connection connection = pool.take();
            connection.setAutoCommit(true);
            return connection;
        } catch (InterruptedException e) {
            LOGGER.error("failed to take connection from pool. %s".formatted(e));
            throw new RuntimeException(e);
        } catch (SQLException e) {
            LOGGER.error("failed to set auto commit for connection from pool. %s".formatted(e));
            throw new RuntimeException(e);
        }
    }

    public void closePool() {
        try {
            for (Connection sourceConnection : sourceConnections) {
                sourceConnection.close();
            }
            LOGGER.info("all connections successfully closed");
        } catch (SQLException e) {
            LOGGER.error("failed to close all connections from pool. %s".formatted(e));
            throw new RuntimeException(e);
        }
    }

    private Connection open() {
        try {
            return DriverManager.getConnection(
                    propertyManager.get(URL_KEY),
                    propertyManager.get(USERNAME_KEY),
                    propertyManager.get(PASSWORD_KEY));
        } catch (SQLException e) {
            LOGGER.error("failed to open connection. %s".formatted(e));
            throw new RuntimeException(e);
        }
    }

    private void initConnectionPool() {
        String poolSize = propertyManager.get(POOL_SIZE_KEY);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        LOGGER.info("connection pool size = %s".formatted(size));
        pool = new ArrayBlockingQueue<>(size);
        sourceConnections = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Connection connection = open();
            Connection proxyConnection =
                    (Connection)
                            Proxy.newProxyInstance(
                                    ConnectionManager.class.getClassLoader(),
                                    new Class[] {Connection.class},
                                    ((proxy, method, args) ->
                                            method.getName().equals("close")
                                                    ? pool.add((Connection) proxy)
                                                    : method.invoke(connection, args)));
            pool.add(proxyConnection);
            sourceConnections.add(connection);
            LOGGER.info("connection №%d opened".formatted(i + 1));
        }
    }
}
