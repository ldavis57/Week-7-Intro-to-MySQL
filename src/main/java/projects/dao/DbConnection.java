
package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import projects.exception.DbException;

public class DbConnection {

	// Database connection details
	private static final String SCHEMA = "projects";
	private static final String USER = "projects";
	private static final String PASSWORD = "projects";
	private static final String HOST = "localhost";
	private static final int PORT = 3306;

	/**
	 * Establishes a connection to the database.
	 * 
	 * @return A Connection object representing the database connection.
	 * @throws DbException if a connection cannot be established.
	 */
	public static Connection getConnection() {
		
		// Constructing the database connection URI
	    String url = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false", HOST, PORT, SCHEMA, USER,
				PASSWORD);
		System.out.println("Connecting with url = " + url);
		try {
			// Attempting to establish a connection
		      Connection conn = DriverManager.getConnection(url);
			System.out.println("Connection successful.");
			return conn;
		} catch (SQLException e) {
			// Handling exceptions if connection fails
		      
			System.out.println("Connection failed.");
			throw new DbException(e);
		}
	}
}
