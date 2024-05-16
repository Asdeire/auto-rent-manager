import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.asdeire.persistence.dao.CarJdbcDao;
import com.asdeire.persistence.entities.Car;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarJdbcDaoTest {

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private CarJdbcDao carJdbcDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void update_ShouldUpdateExistingCar() throws SQLException {
        // Arrange
        UUID id = UUID.randomUUID();
        Car car = new Car(id, "Toyota", "Corolla", 2020, UUID.randomUUID(), 4.5, true, 25000.0);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        // Act
        carJdbcDao.update(car);

        // Assert
        verify(statement).executeUpdate();
    }

    @Test
    void delete_ShouldDeleteCar() throws SQLException {
        // Arrange
        UUID id = UUID.randomUUID();
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        // Act
        carJdbcDao.delete(id);

        // Assert
        verify(statement).executeUpdate();
    }

    @Test
    void getAll_ShouldReturnListOfCars() throws SQLException {
        // Arrange
        List<Car> expectedCars = new ArrayList<>();
        expectedCars.add(new Car(UUID.randomUUID(), "Toyota", "Corolla", 2020, UUID.randomUUID(), 4.5, true, 25000.0));
        expectedCars.add(new Car(UUID.randomUUID(), "Honda", "Civic", 2019, UUID.randomUUID(), 4.2, true, 22000.0));
        expectedCars.add(new Car(UUID.randomUUID(), "Ford", "Mustang", 2021, UUID.randomUUID(), 4.8, true, 35000.0));
        ResultSet resultSet = mock(ResultSet.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        Connection connection = mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, true, false);
        when(resultSet.getObject("car_id")).thenReturn(expectedCars.get(0).getId(), expectedCars.get(1).getId(), expectedCars.get(2).getId());
        when(resultSet.getString("brand")).thenReturn(expectedCars.get(0).getBrand(), expectedCars.get(1).getBrand(), expectedCars.get(2).getBrand());
        when(resultSet.getString("model")).thenReturn(expectedCars.get(0).getModel(), expectedCars.get(1).getModel(), expectedCars.get(2).getModel());
        when(resultSet.getInt("year")).thenReturn(expectedCars.get(0).getYear(), expectedCars.get(1).getYear(), expectedCars.get(2).getYear());
        when(resultSet.getObject("category_id")).thenReturn(expectedCars.get(0).getCategoryId(), expectedCars.get(1).getCategoryId(), expectedCars.get(2).getCategoryId());
        when(resultSet.getDouble("rating")).thenReturn(expectedCars.get(0).getRating(), expectedCars.get(1).getRating(), expectedCars.get(2).getRating());
        when(resultSet.getBoolean("availability")).thenReturn(expectedCars.get(0).isAvailability(), expectedCars.get(1).isAvailability(), expectedCars.get(2).isAvailability());
        when(resultSet.getDouble("price")).thenReturn(expectedCars.get(0).getPrice(), expectedCars.get(1).getPrice(), expectedCars.get(2).getPrice());

        // Act
        List<Car> foundCars = carJdbcDao.getAll();

        // Assert
        assertEquals(expectedCars.size(), foundCars.size());
        for (int i = 0; i < expectedCars.size(); i++) {
            assertEquals(expectedCars.get(i), foundCars.get(i));
        }
    }

    @Test
    void findCarsByCategory_ShouldReturnListOfCarsWithGivenCategoryId() throws SQLException {
        // Arrange
        UUID categoryId = UUID.randomUUID();
        List<Car> expectedCars = new ArrayList<>();
        expectedCars.add(new Car(UUID.randomUUID(), "Toyota", "Corolla", 2020, categoryId, 4.5, true, 25000.0));
        expectedCars.add(new Car(UUID.randomUUID(), "Honda", "Civic", 2019, categoryId, 4.2, true, 22000.0));
        ResultSet resultSet = mock(ResultSet.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        Connection connection = mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getObject("car_id")).thenReturn(expectedCars.get(0).getId(), expectedCars.get(1).getId());
        when(resultSet.getString("brand")).thenReturn(expectedCars.get(0).getBrand(), expectedCars.get(1).getBrand());
        when(resultSet.getString("model")).thenReturn(expectedCars.get(0).getModel(), expectedCars.get(1).getModel());
        when(resultSet.getInt("year")).thenReturn(expectedCars.get(0).getYear(), expectedCars.get(1).getYear());
        when(resultSet.getObject("category_id")).thenReturn(expectedCars.get(0).getCategoryId(), expectedCars.get(1).getCategoryId());
        when(resultSet.getDouble("rating")).thenReturn(expectedCars.get(0).getRating(), expectedCars.get(1).getRating());
        when(resultSet.getBoolean("availability")).thenReturn(expectedCars.get(0).isAvailability(), expectedCars.get(1).isAvailability());
        when(resultSet.getDouble("price")).thenReturn(expectedCars.get(0).getPrice(), expectedCars.get(1).getPrice());

        // Act
        List<Car> foundCars = carJdbcDao.findCarsByCategory(categoryId);

        // Assert
        assertEquals(expectedCars.size(), foundCars.size());
        for (int i = 0; i < expectedCars.size(); i++) {
            assertEquals(expectedCars.get(i), foundCars.get(i));
        }
    }


}

