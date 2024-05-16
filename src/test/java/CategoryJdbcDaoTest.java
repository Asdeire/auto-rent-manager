import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;

import com.asdeire.persistence.dao.CategoryJdbcDao;
import com.asdeire.persistence.entities.Category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CategoryJdbcDaoTest {

    private DataSource dataSource;
    private CategoryJdbcDao categoryJdbcDao;

    @BeforeEach
    void setUp() throws SQLException {
        dataSource = Mockito.mock(DataSource.class);
        categoryJdbcDao = new CategoryJdbcDao(dataSource);
    }

    @Test
    void findById_CategoryExists_ReturnsCategory() throws SQLException {
        // Arrange
        UUID categoryId = UUID.randomUUID();
        String categoryName = "Test Category";
        String categoryDescription = "Test Description";
        Category expectedCategory = new Category(categoryId, categoryName, categoryDescription);

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getObject("category_id")).thenReturn(categoryId);
        when(resultSet.getString("name")).thenReturn(categoryName);
        when(resultSet.getString("description")).thenReturn(categoryDescription);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(resultSet);

        Connection connection = Mockito.mock(Connection.class);
        when(connection.prepareStatement(any(String.class))).thenReturn(statement);

        when(dataSource.getConnection()).thenReturn(connection);

        // Act
        Category foundCategory = categoryJdbcDao.findById(categoryId);

        // Assert
        assertEquals(expectedCategory, foundCategory);
    }

    @Test
    void update_ValidCategory_UpdatesCategory() throws SQLException {
        // Arrange
        UUID categoryId = UUID.randomUUID();
        String updatedCategoryName = "Updated Category";
        String updatedCategoryDescription = "Updated Description";
        Category updatedCategory = new Category(categoryId, updatedCategoryName, updatedCategoryDescription);

        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        Connection connection = Mockito.mock(Connection.class);
        when(connection.prepareStatement(any(String.class))).thenReturn(statement);
        when(dataSource.getConnection()).thenReturn(connection);

        // Act
        categoryJdbcDao.update(updatedCategory);

        // Assert
        verify(statement).setString(1, updatedCategoryName);
        verify(statement).setString(2, updatedCategoryDescription);
        verify(statement).setObject(3, categoryId);
        verify(statement).executeUpdate();
    }

    @Test
    void delete_ExistingCategoryId_DeletesCategory() throws SQLException {
        // Arrange
        UUID categoryId = UUID.randomUUID();
        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        Connection connection = Mockito.mock(Connection.class);
        when(connection.prepareStatement(any(String.class))).thenReturn(statement);
        when(dataSource.getConnection()).thenReturn(connection);

        // Act
        categoryJdbcDao.delete(categoryId);

        // Assert
        verify(statement).setObject(1, categoryId);
        verify(statement).executeUpdate();
    }

}
