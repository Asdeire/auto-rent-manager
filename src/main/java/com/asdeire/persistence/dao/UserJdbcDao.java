package com.asdeire.persistence.dao;

import com.asdeire.persistence.entities.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object (DAO) for accessing User entities using JDBC.
 */
public class UserJdbcDao {
    private DataSource dataSource;

    /**
     * Constructs a new UserJdbcDao with the specified DataSource.
     *
     * @param dataSource the DataSource to be used for database access.
     */
    public UserJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Finds a user by its ID.
     *
     * @param id the ID of the user to find.
     * @return the user if found, otherwise null.
     */
    public User findById(UUID id) {
        User user = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE user_id = ?")) {
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
     * Creates a new user.
     *
     * @param user the user to be created.
     */
    public void create(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users (user_id, username, email, password, balance) VALUES (?, ?, ?, ?, ?)")) {
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
     * Updates an existing user.
     *
     * @param user the user to be updated.
     */
    public void update(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET username = ?, email = ?, password = ?, balance = ? WHERE user_id = ?")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setDouble(4, user.getBalance());
            statement.setObject(5, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a user by its ID.
     *
     * @param id the ID of the user to delete.
     */
    public void delete(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE user_id = ?")) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all users.
     */
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("user_id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                double balance = resultSet.getDouble("balance");
                users.add(new User(id, username, email, password, balance));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
