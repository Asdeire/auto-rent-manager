import com.asdeire.persistence.dao.UserJdbcDao;
import com.asdeire.persistence.entities.User;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserJdbcDaoTest {

    @Test
    void testUpdate() throws SQLException {
        // Arrange
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        User userToUpdate = new User(
                UUID.randomUUID(),
                "test_user",
                "test@example.com",
                "password",
                100.0
        );

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        UserJdbcDao userDao = new UserJdbcDao(dataSource);

        // Act
        userDao.update(userToUpdate);

        // Assert
        // Verify that executeUpdate() was called with the expected SQL statement
        String expectedSql = "UPDATE Users SET username = ?, email = ?, password = ?, balance = ? WHERE user_id = ?";
        verify(statement).setString(1, userToUpdate.getUsername());
        verify(statement).setString(2, userToUpdate.getEmail());
        verify(statement).setString(3, userToUpdate.getPassword());
        verify(statement).setDouble(4, userToUpdate.getBalance());
        verify(statement).setObject(5, userToUpdate.getId());
        verify(statement).executeUpdate();
    }

    @Test
    void testDelete() throws SQLException {
        // Arrange
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        UUID userIdToDelete = UUID.randomUUID();

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        UserJdbcDao userDao = new UserJdbcDao(dataSource);

        // Act
        userDao.delete(userIdToDelete);

        // Assert
        // Verify that executeUpdate() was called with the expected SQL statement
        String expectedSql = "DELETE FROM Users WHERE user_id = ?";
        verify(statement).setObject(1, userIdToDelete);
        verify(statement).executeUpdate();
    }
}
