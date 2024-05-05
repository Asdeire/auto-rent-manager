import com.asdeire.persistence.config.DatabaseConfig;
import com.asdeire.persistence.dao.ReviewJdbcDao;
import com.asdeire.persistence.entities.Review;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewDaoIntegrationTest {

    Date utilDate = new Date();
    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

    private static ReviewJdbcDao reviewDao;
    private static DataSource dataSource;

    @BeforeEach
    void init() {
        dataSource = DatabaseConfig.getDataSource();
        reviewDao = new ReviewJdbcDao(dataSource);
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Review (id SERIAL PRIMARY KEY, userID INT, carID INT, rating DOUBLE PRECISION, comment VARCHAR(255), date DATE)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Review");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateAndFindById() {
        Review review = new Review(1, 1, 1, 4.5, "Great car!", sqlDate);
        reviewDao.create(review);

        Review retrievedReview = reviewDao.findById(review.getId());
        assertNotNull(retrievedReview);
        assertEquals(review.getUserID(), retrievedReview.getUserID());
        assertEquals(review.getCarID(), retrievedReview.getCarID());
        assertEquals(review.getRating(), retrievedReview.getRating());
        assertEquals(review.getComment(), retrievedReview.getComment());
        assertEquals(review.getDate(), retrievedReview.getDate());
    }

    @Test
    void testUpdate() {
        Review review = new Review(1, 1, 1, 4.5, "Great car!", sqlDate);
        reviewDao.create(review);

        Review updatedReview = new Review(review.getId(), 2, 2, 5.0, "Excellent car!", sqlDate);
        reviewDao.update(updatedReview);

        Review retrievedReview = reviewDao.findById(review.getId());
        assertNotNull(retrievedReview);
        assertEquals(updatedReview.getUserID(), retrievedReview.getUserID());
        assertEquals(updatedReview.getCarID(), retrievedReview.getCarID());
        assertEquals(updatedReview.getRating(), retrievedReview.getRating());
        assertEquals(updatedReview.getComment(), retrievedReview.getComment());
        assertEquals(updatedReview.getDate(), retrievedReview.getDate());
    }

    @Test
    void testDelete() {
        Review review = new Review(1, 1, 1, 4.5, "Great car!", sqlDate);
        reviewDao.create(review);

        reviewDao.delete(review.getId());

        Review retrievedReview = reviewDao.findById(review.getId());
        assertNull(retrievedReview);
    }
}
