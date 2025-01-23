/**
 * This class represents a step entity, which defines an individual step 
 * associated with a project. Each step includes details like its unique ID, 
 * the associated project ID, the step description, and the order in which the step 
 * should be executed.
 */
package projects.entity;

public class Step {
  private Integer stepId; // Unique identifier for the step
  private Integer projectId; // ID of the project to which this step belongs
  private String stepText; // Description or details of the step
  private Integer stepOrder; // Execution order of the step within the project

  /**
   * Gets the ID of the step.
   * 
   * @return The step ID
   */
  public Integer getStepId() {
    return stepId;
  }

  /**
   * Sets the ID of the step.
   * 
   * @param stepId The step ID to set
   */
  public void setStepId(Integer stepId) {
    this.stepId = stepId;
  }

  /**
   * Gets the ID of the project associated with this step.
   * 
   * @return The project ID
   */
  public Integer getProjectId() {
    return projectId;
  }

  /**
   * Sets the ID of the project associated with this step.
   * 
   * @param projectId The project ID to set
   */
  public void setProjectId(Integer projectId) {
    this.projectId = projectId;
  }

  /**
   * Gets the description or details of the step.
   * 
   * @return The step description
   */
  public String getStepText() {
    return stepText;
  }

  /**
   * Sets the description or details of the step.
   * 
   * @param stepText The step description to set
   */
  public void setStepText(String stepText) {
    this.stepText = stepText;
  }

  /**
   * Gets the execution order of the step within the project.
   * 
   * @return The step order
   */
  public Integer getStepOrder() {
    return stepOrder;
  }

  /**
   * Sets the execution order of the step within the project.
   * 
   * @param stepOrder The step order to set
   */
  public void setStepOrder(Integer stepOrder) {
    this.stepOrder = stepOrder;
  }

  /**
   * Overrides the `toString` method to provide a simple representation 
   * of the step, including the step ID and description.
   * 
   * @return A string representation of the step
   */
  @Override
  public String toString() {
    return "ID=" + stepId + ", stepText=" + stepText;
  }
}
