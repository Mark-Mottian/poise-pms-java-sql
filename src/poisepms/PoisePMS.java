package poisepms;

/** Runs the main menu and controls the overall program flow. */
public class PoisePMS {

  // <=== MAIN METHOD ===>

  /** Starts the PoisePMS console application. */
  public static void main(String[] args) {
    // Task Requirement: Use JDBC to interact with the PoisePMS database.
    DatabaseManager.testDatabaseConnection();

    // Start the main menu after the database connection has been confirmed.
    showMainMenu();
  }

  // <=== MAIN MENU ===>

  private static void showMainMenu() {
    // Keep the program running until the user chooses to exit.
    boolean running = true;

    // Keep showing the menu while the program is running.
    while (running) {
      System.out.println();
      System.out.println("<=== POISEPMS MAIN MENU ===>");
      System.out.println("1. View all projects");
      System.out.println("2. View all people");
      System.out.println("3. Add a new person");
      System.out.println("4. Capture a new project");
      System.out.println("5. Update an existing project");
      System.out.println("6. Finalise a project");
      System.out.println("7. View incomplete projects");
      System.out.println("8. View overdue projects");
      System.out.println("9. Search for a project");
      System.out.println("10. Delete project or person data");
      System.out.println("0. Exit");
      System.out.print("Choose an option: ");

      // Read the user's menu choice as text to avoid Scanner number-input issues.
      String menuChoice = InputHelper.readRawText();

      // Send the user to the method that matches their menu choice.
      switch (menuChoice) {
        case "1":
          ProjectService.viewAllProjects();
          break;

        case "2":
          PersonService.viewAllPeople();
          break;

        case "3":
          PersonService.addNewPerson();
          break;

        case "4":
          ProjectService.captureNewProject();
          break;

        case "5":
          ProjectService.updateExistingProject();
          break;

        case "6":
          ProjectService.finaliseProject();
          break;

        case "7":
          ProjectService.viewIncompleteProjects();
          break;

        case "8":
          ProjectService.viewOverdueProjects();
          break;

        case "9":
          ProjectService.searchForProject();
          break;

        case "10":
          deleteProjectOrPersonData();
          break;

        case "0":
          // Stop the loop so the program can close cleanly.
          running = false;
          System.out.println("Goodbye.");
          break;

        default:
          // Handle anything that is not one of the listed menu options.
          System.out.println("Invalid option. Please try again.");
      }
    }
  }

  // <=== DELETE MENU ===>

  private static void deleteProjectOrPersonData() {
    // Task Requirement: Delete data about projects and people associated with them.

    System.out.println();
    System.out.println("<=== DELETE PROJECT OR PERSON DATA ===>");
    System.out.println("1. Delete project");
    System.out.println("2. Delete customer");
    System.out.println("3. Delete architect");
    System.out.println("4. Delete contractor");
    System.out.println("5. Delete structural engineer");
    System.out.println("6. Delete project manager");
    System.out.print("Choose what to delete: ");

    // Read the user's delete choice.
    String deleteChoice = InputHelper.readRawText();

    // Delete the selected type of record.
    switch (deleteChoice) {
      case "1":
        ProjectService.deleteProject();
        break;

      case "2":
        PersonService.deletePerson("customers", "customer_id", "customer");
        break;

      case "3":
        PersonService.deletePerson("architects", "architect_id", "architect");
        break;

      case "4":
        PersonService.deletePerson("contractors", "contractor_id", "contractor");
        break;

      case "5":
        PersonService.deletePerson(
            "structural_engineers", "structural_engineer_id", "structural engineer");
        break;

      case "6":
        PersonService.deletePerson("project_managers", "project_manager_id", "project manager");
        break;

      default:
        // Stop early if the user chooses an invalid delete option.
        System.out.println("Invalid delete option.");
    }
  }
}