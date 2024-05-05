package com.asdeire.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.asdeire.persistence.entities.Review;

public class ReviewJdbcDao {
    private DataSource dataSource;

    public ReviewJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Review findById(int id) {
        Review review = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Review WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    review = new Review(
                            resultSet.getInt("id"),
                            resultSet.getInt("userID"),
                            resultSet.getInt("carID"),
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

    public void create(Review review) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Review (userID, carID, rating, comment, date) VALUES (?, ?, ?, ?, ?)")) {
            statement.setInt(1, review.getUserID());
            statement.setInt(2, review.getCarID());
            statement.setDouble(3, review.getRating());
            statement.setString(4, review.getComment());
            statement.setDate(5, java.sql.Date.valueOf(review.getDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Review review) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE Review SET userID = ?, carID = ?, rating = ?, comment = ?, date = ? WHERE id = ?")) {
            statement.setInt(1, review.getUserID());
            statement.setInt(2, review.getCarID());
            statement.setDouble(3, review.getRating());
            statement.setString(4, review.getComment());
            statement.setDate(5, java.sql.Date.valueOf(review.getDate()));
            statement.setInt(6, review.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Review WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

