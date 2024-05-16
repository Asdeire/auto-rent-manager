package com.asdeire.persistence.dao;

import com.asdeire.persistence.entities.Car;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CarJdbcDao {
    private DataSource dataSource;

    public CarJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Car findById(UUID id) {
        Car car = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM cars WHERE car_id = ?")) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    car = new Car(
                            (UUID) resultSet.getObject("car_id"),
                            resultSet.getString("brand"),
                            resultSet.getString("model"),
                            resultSet.getInt("year"),
                            (UUID) resultSet.getObject("category_id"),
                            resultSet.getDouble("rating"),
                            resultSet.getBoolean("availability"),
                            resultSet.getDouble("price")
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
             PreparedStatement statement = connection.prepareStatement("INSERT INTO cars (car_id, brand, model, year, category_id, rating, availability, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setObject(1, car.getId());
            statement.setString(2, car.getBrand());
            statement.setString(3, car.getModel());
            statement.setInt(4, car.getYear());
            statement.setObject(5, car.getCategoryId());
            statement.setDouble(6, car.getRating());
            statement.setBoolean(7, car.isAvailability());
            statement.setDouble(8, car.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Car car) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE cars SET brand = ?, model = ?, year = ?, category_id = ?, rating = ?, availability = ?, price = ? WHERE car_id = ?")) {
            statement.setString(1, car.getBrand());
            statement.setString(2, car.getModel());
            statement.setInt(3, car.getYear());
            statement.setObject(4, car.getCategoryId());
            statement.setDouble(5, car.getRating());
            statement.setBoolean(6, car.isAvailability());
            statement.setDouble(7, car.getPrice());
            statement.setObject(8, car.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM cars WHERE car_id = ?")) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("car_id");
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                int year = resultSet.getInt("year");
                UUID categoryId = (UUID) resultSet.getObject("category_id");
                double rating = resultSet.getDouble("rating");
                boolean availability = resultSet.getBoolean("availability");
                double price = resultSet.getDouble("price");

                cars.add(new Car(id, brand, model, year, categoryId, rating, availability, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    public List<Car> findCarsByCategory(UUID categoryId) {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM cars WHERE category_id = ?")) {
            statement.setObject(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Car car = new Car(
                            (UUID) resultSet.getObject("car_id"),
                            resultSet.getString("brand"),
                            resultSet.getString("model"),
                            resultSet.getInt("year"),
                            (UUID) resultSet.getObject("category_id"),
                            resultSet.getDouble("rating"),
                            resultSet.getBoolean("availability"),
                            resultSet.getDouble("price")
                    );
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public List<Car> findCarsByBrand(String brand) {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM cars WHERE brand = ?")) {
            statement.setString(1, brand);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Car car = new Car(
                            (UUID) resultSet.getObject("car_id"),
                            resultSet.getString("brand"),
                            resultSet.getString("model"),
                            resultSet.getInt("year"),
                            (UUID) resultSet.getObject("category_id"),
                            resultSet.getDouble("rating"),
                            resultSet.getBoolean("availability"),
                            resultSet.getDouble("price")
                    );
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

}
