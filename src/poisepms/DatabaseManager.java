package poisepms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Stores the database connection details and gives the program database connections. */
public class DatabaseManager {

  // <=== DATABASE CONNECTION DETAILS ===>

  // Environment variables make the app easier to run on different machines.
  private static final String DB_URL =
      getEnvironmentValue("POISEPMS_DB_URL", "jdbc:mariadb://localhost:3306/PoisePMS");

  // Default local username can be changed with POISEPMS_DB_USER.
  private static final String DB_USER = getEnvironmentValue("POISEPMS_DB_USER", "root");

  // Default local password can be changed with POISEPMS_DB_PASSWORD.
  private static final String DB_PASSWORD =
      getEnvironmentValue("POISEPMS_DB_PASSWORD", "password123");

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

  // <=== ENVIRONMENT VARIABLE HELPER ===>

  private static String getEnvironmentValue(String variableName, String defaultValue) {
    // Read the environment variable from the user's machine.
    String environmentValue = System.getenv(variableName);

    // Use the environment variable if it exists.
    if (environmentValue != null && !environmentValue.isBlank()) {
      return environmentValue;
    }

    // Use the local default if no environment variable was provided.
    return defaultValue;
  }
}