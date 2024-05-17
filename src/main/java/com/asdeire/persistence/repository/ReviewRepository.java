package com.asdeire.persistence.repository;

import com.asdeire.persistence.entities.Review;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Repository class for accessing and manipulating review data in the database.
 */
@Repository
public class ReviewRepository {
    private DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new ReviewRepository with the specified DataSource and JdbcTemplate.
     *
     * @param dataSource   The DataSource used to obtain connections to the database.
     * @param jdbcTemplate The JdbcTemplate used to execute SQL queries.
     */
    public ReviewRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves a review from the database by its ID.
     *
     * @param id The ID of the review to retrieve.
     * @return The review object if found, otherwise null.
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

    /**
     * Creates a new review record in the database.
     *
     * @param review The review object to be created.
     */
    public void create(Review review) {
        String sql = "INSERT INTO Reviews (review_id ,user_id, car_id, rating, comment, review_date) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, review.getId(), review.getUserID(), review.getCarID(),
                review.getRating(), review.getComment(), review.getDate());
    }
}
