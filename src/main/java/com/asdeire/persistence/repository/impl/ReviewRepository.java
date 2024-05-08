package com.asdeire.persistence.repository.impl;

import com.asdeire.persistence.entities.Review;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ReviewRepository {
    private DataSource dataSource;

    public ReviewRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Review findById(UUID id) {
        Review review = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Review WHERE id = ?")) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    review = new Review(
                            (UUID) resultSet.getObject("id"),
                            (UUID) resultSet.getObject("userId"),
                            (UUID) resultSet.getObject("carId"),
                            resultSet.getDouble("rating"),
                            resultSet.getString("comment"),
                            resultSet.getDate("date")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return review;
    }
}
