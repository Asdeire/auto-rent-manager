import com.asdeire.persistence.config.DatabaseConfig;
import com.asdeire.persistence.dao.CarJdbcDao;
import com.asdeire.persistence.entities.Car;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CarDaoIntegrationTest {

    private CarJdbcDao carDao;
    private DataSource dataSource;

    @BeforeEach
    void init() {
        carDao = new CarJdbcDao(dataSource);
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Car (id SERIAL PRIMARY KEY, brand VARCHAR(255), model VARCHAR(255), year INT, categoryid INT, rating DOUBLE PRECISION, availability BOOLEAN)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Car");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateAndGetCar() {
        Car car = new Car(null, "Toyota", "Camry", 2022, 1, 4.5, true);
        carDao.create(car);
        Car retrievedCar = carDao.findById(car.getId());
        assertNotNull(retrievedCar);
        assertEquals("Toyota", retrievedCar.getBrand());
        assertEquals("Camry", retrievedCar.getModel());
        assertEquals(2022, retrievedCar.getYear());
        assertEquals(1, retrievedCar.getCategoryId());
        assertEquals(4.5, retrievedCar.getRating());
        assertTrue(retrievedCar.isAvailability());
    }

    @Test
    void testUpdateCar() {
        Car car = new Car(UUID.randomUUID(), "Honda", "Accord", 2020, 2, 4.3, false);
        carDao.create(car);

        Car updatedCar = new Car(car.getId(), car.getBrand(), "Civic", car.getYear(), car.getCategoryId(), car.getRating(), car.isAvailability());
        carDao.update(updatedCar);

        Car retrievedCar = carDao.findById(car.getId());
        assertNotNull(retrievedCar);
        assertEquals("Honda", retrievedCar.getBrand());
        assertEquals("Civic", retrievedCar.getModel());
        assertEquals(2020, retrievedCar.getYear());
        assertEquals(2, retrievedCar.getCategoryId());
        assertEquals(4.3, retrievedCar.getRating());
        assertFalse(retrievedCar.isAvailability());
    }


    @Test
    void testDeleteCar() {
        Car car = new Car(UUID.randomUUID(), "Ford", "Focus", 2018, 3, 4.0, true);
        carDao.create(car);
        carDao.delete(car.getId());
        Car retrievedCar = carDao.findById(car.getId());
        assertNull(retrievedCar);
    }

    @Test
    void testGetAllCars() {
        Car car1 = new Car(UUID.randomUUID(), "Toyota", "Camry", 2021, 1, 4.2, true);
        Car car2 = new Car(UUID.randomUUID(), "Honda", "Accord", 2019, 2, 4.4, false);
        carDao.create(car1);
        carDao.create(car2);
        List<Car> cars = carDao.getAll();
        assertEquals(2, cars.size());
    }
}
