package com.asdeire.persistence.repository.impl;

import com.asdeire.persistence.dao.CarJdbcDao;
import com.asdeire.persistence.entities.Car;
import com.asdeire.persistence.entities.Category;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CarRepository {
    private DataSource dataSource;

    public CarRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Car findById(int id) {
        Car car = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Cars WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    car = new Car(
                            (UUID) resultSet.getObject("car_id"),
                            resultSet.getString("brand"),
                            resultSet.getString("model"),
                            resultSet.getInt("year"),
                            (UUID) resultSet.getObject("categoryId"),
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
