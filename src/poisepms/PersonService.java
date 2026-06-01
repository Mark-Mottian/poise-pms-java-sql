package poisepms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Handles people-related database actions such as viewing, adding, and deleting people. */
public class PersonService {

  // <=== VIEW ALL PEOPLE ===>

  /** Displays all people stored in the people tables. */
  public static void viewAllPeople() {
    // Task Requirement: Read data about people associated with projects from the database.

    System.out.println();
    System.out.println("<=== ALL PEOPLE ===>");

    // Show each people table separately so the user can see available IDs.
    viewPeopleTable("customers", "customer_id");
    viewPeopleTable("architects", "architect_id");
    viewPeopleTable("contractors", "contractor_id");
    viewPeopleTable("structural_engineers", "structural_engineer_id");
    viewPeopleTable("project_managers", "project_manager_id");
  }

  // <=== VIEW PEOPLE TABLE ===>

  private static void viewPeopleTable(String tableName, String idColumn) {
    // Build the SELECT query for the selected people table.
    String query = "SELECT * FROM " + tableName + " ORDER BY " + idColumn;

    System.out.println();
    System.out.println("<=== " + tableName.toUpperCase() + " ===>");

    // Try-with-resources closes the connection, statement, and result set automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery()
    ) {
      // Track whether this table contains any rows.
      boolean hasPeople = false;

      // Loop through every person in the selected table.
      while (results.next()) {
        // Mark that at least one person was found.
        hasPeople = true;

        // Print the current person in a readable format.
        displayPerson(results, idColumn);
      }

      // Give clear feedback if the table is empty.
      if (!hasPeople) {
        System.out.println("No records found.");
      }

    } catch (SQLException error) {
      // Tell the user which table could not be read.
      System.out.println("Could not retrieve data from " + tableName + ".");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== ADD NEW PERSON ===>

  /** Adds a new person to one of the people tables. */
  public static void addNewPerson() {
    // Task Requirement: Write data about people associated with projects to the database.

    System.out.println();
    System.out.println("<=== ADD NEW PERSON ===>");
    System.out.println("1. Add customer");
    System.out.println("2. Add architect");
    System.out.println("3. Add contractor");
    System.out.println("4. Add structural engineer");
    System.out.println("5. Add project manager");
    System.out.print("Choose a person type: ");

    // Read the selected person type.
    String personChoice = InputHelper.readRawText();

    // Convert the user's menu choice into a table name.
    String tableName = getPersonTableName(personChoice);

    // Stop early if the user chose an invalid person type.
    if (tableName.isBlank()) {
      System.out.println("Invalid person type.");
      return;
    }

    // Read the new person's details.
    String firstName = InputHelper.readText("Enter first name: ");
    String surname = InputHelper.readText("Enter surname: ");
    String telephone = InputHelper.readText("Enter telephone: ");
    String email = InputHelper.readText("Enter email: ");
    String physicalAddress = InputHelper.readText("Enter physical address: ");

    // Insert the person into the selected table.
    String query =
        "INSERT INTO "
            + tableName
            + " (first_name, surname, telephone, email, physical_address) "
            + "VALUES (?, ?, ?, ?, ?)";

    // Try-with-resources closes the connection and statement automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query)
    ) {
      // Fill the placeholders with user input.
      statement.setString(1, firstName);
      statement.setString(2, surname);
      statement.setString(3, telephone);
      statement.setString(4, email);
      statement.setString(5, physicalAddress);

      // Run the INSERT statement.
      int rowsAffected = statement.executeUpdate();

      // Tell the user whether the insert worked.
      printRowsAffected(rowsAffected, "Person record added successfully.");

    } catch (SQLException error) {
      // Tell the user the insert failed.
      System.out.println("Could not add person record.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== DELETE PERSON ===>

  /** Deletes a person from the selected people table. */
  public static void deletePerson(String tableName, String idColumn, String personType) {
    // Task Requirement: Delete data about people associated with projects from the database.

    // Ask the user which person ID should be deleted.
    int personId = InputHelper.readInt("Enter " + personType + " ID to delete: ");

    // Delete the selected person from the selected table.
    String query = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";

    // Try-with-resources closes the connection and statement automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query)
    ) {
      // Fill the ID placeholder.
      statement.setInt(1, personId);

      // Run the DELETE statement.
      int rowsAffected = statement.executeUpdate();

      // Tell the user whether the delete worked.
      printRowsAffected(rowsAffected, personType + " deleted successfully.");

    } catch (SQLException error) {
      // Explain why the delete may have failed.
      System.out.println("Could not delete " + personType + ".");
      System.out.println("This may happen if the person is still linked to a project.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== GET PERSON TABLE NAME ===>

  private static String getPersonTableName(String personChoice) {
    // Convert a menu choice into the matching database table.
    switch (personChoice) {
      case "1":
        return "customers";

      case "2":
        return "architects";

      case "3":
        return "contractors";

      case "4":
        return "structural_engineers";

      case "5":
        return "project_managers";

      default:
        return "";
    }
  }

  // <=== DISPLAY PERSON ===>

  private static void displayPerson(ResultSet results, String idColumn) throws SQLException {
    // Print a divider so each person record is easy to read.
    System.out.println("----------------------------------------");

    // Print each person field with a clear label.
    System.out.println("ID: " + results.getInt(idColumn));
    System.out.println("First Name: " + results.getString("first_name"));
    System.out.println("Surname: " + results.getString("surname"));
    System.out.println("Telephone: " + results.getString("telephone"));
    System.out.println("Email: " + results.getString("email"));
    System.out.println("Physical Address: " + results.getString("physical_address"));

    // Close the visual block.
    System.out.println("----------------------------------------");
  }

  // <=== PRINT ROWS AFFECTED ===>

  private static void printRowsAffected(int rowsAffected, String successMessage) {
    // If rows changed, the database operation worked.
    if (rowsAffected > 0) {
      System.out.println(successMessage);

    } else {
      // If no rows changed, the selected ID probably does not exist.
      System.out.println("No matching record found.");
    }
  }
}