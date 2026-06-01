package poisepms;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/** Handles project-related database actions for the PoisePMS program. */
public class ProjectService {

  // <=== VIEW ALL PROJECTS ===>

  /** Displays all projects with their linked people. */
  public static void viewAllProjects() {
    // Task Requirement: Read data about projects and people associated with projects.

    String query =
        "SELECT "
            + "projects.*, "
            + "customers.first_name AS customer_first_name, "
            + "customers.surname AS customer_surname, "
            + "architects.first_name AS architect_first_name, "
            + "architects.surname AS architect_surname, "
            + "contractors.first_name AS contractor_first_name, "
            + "contractors.surname AS contractor_surname, "
            + "structural_engineers.first_name AS engineer_first_name, "
            + "structural_engineers.surname AS engineer_surname, "
            + "project_managers.first_name AS manager_first_name, "
            + "project_managers.surname AS manager_surname "
            + "FROM projects "
            + "JOIN customers ON projects.customer_id = customers.customer_id "
            + "JOIN architects ON projects.architect_id = architects.architect_id "
            + "JOIN contractors ON projects.contractor_id = contractors.contractor_id "
            + "JOIN structural_engineers "
            + "ON projects.structural_engineer_id = structural_engineers.structural_engineer_id "
            + "JOIN project_managers "
            + "ON projects.project_manager_id = project_managers.project_manager_id "
            + "ORDER BY projects.project_number";

    System.out.println();
    System.out.println("<=== ALL PROJECTS ===>");

    // Print the joined project results.
    viewJoinedProjectQuery(query, "No projects found.");
  }

  // <=== CAPTURE NEW PROJECT ===>

