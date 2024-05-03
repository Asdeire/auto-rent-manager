package com.asdeire.persistence.repository;

import com.asdeire.persistence.entities.Car;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarRepository {
    private DataSource dataSource;

    public CarRepository(DataSource dataSource) {
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
                            resultSet.getInt("categoryId"),
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
}
