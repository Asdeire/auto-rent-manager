package com.asdeire.persistence.dao;

import com.asdeire.persistence.entities.Rental;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RentalJdbcDao {
    private Connection connection;

    // Конструктор
    public RentalJdbcDao(Connection connection) {
        this.connection = connection;
    }

    // Метод для створення оренди
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

    // Метод для отримання оренди за її ідентифікатором
    public Rental getRentalById(UUID rentalId) throws SQLException {
        String sql = "SELECT * FROM rentals WHERE rental_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, rentalId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UUID userId = (UUID) resultSet.getObject("user_id");
                    UUID carId = (UUID) resultSet.getObject("car_id");
                    LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
                    LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
                    BigDecimal price = resultSet.getBigDecimal("price");
                    return new Rental(rentalId, userId, carId, startDate, endDate, price);
                } else {
                    return null; // Якщо оренду не знайдено
                }
            }
        }
    }

    // Метод для оновлення інформації про оренду
    public void updateRental(Rental rental) throws SQLException {
        String sql = "UPDATE rentals SET user_id = ?, car_id = ?, start_date = ?, end_date = ?, price = ? WHERE rental_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, rental.getUserId());
            statement.setObject(2, rental.getCarId());
            statement.setDate(3, Date.valueOf(rental.getStartDate()));
            statement.setDate(4, Date.valueOf(rental.getEndDate()));
            statement.setBigDecimal(5, rental.getPrice());
            statement.setObject(6, rental.getRentalId());
            statement.executeUpdate();
        }
    }

    // Метод для видалення оренди за її ідентифікатором
    public void deleteRental(UUID rentalId) throws SQLException {
        String sql = "DELETE FROM rentals WHERE rental_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, rentalId);
            statement.executeUpdate();
        }
    }

    // Метод для отримання всіх оренд
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
                BigDecimal price = resultSet.getBigDecimal("price");
                Rental rental = new Rental(rentalId, userId, carId, startDate, endDate, price);
                rentals.add(rental);
            }
        }
        return rentals;
    }
}
