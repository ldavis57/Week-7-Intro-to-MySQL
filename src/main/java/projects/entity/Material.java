/**
 * This class represents a material entity, which contains details about a specific material 
 * used in a project. It includes fields for material ID, project ID, material name, 
 * quantity required, and cost. It provides getter and setter methods for each field 
 * and overrides the `toString` method for easy display of material information.
 */
package projects.entity;

import java.math.BigDecimal;

public class Material {
  private Integer materialId; // Unique identifier for the material
  private Integer projectId; // ID of the project associated with this material
  private String materialName; // Name of the material
  private Integer numRequired; // Number of units required for the material
  private BigDecimal cost; // Cost of the material

  /**
   * Gets the ID of the material.
   * 
   * @return the material ID
   */
  public Integer getMaterialId() {
    return materialId;
  }

  /**
   * Sets the ID of the material.
   * 
   * @param materialId the ID to set for the material
   */
  public void setMaterialId(Integer materialId) {
    this.materialId = materialId;
  }

  /**
   * Gets the ID of the project associated with this material.
   * 
   * @return the project ID
   */
  public Integer getProjectId() {
    return projectId;
  }

  /**
   * Sets the ID of the project associated with this material.
   * 
   * @param projectId the project ID to set
   */
  public void setProjectId(Integer projectId) {
    this.projectId = projectId;
  }

  /**
   * Gets the name of the material.
   * 
   * @return the material name
   */
  public String getMaterialName() {
    return materialName;
  }

  /**
   * Sets the name of the material.
   * 
   * @param materialName the name to set for the material
   */
  public void setMaterialName(String materialName) {
    this.materialName = materialName;
  }

  /**
   * Gets the number of units required for this material.
   * 
   * @return the number of units required
   */
  public Integer getNumRequired() {
    return numRequired;
  }

  /**
   * Sets the number of units required for this material.
   * 
   * @param numRequired the quantity to set
   */
  public void setNumRequired(Integer numRequired) {
    this.numRequired = numRequired;
  }

  /**
   * Gets the cost of the material.
   * 
   * @return the cost of the material
   */
  public BigDecimal getCost() {
    return cost;
  }

  /**
   * Sets the cost of the material.
   * 
   * @param cost the cost to set for the material
   */
  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  /**
   * Provides a string representation of the material object.
   * The format includes material ID, name, number required, and cost.
   * 
   * @return a string representation of the material
   */
  @Override
  public String toString() {
    return "ID=" + materialId + ", materialName=" + materialName + ", numRequired=" + numRequired + ", cost=" + cost;
  }
}
