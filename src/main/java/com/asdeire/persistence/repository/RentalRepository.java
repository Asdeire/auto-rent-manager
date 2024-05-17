package com.asdeire.persistence.repository;

import com.asdeire.persistence.entities.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Repository class for accessing and manipulating rental data in the database.
 */
@Repository
public class RentalRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new RentalRepository with the specified JdbcTemplate.
     *
     * @param jdbcTemplate The JdbcTemplate used to execute SQL queries.
     */
    @Autowired
    public RentalRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves all rentals associated with a given user ID.
     *
     * @param userId The ID of the user whose rentals are to be retrieved.
     * @return A list of rentals associated with the specified user ID.
     */
    public List<Rental> findAllByUserId(UUID userId) {
        String sql = "SELECT * FROM rentals WHERE user_id = ?";
        return jdbcTemplate.query(sql, new RentalRowMapper(), userId);
    }

    /**
     * Retrieves all rentals associated with a given car ID.
     *
     * @param carId The ID of the car whose rentals are to be retrieved.
     * @return A list of rentals associated with the specified car ID.
     */
    public List<Rental> findAllByCarId(UUID carId) {
        String sql = "SELECT * FROM rentals WHERE car_id = ?";
        return jdbcTemplate.query(sql, new RentalRowMapper(), carId);
    }

    /**
     * Retrieves a rental from the database by its ID.
     *
     * @param rentalId The ID of the rental to retrieve.
     * @return The rental object if found, otherwise null.
     */
    public Rental findById(UUID rentalId) {
        String sql = "SELECT * FROM rentals WHERE rental_id = ?";
        return jdbcTemplate.queryForObject(sql, new RentalRowMapper(), rentalId);
    }

    /**
     * Creates a new rental record in the database.
     *
     * @param rental The rental object to be created.
     */
    public void create(Rental rental) {
        String sql = "INSERT INTO rentals (rental_id, user_id, car_id, start_date, end_date, price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, rental.getRentalId(), rental.getUserId(), rental.getCarId(),
                rental.getStartDate(), rental.getEndDate(), rental.getPrice());
    }

    /**
     * Updates an existing rental record in the database.
     *
     * @param rental The rental object containing updated information.
     */
    public void update(Rental rental) {
        String sql = "UPDATE rentals SET user_id = ?, car_id = ?, start_date = ?, end_date = ?, price = ? " +
                "WHERE rental_id = ?";
        jdbcTemplate.update(sql, rental.getUserId(), rental.getCarId(), rental.getStartDate(),
                rental.getEndDate(), rental.getPrice(), rental.getRentalId());
    }

    /**
     * Deletes a rental record from the database by its ID.
     *
     * @param rentalId The ID of the rental to be deleted.
     */
    public void deleteById(UUID rentalId) {
        String sql = "DELETE FROM rentals WHERE rental_id = ?";
        jdbcTemplate.update(sql, rentalId);
    }

    /**
     * RowMapper implementation for mapping database rows to Rental objects.
     */
    private static class RentalRowMapper implements RowMapper<Rental> {
        @Override
        public Rental mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Rental(
                    UUID.fromString(rs.getString("rental_id")),
                    UUID.fromString(rs.getString("user_id")),
                    UUID.fromString(rs.getString("car_id")),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate(),
                    rs.getDouble("price")
            );
        }
    }
}
