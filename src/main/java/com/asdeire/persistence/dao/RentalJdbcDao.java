package com.asdeire.persistence.dao;

import com.asdeire.persistence.entities.Rental;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object (DAO) for accessing Rental entities using JDBC.
 */
public class RentalJdbcDao {
    private Connection connection;

    /**
     * Constructs a new RentalJdbcDao with the specified Connection.
     *
     * @param connection the database connection.
     */
    public RentalJdbcDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a new rental.
     *
     * @param userId    the ID of the user renting the car.
     * @param carId     the ID of the car being rented.
     * @param startDate the start date of the rental.
     * @param endDate   the end date of the rental.
     * @param price     the price of the rental.
     * @throws SQLException if a database access error occurs.
     */
    public void createRental(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate, BigDecimal price) throws SQLException {
        String sql = "INSERT INTO rentals (rental_id, user_id, car_id, start_date, end_date, price) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            UUID rentalId = UUID.randomUUID();
            statement.setObject(1, rentalId);
            statement.setObject(2, userId);
            statement.setObject(3, carId);
            statement.setDate(4, Date.valueOf(startDate));
            statement.setDate(5, Date.valueOf(endDate));
            statement.setBigDecimal(6, price);
            statement.executeUpdate();
        }
    }

    // Methods for finding, updating, and deleting rentals are omitted for brevity...

    /**
     * Retrieves all rentals from the database.
     *
     * @return a list of all rentals.
     * @throws SQLException if a database access error occurs.
     */
    public List<Rental> getAllRentals() throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                UUID rentalId = (UUID) resultSet.getObject("rental_id");
                UUID userId = (UUID) resultSet.getObject("user_id");
                UUID carId = (UUID) resultSet.getObject("car_id");
                LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
                LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
                Double price = resultSet.getDouble("price");
                Rental rental = new Rental(rentalId, userId, carId, startDate, endDate, price);
                rentals.add(rental);
            }
        }
        return rentals;
    }
}
