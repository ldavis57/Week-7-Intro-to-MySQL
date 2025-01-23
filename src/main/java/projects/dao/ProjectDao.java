/**
 * This class provides data access methods for interacting with the `project` table in the database.
 * It includes methods for inserting a new project into the database and handles database transactions.
 */
package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import projects.entity.Project;
import projects.exception.DbException;
import provided.util.DaoBase;

@SuppressWarnings("unused")
public class ProjectDao extends DaoBase {
	// Constants representing table names in the database
	private static final String CATEGORY_TABLE = "category";
	private static final String MATERIAL_TABLE = "material";
	private static final String PROJECT_TABLE = "project";
	private static final String PROJECT_CATEGORY_TABLE = "project_category";
	private static final String STEP_TABLE = "step";

	/**
	 * Inserts a new project into the database.
	 * 
	 * @param project The `Project` object containing the details to be added.
	 * @return The `Project` object after insertion, with the generated project ID.
	 * @throws DbException If any database operation fails.
	 */
	public Project insertProject(Project project) {
		// SQL statement for inserting a new project into the `project` table

		// @formatter:off
    String sql = ""
        + "INSERT INTO " + PROJECT_TABLE + " "
        + "(project_name, estimated_hours, actual_hours, difficulty, notes) "
        + "VALUES "
        + "(?, ?, ?, ?, ?)";
    // @formatter:on

		try (Connection conn = DbConnection.getConnection()) { // Obtain a database connection
			startTransaction(conn); // Start a new transaction

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				// Set the parameters for the prepared statement
				setParameter(stmt, 1, project.getProjectName(), String.class);
				setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
				setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
				setParameter(stmt, 4, project.getDifficulty(), Integer.class);
				setParameter(stmt, 5, project.getNotes(), String.class);

				stmt.executeUpdate(); // Execute the SQL statement

				// Retrieve the ID of the newly inserted project
				Integer projectId = getLastInsertId(conn, PROJECT_TABLE);
				commitTransaction(conn); // Commit the transaction

				// Set the generated ID in the `Project` object and return it
				project.setProjectId(projectId);
				return project;
			} catch (Exception e) {
				rollbackTransaction(conn); // Rollback the transaction on error
				throw new DbException(e); // Wrap and rethrow the exception
			}
		} catch (SQLException e) {
			throw new DbException(e); // Wrap and rethrow SQL exceptions
		}
	}
}
