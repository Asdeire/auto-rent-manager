import com.asdeire.persistence.config.DatabaseConfig;
import com.asdeire.persistence.dao.CategoryJdbcDao;
import com.asdeire.persistence.entities.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryDaoIntegrationTest {
    private static DataSource dataSource;
    private CategoryJdbcDao categoryDao;



    @BeforeEach
    void init() {
        categoryDao = new CategoryJdbcDao(dataSource);
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Category (id SERIAL PRIMARY KEY, name VARCHAR(255), description VARCHAR(255))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Category");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateCategory() {
        Category category = new Category(1, "Test Category", "Test Description");
        categoryDao.create(category);
        Category savedCategory = categoryDao.findById(category.getId());
        assertNotNull(savedCategory);
        assertEquals(category.getName(), savedCategory.getName());
        assertEquals(category.getDescription(), savedCategory.getDescription());
    }

    @Test
    void testUpdateCategory() {
        Category category = new Category(1, "Test Category", "Test Description");
        categoryDao.create(category);
        Category updatedCategory = new Category(category.getId(), "Updated Test Category", "Updated Test Description");
        categoryDao.update(updatedCategory);
        Category retrievedCategory = categoryDao.findById(category.getId());
        assertNotNull(retrievedCategory);
        assertEquals(updatedCategory.getName(), retrievedCategory.getName());
        assertEquals(updatedCategory.getDescription(), retrievedCategory.getDescription());
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category(1, "Test Category", "Test Description");
        categoryDao.create(category);
        categoryDao.delete(category.getId());
        Category deletedCategory = categoryDao.findById(category.getId());
        assertEquals(null, deletedCategory);
    }
}
