package com.asdeire.persistence.repository.impl;

import com.asdeire.persistence.entities.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CategoryRepository {
    private DataSource dataSource;

    public CategoryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Category findById(int id) {
        Category category = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Category WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    category = new Category(
                            (UUID) resultSet.getObject("id"),
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
}
