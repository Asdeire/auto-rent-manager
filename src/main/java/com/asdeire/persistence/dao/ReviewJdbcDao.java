package com.asdeire.persistence.dao;

import com.asdeire.persistence.entities.Review;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * Data Access Object (DAO) for accessing Review entities using JDBC.
 */
public class ReviewJdbcDao {
    private DataSource dataSource;

    /**
     * Constructs a new ReviewJdbcDao with the specified DataSource.
     *
     * @param dataSource the DataSource to be used for database access.
     */
    public ReviewJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Converts a Date object to a LocalDate object.
     *
     * @param date the Date object to be converted.
     * @return the LocalDate equivalent of the input Date.
     */
    public LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Finds a review by its ID.
     *
     * @param id the ID of the review to find.
     * @return the review if found, otherwise null.
     */
    public Review findById(UUID id) {
        Review review = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Reviews WHERE id = ?")) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    review = new Review(
                            (UUID) resultSet.getObject("id"),
                            (UUID) resultSet.getObject("userID"),
                            (UUID) resultSet.getObject("carID"),
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

    /**
     * Creates a new review.
     *
     * @param review the review to be created.
     */
    public void create(Review review) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Reviews (userID, carID, rating, comment, date) VALUES (?, ?, ?, ?, ?)")) {
            statement.setObject(1, review.getUserID());
            statement.setObject(2, review.getCarID());
            statement.setDouble(3, review.getRating());
            statement.setString(4, review.getComment());
            statement.setDate(5, java.sql.Date.valueOf(toLocalDate(review.getDate())));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing review.
     *
     * @param review the review to be updated.
     */
    public void update(Review review) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE Reviews SET userID = ?, carID = ?, rating = ?, comment = ?, date = ? WHERE id = ?")) {
            statement.setObject(1, review.getUserID());
            statement.setObject(2, review.getCarID());
            statement.setDouble(3, review.getRating());
            statement.setString(4, review.getComment());
            statement.setDate(5, java.sql.Date.valueOf(toLocalDate(review.getDate())));
            statement.setObject(6, review.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a review by its ID.
     *
     * @param id the ID of the review to delete.
     */
    public void delete(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Reviews WHERE id = ?")) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
