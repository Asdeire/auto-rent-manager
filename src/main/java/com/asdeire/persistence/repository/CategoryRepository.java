package com.asdeire.persistence.repository;

import com.asdeire.persistence.entities.Category;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository class for accessing and manipulating category data in the database.
 */
@Repository
public class CategoryRepository {
    private DataSource dataSource;

    /**
     * Constructs a new CategoryRepository with the specified data source.
     *
     * @param dataSource The data source used to establish connections to the database.
     */
    public CategoryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Retrieves a category from the database by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return The category object if found, otherwise null.
     */
    public Category findById(UUID id) {
        Category category = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Categories WHERE id = ?")) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    category = new Category(
                            (UUID) resultSet.getObject("category_id"),
                            resultSet.getString("name"),
                            resultSet.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    /**
     * Retrieves all categories from the database.
     *
     * @return A list of all categories in the database.
     */
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Categories")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Category category = new Category(
                            (UUID) resultSet.getObject("category_id"),
                            resultSet.getString("name"),
                            resultSet.getString("description")
                    );
                    categories.add(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
