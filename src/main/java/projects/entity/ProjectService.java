package projects.entity;

import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectService {
	private ProjectDao projectDao = new ProjectDao(); // DAO instance for interacting with the database

	/**
	 * Adds a new project by delegating the operation to the DAO layer.
	 * 
	 * @param project The `Project` entity to be added to the database.
	 * @return The `Project` entity after it has been added to the database,
	 *         including any generated ID or other database-set fields.
	 */
	public Project addProject(Project project) {
		return projectDao.insertProject(project); // Calls the DAO method to insert the project
	}
}
