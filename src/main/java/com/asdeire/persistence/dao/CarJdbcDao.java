package com.asdeire.persistence.dao;

import com.asdeire.persistence.entities.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class CarJdbcDao {
    private DataSource dataSource;

    public CarJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Car findById(int id) {
        Car car = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Car WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    car = new Car(
                            resultSet.getInt("id"),
                            resultSet.getString("brand"),
                            resultSet.getString("model"),
                            resultSet.getInt("year"),
                            resultSet.getInt("categoryID"),
                            resultSet.getDouble("rating"),
                            resultSet.getBoolean("availability")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }

    public void create(Car car) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Car (brand, model, year, categoryID, rating, availability) VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setInt(4, car.getCategoryID());
            statement.setDouble(5, car.getRating());
            statement.setBoolean(6, car.isAvailability());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Car car) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE Car SET brand = ?, model = ?, year = ?, categoryID = ?, rating = ?, availability = ? WHERE id = ?")) {
            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setInt(4, car.getCategoryID());
            statement.setDouble(5, car.getRating());
            statement.setBoolean(6, car.isAvailability());
            statement.setInt(7, car.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Car WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

