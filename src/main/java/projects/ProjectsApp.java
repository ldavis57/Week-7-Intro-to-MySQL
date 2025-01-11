/**
 * Main package for the projects application.
 */
package projects;

import projects.dao.DbConnection;

/**
 * Main class for the Projects application.
 */
public class ProjectsApp {

	/**
	 * The entry point of the application.
	 */
	public static void main(String[] args) {
		// Establish a connection to the database using the DbConnection class.
		DbConnection.getConnection();
	}
}
