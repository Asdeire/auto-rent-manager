import com.asdeire.persistence.config.DatabaseConfig;
import com.asdeire.persistence.dao.UserJdbcDao;
import com.asdeire.persistence.entities.User;
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

public class UserDaoIntegrationTest {

    private UserJdbcDao userDao;
    private DataSource dataSource;

    @BeforeEach
    void init() {

        userDao = new UserJdbcDao(dataSource);
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Users (id SERIAL PRIMARY KEY, username VARCHAR(255), email VARCHAR(255), password VARCHAR(255), balance DOUBLE PRECISION)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateAndGetUser() {
        User user = new User(UUID.randomUUID(), "john_doe", "john@example.com", "password", 100.0);
        userDao.create(user);

        // Check database data after inserting a user
        List<User> users = userDao.getAll();
        assertEquals(1, users.size());
        User retrievedUser = users.get(0);
        assertEquals("john_doe", retrievedUser.getUsername());
        assertEquals("john@example.com", retrievedUser.getEmail());
        assertEquals("password", retrievedUser.getPassword());
        assertEquals(100.0, retrievedUser.getBalance());
    }

    @Test
    void testUpdateUser() {
        User user = new User(UUID.randomUUID(),"jane_doe", "jane@example.com", "password", 200.0);
        userDao.create(user);

        // Update user data
        User updatedUser = new User(user.id(), user.username(), "new_email@example.com", "newPassword", 150.00);
        userDao.update(updatedUser);


        // Check updated data in the database
        User retrievedUser = userDao.findById(user.getId());
        assertNotNull(retrievedUser);
        assertEquals("jane_doe", retrievedUser.getUsername());
        assertEquals("new_email@example.com", retrievedUser.getEmail());
        assertEquals("newPassword", retrievedUser.getPassword());
        assertEquals(150.0, retrievedUser.getBalance());
    }

    @Test
    void testDeleteUser() {
        User user = new User(UUID.randomUUID(),"test_user", "test@example.com", "password", 300.0);
        userDao.create(user);

        // Delete the user
        userDao.delete(user.getId());

        // Check that the user was deleted from the database
        User retrievedUser = userDao.findById(user.getId());
        assertNull(retrievedUser);
    }
}
