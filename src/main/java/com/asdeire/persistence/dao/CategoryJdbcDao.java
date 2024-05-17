package com.asdeire.persistence.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.asdeire.persistence.entities.Category;

/**
 * Data Access Object (DAO) for accessing Category entities using JDBC.
 */
public class CategoryJdbcDao {
    private final DataSource dataSource;

    /**
     * Constructs a new CategoryJdbcDao with the specified DataSource.
     *
     * @param dataSource the data source.
     */
    public CategoryJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Finds a category by its ID.
     *
     * @param id the ID of the category.
     * @return the category with the specified ID, or null if not found.
     */
    public Category findById(UUID id) {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Category(
                            (UUID) resultSet.getObject("category_id"),
                            resultSet.getString("name"),
                            resultSet.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new category.
     *
     * @param category the category to create.
     */
    public void create(Category category) {
        String sql = "INSERT INTO categories (category_id, name, description) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, category.getId());
            statement.setString(2, category.getName());
            statement.setString(3, category.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing category.
     *
     * @param category the category to update.
     */
    public void update(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setObject(3, category.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete.
     */
    public void delete(UUID id) {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
