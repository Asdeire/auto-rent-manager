package com.asdeire.persistence.repository;

import com.asdeire.persistence.entities.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository class for accessing and manipulating user data in the database.
 */
@Repository
public class UserRepository {
    private DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new UserRepository with the specified DataSource and JdbcTemplate.
     *
     * @param dataSource   The DataSource used to obtain connections to the database.
     * @param jdbcTemplate The JdbcTemplate used to execute SQL queries.
     */
    public UserRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves a user from the database by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user object if found, otherwise null.
     */
    public User findById(UUID id) {
        User user = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM User WHERE id = ?")) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User(
                            (UUID) resultSet.getObject("user_id"),
                            resultSet.getString("username"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getDouble("balance")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Retrieves a user from the database by their username.
     *
     * @param username The username of the user to retrieve.
     * @return An Optional containing the user object if found, otherwise empty.
     */
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("user_id"));
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    Double balance = resultSet.getDouble("balance");
                    return Optional.of(new User(uuid, username, email, password, balance));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Adds a new user record to the database.
     *
     * @param user The user object to be added.
     */
    public void add(User user) {
        String sql = "INSERT INTO Users (user_id, username, email, password, balance) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, user.getId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setDouble(5, user.getBalance());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing user record in the database.
     *
     * @param user The user object with updated information.
     */
    public void update(User user) {
        String sql = "UPDATE Users SET username = ?, email = ?, password = ?, balance = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword(), user.getBalance(), user.getId());
    }
}