  /** Captures a new project and stores it in the database. */
  public static void captureNewProject() {
    // Task Requirement: Capture information about new projects and add these to the database.

    System.out.println();
    System.out.println("<=== CAPTURE NEW PROJECT ===>");

    // Show existing people first so the user can choose valid IDs.
    PersonService.viewAllPeople();

    // Read the project details.
    String projectName =
        InputHelper.readTextAllowBlank("Enter project name or press Enter to auto-generate: ");
    String buildingType = InputHelper.readText("Enter building type: ");
    String physicalAddress = InputHelper.readText("Enter project physical address: ");
    String erfNumber = InputHelper.readText("Enter ERF number: ");
    BigDecimal totalFee = InputHelper.readBigDecimal("Enter total fee: ");
    BigDecimal amountPaid = InputHelper.readBigDecimal("Enter amount paid to date: ");
    LocalDate deadline = InputHelper.readDate("Enter deadline (YYYY-MM-DD): ");

    // Read the foreign-key IDs that connect this project to people.
    int customerId = InputHelper.readInt("Enter customer ID: ");
    int architectId = InputHelper.readInt("Enter architect ID: ");
    int contractorId = InputHelper.readInt("Enter contractor ID: ");
    int structuralEngineerId = InputHelper.readInt("Enter structural engineer ID: ");
    int projectManagerId = InputHelper.readInt("Enter project manager ID: ");

    // Auto-generate the project name if the user did not enter one.
    if (projectName.isBlank()) {
      projectName = generateProjectName(buildingType, customerId);
    }

    // Insert the new project into the database.
    String query =
        "INSERT INTO projects "
            + "(project_name, building_type, physical_address, erf_number, total_fee, "
            + "amount_paid, deadline, is_finalised, completion_date, customer_id, architect_id, "
            + "contractor_id, structural_engineer_id, project_manager_id) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, FALSE, NULL, ?, ?, ?, ?, ?)";

    // Try-with-resources closes the connection and statement automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query)
    ) {
      // Fill the project details into the INSERT statement.
      statement.setString(1, projectName);
      statement.setString(2, buildingType);
      statement.setString(3, physicalAddress);
      statement.setString(4, erfNumber);
      statement.setBigDecimal(5, totalFee);
      statement.setBigDecimal(6, amountPaid);
      statement.setDate(7, Date.valueOf(deadline));
      statement.setInt(8, customerId);
      statement.setInt(9, architectId);
      statement.setInt(10, contractorId);
      statement.setInt(11, structuralEngineerId);
      statement.setInt(12, projectManagerId);

      // Run the INSERT statement.
      int rowsAffected = statement.executeUpdate();

      // Tell the user whether the project was added.
      printRowsAffected(rowsAffected, "Project added successfully.");

    } catch (SQLException error) {
      // Tell the user the insert failed.
      System.out.println("Could not add project.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== UPDATE EXISTING PROJECT ===>

  /** Updates selected information about an existing project. */
  public static void updateExistingProject() {
    // Task Requirement: Update information about existing projects.

    System.out.println();
    System.out.println("<=== UPDATE EXISTING PROJECT ===>");

    // Ask which project should be updated.
    int projectNumber = InputHelper.readInt("Enter project number to update: ");

    // Show the fields this program allows the user to update.
    System.out.println();
    System.out.println("1. Update project name");
    System.out.println("2. Update building type");
    System.out.println("3. Update physical address");
    System.out.println("4. Update ERF number");
    System.out.println("5. Update total fee");
    System.out.println("6. Update amount paid");
    System.out.println("7. Update deadline");
    System.out.print("Choose what to update: ");

    // Read the update choice.
    String updateChoice = InputHelper.readRawText();

    // Send the update to the correct helper method.
    switch (updateChoice) {
      case "1":
        updateProjectTextField(projectNumber, "project_name", "Enter new project name: ");
        break;

      case "2":
        updateProjectTextField(projectNumber, "building_type", "Enter new building type: ");
        break;

      case "3":
        updateProjectTextField(projectNumber, "physical_address", "Enter new physical address: ");
        break;

      case "4":
        updateProjectTextField(projectNumber, "erf_number", "Enter new ERF number: ");
        break;

      case "5":
        updateProjectMoneyField(projectNumber, "total_fee", "Enter new total fee: ");
        break;

      case "6":
        updateProjectMoneyField(projectNumber, "amount_paid", "Enter new amount paid: ");
        break;

      case "7":
        updateProjectDateField(projectNumber, "deadline", "Enter new deadline (YYYY-MM-DD): ");
        break;

      default:
        // Stop early if the user chooses an invalid update option.
        System.out.println("Invalid update option.");
    }
  }

  // <=== FINALISE PROJECT ===>

  /** Marks a project as finalised and adds today's date as the completion date. */
  public static void finaliseProject() {
    // Task Requirement: Finalise projects by marking them finalised and adding a completion date.

    System.out.println();
    System.out.println("<=== FINALISE PROJECT ===>");

    // Ask which project should be finalised.
    int projectNumber = InputHelper.readInt("Enter project number to finalise: ");

    // Use today's date as the completion date.
    LocalDate completionDate = LocalDate.now();

    // Update the project finalised status and completion date.
    String query =
        "UPDATE projects "
            + "SET is_finalised = TRUE, completion_date = ? "
            + "WHERE project_number = ?";

    // Try-with-resources closes the connection and statement automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query)
    ) {
      // Fill the completion date and project number.
      statement.setDate(1, Date.valueOf(completionDate));
      statement.setInt(2, projectNumber);

      // Run the UPDATE statement.
      int rowsAffected = statement.executeUpdate();

      // Tell the user whether the project was finalised.
      printRowsAffected(rowsAffected, "Project finalised successfully.");

    } catch (SQLException error) {
      // Tell the user the finalise operation failed.
      System.out.println("Could not finalise project.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== VIEW INCOMPLETE PROJECTS ===>

  /** Displays projects that have not been finalised. */
  public static void viewIncompleteProjects() {
    // Task Requirement: Find all projects that still need to be completed from the database.

    // Select projects that have not been finalised.
    String query = "SELECT * FROM projects WHERE is_finalised = FALSE ORDER BY deadline";

    System.out.println();
    System.out.println("<=== INCOMPLETE PROJECTS ===>");

    // Print the matching projects.
    viewSimpleProjectQuery(query, "No incomplete projects found.");
  }

  // <=== VIEW OVERDUE PROJECTS ===>

  /** Displays projects that are past their deadline and not finalised. */
  public static void viewOverdueProjects() {
    // Task Requirement: Find all projects that are past the due date from the database.

    // Select projects that are overdue and not finalised.
    String query =
        "SELECT * FROM projects "
            + "WHERE deadline < CURDATE() AND is_finalised = FALSE "
            + "ORDER BY deadline";

    System.out.println();
    System.out.println("<=== OVERDUE PROJECTS ===>");

    // Print the matching projects.
    viewSimpleProjectQuery(query, "No overdue projects found.");
  }

  // <=== SEARCH FOR PROJECT ===>

  /** Searches for a project by project number or project name. */
  public static void searchForProject() {
    // Task Requirement: Find and select a project by entering project number or project name.

    System.out.println();
    System.out.println("<=== SEARCH FOR PROJECT ===>");
    System.out.println("1. Search by project number");
    System.out.println("2. Search by project name");
    System.out.print("Choose search type: ");

    // Read the user's search choice.
    String searchChoice = InputHelper.readRawText();

    // Search using the selected method.
    switch (searchChoice) {
      case "1":
        searchProjectByNumber();
        break;

      case "2":
        searchProjectByName();
        break;

      default:
        // Stop early if the user chooses an invalid search option.
        System.out.println("Invalid search option.");
    }
  }

  // <=== DELETE PROJECT ===>

  /** Deletes a selected project from the database. */
  public static void deleteProject() {
    // Task Requirement: Delete data about projects from the database.

    // Ask which project should be deleted.
    int projectNumber = InputHelper.readInt("Enter project number to delete: ");

    // Delete the matching project.
    String query = "DELETE FROM projects WHERE project_number = ?";

    // Try-with-resources closes the connection and statement automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query)
    ) {
      // Fill the project number placeholder.
      statement.setInt(1, projectNumber);

      // Run the DELETE statement.
      int rowsAffected = statement.executeUpdate();

      // Tell the user whether the project was deleted.
      printRowsAffected(rowsAffected, "Project deleted successfully.");

    } catch (SQLException error) {
      // Tell the user the delete failed.
      System.out.println("Could not delete project.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== GENERATE PROJECT NAME ===>

  private static String generateProjectName(String buildingType, int customerId) {
    // Task Requirement: If no project name is provided, name it using the customer's surname.

    // Get the customer's surname using the selected customer ID.
    String query = "SELECT surname FROM customers WHERE customer_id = ?";

    // Try-with-resources closes the connection and statement automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query)
    ) {
      // Fill the customer ID placeholder.
      statement.setInt(1, customerId);

      // Run the SELECT statement.
      try (ResultSet results = statement.executeQuery()) {
        // Use the customer's surname if the customer exists.
        if (results.next()) {
          return buildingType + " " + results.getString("surname");
        }
      }

    } catch (SQLException error) {
      // Explain that the automatic name could not be created.
      System.out.println("Could not auto-generate project name.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }

    // Use a fallback name if the customer's surname cannot be found.
    return buildingType + " Project";
  }

  // <=== UPDATE PROJECT TEXT FIELD ===>

  private static void updateProjectTextField(int projectNumber, String columnName, String prompt) {
    // Task Requirement: Update information about existing projects.

    // Read the new text value.
    String newValue = InputHelper.readText(prompt);

    // Update the chosen text column.
    String query = "UPDATE projects SET " + columnName + " = ? WHERE project_number = ?";

    // Run the text update.
    updateTextField(query, newValue, projectNumber);
  }

  // <=== UPDATE PROJECT MONEY FIELD ===>

  private static void updateProjectMoneyField(int projectNumber, String columnName, String prompt) {
    // Task Requirement: Update information about existing projects.

    // Read the new money value.
    BigDecimal newValue = InputHelper.readBigDecimal(prompt);

    // Update the chosen money column.
    String query = "UPDATE projects SET " + columnName + " = ? WHERE project_number = ?";

    // Try-with-resources closes the connection and statement automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query)
    ) {
      // Fill the new value and project number.
      statement.setBigDecimal(1, newValue);
      statement.setInt(2, projectNumber);

      // Run the UPDATE statement.
      int rowsAffected = statement.executeUpdate();

      // Tell the user whether the update worked.
      printRowsAffected(rowsAffected, "Project updated successfully.");

    } catch (SQLException error) {
      // Tell the user the update failed.
      System.out.println("Could not update project.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== UPDATE PROJECT DATE FIELD ===>

  private static void updateProjectDateField(int projectNumber, String columnName, String prompt) {
    // Task Requirement: Update information about existing projects.

    // Read the new date value.
    LocalDate newValue = InputHelper.readDate(prompt);

    // Update the chosen date column.
    String query = "UPDATE projects SET " + columnName + " = ? WHERE project_number = ?";

    // Try-with-resources closes the connection and statement automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query)
    ) {
      // Fill the new date and project number.
      statement.setDate(1, Date.valueOf(newValue));
      statement.setInt(2, projectNumber);

      // Run the UPDATE statement.
      int rowsAffected = statement.executeUpdate();

      // Tell the user whether the update worked.
      printRowsAffected(rowsAffected, "Project updated successfully.");

    } catch (SQLException error) {
      // Tell the user the update failed.
      System.out.println("Could not update project.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== UPDATE TEXT FIELD ===>

  private static void updateTextField(String query, String newValue, int projectNumber) {
    // Task Requirement: Update information about existing projects.

    // Try-with-resources closes the connection and statement automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query)
    ) {
      // Fill the new text value and project number.
      statement.setString(1, newValue);
      statement.setInt(2, projectNumber);

      // Run the UPDATE statement.
      int rowsAffected = statement.executeUpdate();

      // Tell the user whether the update worked.
      printRowsAffected(rowsAffected, "Project updated successfully.");

    } catch (SQLException error) {
      // Tell the user the update failed.
      System.out.println("Could not update project.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== SEARCH PROJECT BY NUMBER ===>

  private static void searchProjectByNumber() {
    // Task Requirement: Find and select a project by entering the project number.

    // Ask for the project number.
    int projectNumber = InputHelper.readInt("Enter project number: ");

    // Search for the exact project number.
    String query = "SELECT * FROM projects WHERE project_number = ?";

    // Try-with-resources closes the connection and statement automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query)
    ) {
      // Fill the project number placeholder.
      statement.setInt(1, projectNumber);

      // Run the SELECT statement.
      try (ResultSet results = statement.executeQuery()) {
        // Display the project if it exists.
        if (results.next()) {
          displayProject(results);

        } else {
          // Tell the user no matching project was found.
          System.out.println("No project found with that project number.");
        }
      }

    } catch (SQLException error) {
      // Tell the user the search failed.
      System.out.println("Could not search for project.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== SEARCH PROJECT BY NAME ===>

  private static void searchProjectByName() {
    // Task Requirement: Find and select a project by entering the project name.

    // Ask for the project name.
    String projectName = InputHelper.readText("Enter project name: ");

    // Use LIKE so the user can search with part of the project name.
    String query = "SELECT * FROM projects WHERE project_name LIKE ?";

    // Try-with-resources closes the connection and statement automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query)
    ) {
      // Add wildcards around the search text.
      statement.setString(1, "%" + projectName + "%");

      // Run the SELECT statement.
      try (ResultSet results = statement.executeQuery()) {
        // Track whether any matching project was found.
        boolean hasProjects = false;

        // Print every matching project.
        while (results.next()) {
          hasProjects = true;
          displayProject(results);
        }

        // Tell the user if no project matched the search.
        if (!hasProjects) {
          System.out.println("No project found with that name.");
        }
      }

    } catch (SQLException error) {
      // Tell the user the search failed.
      System.out.println("Could not search for project.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== VIEW SIMPLE PROJECT QUERY ===>

  private static void viewSimpleProjectQuery(String query, String emptyMessage) {
    // Task Requirement: Read selected project data from the database.

    // Try-with-resources closes the connection, statement, and result set automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery()
    ) {
      // Track whether any projects were returned.
      boolean hasProjects = false;

      // Print every project returned by the query.
      while (results.next()) {
        hasProjects = true;
        displayProject(results);
      }

      // Tell the user if the query returned no projects.
      if (!hasProjects) {
        System.out.println(emptyMessage);
      }

    } catch (SQLException error) {
      // Tell the user the query failed.
      System.out.println("Could not retrieve projects.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== VIEW JOINED PROJECT QUERY ===>

  private static void viewJoinedProjectQuery(String query, String emptyMessage) {
    // Task Requirement: Read project data and people associated with projects from the database.

    // Try-with-resources closes the connection, statement, and result set automatically.
    try (
        Connection connection = DatabaseManager.getDatabaseConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery()
    ) {
      // Track whether any projects were returned.
      boolean hasProjects = false;

      // Print every joined project record returned by the query.
      while (results.next()) {
        hasProjects = true;
        displayProjectWithPeople(results);
      }

      // Tell the user if the query returned no projects.
      if (!hasProjects) {
        System.out.println(emptyMessage);
      }

    } catch (SQLException error) {
      // Tell the user the query failed.
      System.out.println("Could not retrieve projects.");

      // Print the SQL error for debugging.
      System.out.println("Error: " + error.getMessage());
    }
  }

  // <=== DISPLAY PROJECT WITH PEOPLE ===>

  private static void displayProjectWithPeople(ResultSet results) throws SQLException {
    // Print the project details first.
    displayProject(results);

    // Print the linked people underneath the project.
    System.out.println("Customer: " + results.getString("customer_first_name") + " "
        + results.getString("customer_surname"));
    System.out.println("Architect: " + results.getString("architect_first_name") + " "
        + results.getString("architect_surname"));
    System.out.println("Contractor: " + results.getString("contractor_first_name") + " "
        + results.getString("contractor_surname"));
    System.out.println("Structural Engineer: " + results.getString("engineer_first_name") + " "
        + results.getString("engineer_surname"));
    System.out.println("Project Manager: " + results.getString("manager_first_name") + " "
        + results.getString("manager_surname"));

    // Close the visual block.
    System.out.println("----------------------------------------");
  }

  // <=== DISPLAY PROJECT ===>

  private static void displayProject(ResultSet results) throws SQLException {
    // Print a divider so each project is easy to read.
    System.out.println("----------------------------------------");

    // Print each project field with a clear label.
    System.out.println("Project Number: " + results.getInt("project_number"));
    System.out.println("Project Name: " + results.getString("project_name"));
    System.out.println("Building Type: " + results.getString("building_type"));
    System.out.println("Physical Address: " + results.getString("physical_address"));
    System.out.println("ERF Number: " + results.getString("erf_number"));
    System.out.println("Total Fee: R" + results.getBigDecimal("total_fee"));
    System.out.println("Amount Paid: R" + results.getBigDecimal("amount_paid"));
    System.out.println("Deadline: " + results.getDate("deadline"));
    System.out.println("Finalised: " + results.getBoolean("is_finalised"));
    System.out.println("Completion Date: " + results.getDate("completion_date"));
    System.out.println("Customer ID: " + results.getInt("customer_id"));
    System.out.println("Architect ID: " + results.getInt("architect_id"));
    System.out.println("Contractor ID: " + results.getInt("contractor_id"));
    System.out.println("Structural Engineer ID: " + results.getInt("structural_engineer_id"));
    System.out.println("Project Manager ID: " + results.getInt("project_manager_id"));

    // Close the project details block.
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