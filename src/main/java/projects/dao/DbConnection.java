package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import projects.exception.DbException;

/**
 * This class handles database connections for the application. It establishes a
 * connection to a MySQL database using the specified credentials.
 */
public class DbConnection {
	// Database connection details
	private static String HOST = "localhost"; // Hostname of the database server
	private static String PASSWORD = "projects"; // Password for the database user
	private static int PORT = 3306; // Port number for the MySQL server
	private static String SCHEMA = "projects"; // Database schema to connect to
	private static String USER = "projects"; // Username for the database

	/**
	 * Establishes a connection to the database using the specified credentials.
	 * 
	 * @return A Connection object if the connection is successful.
	 * @throws DbException If the connection fails.
	 */
	public static Connection getConnection() {
		// Construct the URI for the database connection
		String uri = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", HOST, PORT, SCHEMA, USER, PASSWORD);

		try {
			// Attempt to establish the connection
			Connection conn = DriverManager.getConnection(uri);
			System.out.println("Connection to schema '" + SCHEMA + "' is successful."); // Success message
			return conn; // Return the established connection
		} catch (SQLException e) {
			// Print error message and throw a custom exception if the connection fails
			System.out.println("Unable to get connection at " + uri);
			throw new DbException("Unable to get connection at " + uri);
		}
	}
}
