/**
 * This class represents a project entity with details such as project ID, name, 
 * estimated and actual hours, difficulty level, and notes. It also contains lists 
 * of associated materials, steps, and categories related to the project. 
 * The class provides getter and setter methods for accessing and modifying 
 * these properties and overrides the `toString` method for detailed output.
 */
package projects.entity;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class Project {
  private Integer projectId; // Unique identifier for the project
  private String projectName; // Name of the project
  private BigDecimal estimatedHours; // Estimated hours to complete the project
  private BigDecimal actualHours; // Actual hours spent on the project
  private Integer difficulty; // Difficulty level of the project (e.g., 1-5 scale)
  private String notes; // Additional notes about the project

  // Lists to store associated entities for the project
  private List<Material> materials = new LinkedList<>(); // List of materials required for the project
  private List<Step> steps = new LinkedList<>(); // List of steps involved in the project
  private List<Category> categories = new LinkedList<>(); // List of categories associated with the project

  // Getters and setters for each field
  public Integer getProjectId() {
    return projectId;
  }

  public void setProjectId(Integer projectId) {
    this.projectId = projectId;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public BigDecimal getEstimatedHours() {
    return estimatedHours;
  }

  public void setEstimatedHours(BigDecimal estimatedHours) {
    this.estimatedHours = estimatedHours;
  }

  public BigDecimal getActualHours() {
    return actualHours;
  }

  public void setActualHours(BigDecimal actualHours) {
    this.actualHours = actualHours;
  }

  public Integer getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(Integer difficulty) {
    this.difficulty = difficulty;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public List<Material> getMaterials() {
    return materials;
  }

  public List<Step> getSteps() {
    return steps;
  }

  public List<Category> getCategories() {
    return categories;
  }

  /**
   * Overrides the `toString` method to give a detailed representation of the project.
   * Includes all project properties and the associated materials, steps, and categories.
   * 
   * @return A string representation of the project.
   */
  @Override
  public String toString() {
    String result = ""; // Initialize an empty string for the result

    // Add project details to the result
    result += "\n   ID=" + projectId;
    result += "\n   name=" + projectName;
    result += "\n   estimatedHours=" + estimatedHours;
    result += "\n   actualHours=" + actualHours;
    result += "\n   difficulty=" + difficulty;
    result += "\n   notes=" + notes;

    // Append materials to the result
    result += "\n   Materials:";
    for (Material material : materials) {
      result += "\n      " + material;
    }

    // Append steps to the result
    result += "\n   Steps:";
    for (Step step : steps) {
      result += "\n      " + step;
    }

    // Append categories to the result
    result += "\n   Categories:";
    for (Category category : categories) {
      result += "\n      " + category;
    }

    return result; // Return the complete result
  }
}
