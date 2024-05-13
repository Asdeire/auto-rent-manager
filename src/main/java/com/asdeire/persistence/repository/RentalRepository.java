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

@Repository
public class RentalRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RentalRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Rental> findAllByUserId(UUID userId) {
        String sql = "SELECT * FROM rentals WHERE user_id = ?";
        return jdbcTemplate.query(sql, new RentalRowMapper(), userId);
    }

    public List<Rental> findAllByCarId(UUID carId) {
        String sql = "SELECT * FROM rentals WHERE car_id = ?";
        return jdbcTemplate.query(sql, new RentalRowMapper(), carId);
    }

    public Rental findById(UUID rentalId) {
        String sql = "SELECT * FROM rentals WHERE rental_id = ?";
        return jdbcTemplate.queryForObject(sql, new RentalRowMapper(), rentalId);
    }

    public void create(Rental rental) {
        String sql = "INSERT INTO rentals (rental_id, user_id, car_id, start_date, end_date, price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, rental.getRentalId(), rental.getUserId(), rental.getCarId(),
                rental.getStartDate(), rental.getEndDate(), rental.getPrice());
    }

    public void update(Rental rental) {
        String sql = "UPDATE rentals SET user_id = ?, car_id = ?, start_date = ?, end_date = ?, price = ? " +
                "WHERE rental_id = ?";
        jdbcTemplate.update(sql, rental.getUserId(), rental.getCarId(), rental.getStartDate(),
                rental.getEndDate(), rental.getPrice(), rental.getRentalId());
    }

    public void deleteById(UUID rentalId) {
        String sql = "DELETE FROM rentals WHERE rental_id = ?";
        jdbcTemplate.update(sql, rentalId);
    }

    private static class RentalRowMapper implements RowMapper<Rental> {
        @Override
        public Rental mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Rental(
                    UUID.fromString(rs.getString("rental_id")),
                    UUID.fromString(rs.getString("user_id")),
                    UUID.fromString(rs.getString("car_id")),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate(),
                    rs.getBigDecimal("price")
            );
        }
    }
}
