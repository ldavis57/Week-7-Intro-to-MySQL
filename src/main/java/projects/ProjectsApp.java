package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

/**
 * This class creates a menu that performs CRUD operations on the tables in the
 * project database.
 */
public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();

	// Define a list of available menu options
	private List<String> operations = List.of("1) Add a project");

	// Set the entry point for application.
	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();
	}

	/**
	 * Drives the application -- Displays the menu, retrieves the user's selection,
	 * and performs the selected action. Repeats until the user chooses to exit.
	 */
	private void processUserSelections() {
		boolean done = false;

		while (!done) {
			try {
				int selection = getUserSelection(); // Get user menu selection

				switch (selection) {
				case -1: // User chooses to exit
					done = exitMenu();
					break;

				case 1:
					createProject(); // Add a new project
					break;

				default: // Invalid selection
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
					break;
				}
			} catch (Exception e) {
				System.out.println("\nError: " + e + " Try again.");
			}
		}
	}

	/*
	 * Creates a new project by getting input from the user and adding it to the database.
	 */
	private void createProject() {
		// Collect project details from the user.
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		//Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
	    // Prompt for project difficulty (1-5) with validation
		Integer difficulty = null;
	    
		// Prompt for level of difficulty and check to be sure it's between 1 and 5.
		
		while (difficulty == null || difficulty < 1 || difficulty > 5) {
		    try {
		        // Prompt the user for difficulty input
		        String input = getStringInput("Enter the project difficulty (1-5)");
		        difficulty = Integer.parseInt(input); // Attempt to parse the input as an integer

		        // Check if the input is within the valid range
		        if (difficulty < 1 || difficulty > 5) {
		            System.out.println("\n" + difficulty + " is not a valid level of difficulty. Please enter a value between 1 and 5.");
		            difficulty = null; // Reset to null to stay in the loop
		        }
		    } catch (NumberFormatException e) {
		        // Handle non-numeric input
		        System.out.println("\nInvalid input. Please enter a numeric value between 1 and 5.");
		    }
		}
		
		String notes = getStringInput("Enter the project notes");

		// Create a new Project object and set its properties.
		Project project = new Project();

		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);

		// Add the project to the database and show success message.
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
	}

	/**
	 * Converts user input into a BigDecimal. Throws an error if input is invalid.
	 * 
	 * @param prompt The message displayed to the user
	 * @return A BigDecimal value or null
	 */
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null; // Return null if the input is empty
		}

		try {
			// Parse input to BigDecimal and set to 2 decimal places.
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}

	/**
	 * Exits the menu and terminates the application.
	 * 
	 * @return true to indicate the menu should close.
	 */
	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}

	/**
	 * Displays the menu and retrieves the user's selection.
	 * 
	 * @return selection as an integer.
	 */
	private int getUserSelection() {
		printOperations();

		Integer input = getIntInput("Enter a menu selection");

		return Objects.isNull(input) ? -1 : input;
	}

	/**
	 * Converts user input into an Integer. Throws an error if input is invalid.
	 * 
	 * @param prompt The message displayed to the user
	 * @return An Integer value or null
	 */
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}

		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}

	/**
	 * Prompts the user for a string input and trims any whitespace before/after.
	 * 
	 * @param prompt The message displayed to the user
	 * @return The trimmed user input or null if blank
	 */
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();

		return input.isBlank() ? null : input.trim(); // Return null if input is blank
	}

	/**
	 * Prints the menu selections to the console one at a time.
	 */
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");

		// Print each menu option
		operations.forEach(line -> System.out.println("  " + line));
	}
}
