
package projects.entity;

/**
 * This class represents a category entity with properties for a category ID and
 * name. It provides getter and setter methods to access and modify the
 * properties. It also overrides the toString method to display category
 * details.
 */
public class Category {
	private Integer categoryId; // Unique identifier for the category
	private String categoryName; // Name of the category

	/**
	 * Gets the ID of the category.
	 * 
	 * @return the category ID
	 */
	public Integer getCategoryId() {
		return categoryId;
	}

	/**
	 * Sets the ID of the category.
	 * 
	 * @param categoryId the ID to set for the category
	 */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Gets the name of the category.
	 * 
	 * @return the category name
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * Sets the name of the category.
	 * 
	 * @param categoryName the name to set for the category
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * Returns a string representation of the category object. The format is:
	 * ID=<categoryId>, categoryName=<categoryName>
	 * 
	 * @return a string representation of the category
	 */
	@Override
	public String toString() {
		return "ID=" + categoryId + ", categoryName=" + categoryName;
	}
}
