package com.asdeire.persistence.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Initializes the database by executing SQL scripts.
 */
@Component
public final class DatabaseInitializer {

    private final ConnectionManager connectionManager;
    private final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializer.class);

    /**
     * Constructs a DatabaseInitializer with the specified ConnectionManager.
     *
     * @param connectionManager The ConnectionManager used to manage database connections.
     */
    public DatabaseInitializer(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Initializes the database by executing SQL scripts.
     *
     * @throws RuntimeException if unable to initialize the database.
     */
    public void init() {
        try (Connection connection = connectionManager.get()) {
            executeSQLScript(connection, "db/ddl.sql");
            executeSQLScript(connection, "db/dml.sql");
        } catch (SQLException e) {
            LOGGER.error("Failed to initialize database: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void executeSQLScript(Connection connection, String resourceName) {
        try (Statement statement = connection.createStatement()) {
            String sqlScript = readSQLScript(resourceName);
            statement.execute(sqlScript);
            LOGGER.info("SQL script {} executed successfully", resourceName);
        } catch (SQLException e) {
            LOGGER.error("Failed to execute SQL script {}: {}", resourceName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String readSQLScript(String resourceName) {
        return new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(
                                DatabaseInitializer.class
                                        .getClassLoader()
                                        .getResourceAsStream(resourceName))))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
