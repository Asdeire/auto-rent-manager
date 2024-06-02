package com.asdeire.persistence.repository;

import com.asdeire.persistence.entities.Car;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository class for accessing and manipulating car data in the database.
 */
@Repository
public class CarRepository {
    private DataSource dataSource;

    /**
     * Constructs a new CarRepository with the specified data source.
     *
     * @param dataSource The data source used to establish connections to the database.
     */
    public CarRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Retrieves a car from the database by its ID.
     *
     * @param id The ID of the car to retrieve.
     * @return The car object if found, otherwise null.
     */
    public Car findById(UUID id) {
        Car car = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Cars WHERE car_id = ?")) {
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

    /**
     * Retrieves a list of cars from the database based on the given category ID.
     *
     * @param category_id The ID of the category to filter cars by.
     * @return A list of cars that belong to the specified category.
     */
    public List<Car> findCarsByCategory(UUID category_id) {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Cars WHERE category_id = ?")) {
            statement.setObject(1, category_id);
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
