import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;

public class LoginAppTest {
    private LoginApp loginApp;

    @BeforeEach
    public void setUp() {
        loginApp = new LoginApp();
    }

    @Test
    public void testValidLogin() throws ClassNotFoundException {
        String userName = loginApp.authenticateUser("johndoe@example.com", "password123");
        assertNotNull(userName);
        assertEquals("John Doe", userName);
    }

    @Test
    public void testInvalidLogin() throws ClassNotFoundException {
        String userName = loginApp.authenticateUser("invalid@example.com", "wrongpassword");
        assertNull(userName);
    }

    @Test
    public void testEmptyEmail() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            loginApp.authenticateUser("", "password123");
        });
        assertEquals("Email and Password cannot be empty.", exception.getMessage());
    }

    @Test
    public void testEmptyPassword() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            loginApp.authenticateUser("johndoe@example.com", "");
        });
        assertEquals("Email and Password cannot be empty.", exception.getMessage());
    }

    @Test
    public void testDBConnection() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/softwaretesting", "saad", "knuckles007$")) {
            assertNotNull(conn, "Database connection should not be null.");
            assertTrue(conn.isValid(2), "Database connection should be valid within the specified timeout.");
            assertTrue(conn.getAutoCommit(), "Auto-commit should be enabled by default.");
        } catch (SQLException e) {
            fail("Database connection failed with error: " + e.getMessage());
        }
    }

    @Test
    public void testSQLInjection() throws ClassNotFoundException {
        String userName = loginApp.authenticateUser("johndoe@example.com", "' OR '1'='1");
        assertNull(userName);
    }

}