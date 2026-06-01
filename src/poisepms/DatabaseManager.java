package poisepms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Stores the database connection details and gives the program database connections. */
public class DatabaseManager {

  // <=== DATABASE CONNECTION DETAILS ===>

  // Task Requirement: Use JDBC to read and write data from the PoisePMS database.
  private static final String DB_URL = "jdbc:mariadb://localhost:3306/PoisePMS";

  // This is the MariaDB username used by the Java program.
  private static final String DB_USER = "root";

  // Change this password to match your own local MariaDB password before running.
  private static final String DB_PASSWORD = "password123";

  // <=== DATABASE CONNECTION METHOD ===>

  /** Creates a new database connection. */
  public static Connection getDatabaseConnection() throws SQLException {
    // Create and return a fresh connection whenever a database method needs one.
    return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
  }

  // <=== DATABASE CONNECTION TEST ===>

  /** Tests whether the Java program can connect to the PoisePMS database. */
  public static void testDatabaseConnection() {
    // Try-with-resources closes the connection automatically after the test.
    try (Connection connection = getDatabaseConnection()) {
      // Confirm that the database connection worked.
      System.out.println("Connected to the PoisePMS database successfully.");

    } catch (SQLException error) {
      // Tell the user that the connection failed.
      System.out.println("Could not connect to the PoisePMS database.");

      // Print the SQL error so the problem is easier to debug.
      System.out.println("Error: " + error.getMessage());

      // Stop the program because the menu depends on a working database connection.
      System.exit(0);
    }
  }
}