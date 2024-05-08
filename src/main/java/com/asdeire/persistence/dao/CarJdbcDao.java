package com.asdeire.persistence.dao;

import com.asdeire.persistence.entities.Car;

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
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Car WHERE id = ?")) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    car = new Car(
                            (UUID) resultSet.getObject("id"),
                            resultSet.getString("brand"),
                            resultSet.getString("model"),
                            resultSet.getInt("year"),
                            (UUID) resultSet.getObject("categoryID"),
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
            statement.setObject(4, car.getCategoryId());
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
            statement.setObject(4, car.getCategoryId());
            statement.setDouble(5, car.getRating());
            statement.setBoolean(6, car.isAvailability());
            statement.setObject(7, car.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(UUID id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Car WHERE id = ?")) {
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM Car";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("id");
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                int year = resultSet.getInt("year");
                UUID categoryId = (UUID) resultSet.getObject("categoryId");
                double rating = resultSet.getDouble("rating");
                boolean availability = resultSet.getBoolean("availability");

                cars.add(new Car(id, brand, model, year, categoryId, rating, availability));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    public List<Car> findCarsByCategory(String category) {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Cars WHERE category = ?")) {
            statement.setString(1, category);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Car car = new Car(
                            (UUID) resultSet.getObject("id"),
                            resultSet.getString("brand"),
                            resultSet.getString("model"),
                            resultSet.getInt("year"),
                            (UUID) resultSet.getObject("category_id"),
                            resultSet.getDouble("rating"),
                            resultSet.getBoolean("availability")
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
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Cars WHERE brand = ?")) {
            statement.setString(1, brand);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Car car = new Car(
                            (UUID) resultSet.getObject("id"),
                            resultSet.getString("brand"),
                            resultSet.getString("model"),
                            resultSet.getInt("year"),
                            (UUID) resultSet.getObject("category_id"),
                            resultSet.getDouble("rating"),
                            resultSet.getBoolean("availability")
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

