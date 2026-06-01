package poisepms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/** Handles safe user input for text, numbers, money values, and dates. */
public class InputHelper {

  // <=== SCANNER ===>

  // One shared Scanner prevents input bugs caused by creating many Scanner objects.
  private static final Scanner input = new Scanner(System.in);

  // <=== READ RAW TEXT ===>

  /** Reads one line of text exactly as entered by the user. */
  public static String readRawText() {
    // Return the next full line of user input.
    return input.nextLine();
  }

  // <=== READ REQUIRED TEXT ===>

  /** Reads text that cannot be blank. */
  public static String readText(String prompt) {
    // Keep asking until the user enters a non-blank value.
    while (true) {
      System.out.print(prompt);

      // Trim accidental spaces before and after the input.
      String userInput = input.nextLine().trim();

      // Return the value if it is not blank.
      if (!userInput.isBlank()) {
        return userInput;
      }

      // Explain why the input was rejected.
      System.out.println("Input cannot be blank.");
    }
  }

  // <=== READ OPTIONAL TEXT ===>

  /** Reads text that may be blank. */
  public static String readTextAllowBlank(String prompt) {
    // Show the prompt.
    System.out.print(prompt);

    // Return the input even if the user only presses Enter.
    return input.nextLine().trim();
  }

  // <=== READ INTEGER ===>

  /** Reads a valid whole number. */
  public static int readInt(String prompt) {
    // Keep asking until the user enters a valid whole number.
    while (true) {
      System.out.print(prompt);

      // Read the input as text first so invalid input can be handled safely.
      String userInput = input.nextLine().trim();

      try {
        // Convert the text input into an integer.
        return Integer.parseInt(userInput);

      } catch (NumberFormatException error) {
        // Explain the expected input format.
        System.out.println("Please enter a valid whole number.");
      }
    }
  }

  // <=== READ MONEY VALUE ===>

  /** Reads a valid decimal number for money values. */
  public static BigDecimal readBigDecimal(String prompt) {
    // Keep asking until the user enters a valid decimal number.
    while (true) {
      System.out.print(prompt);

      // Read the input as text first.
      String userInput = input.nextLine().trim();

      try {
        // BigDecimal is better than double for money values.
        return new BigDecimal(userInput);

      } catch (NumberFormatException error) {
        // Explain the expected input format.
        System.out.println("Please enter a valid number.");
      }
    }
  }

  // <=== READ DATE ===>

  /** Reads a valid date in YYYY-MM-DD format. */
  public static LocalDate readDate(String prompt) {
    // Keep asking until the user enters a valid date.
    while (true) {
      System.out.print(prompt);

      // Read the input as text first.
      String userInput = input.nextLine().trim();

      try {
        // LocalDate.parse expects YYYY-MM-DD by default.
        return LocalDate.parse(userInput);

      } catch (DateTimeParseException error) {
        // Explain the required date format.
        System.out.println("Please enter a valid date in YYYY-MM-DD format.");
      }
    }
  }
}