/**
 * This abstract class provides common utility methods for Data Access Object (DAO) classes.
 * It includes methods for managing database transactions, setting SQL parameters, 
 * retrieving generated keys, and converting data between SQL and Java formats.
 */
package provided.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public abstract class DaoBase {

  /**
   * Starts a database transaction by setting auto-commit to false.
   * 
   * @param conn The database connection.
   * @throws SQLException If an SQL error occurs.
   */
  protected void startTransaction(Connection conn) throws SQLException {
    conn.setAutoCommit(false);
  }

  /**
   * Commits the current transaction.
   * 
   * @param conn The database connection.
   * @throws SQLException If an SQL error occurs.
   */
  protected void commitTransaction(Connection conn) throws SQLException {
    conn.commit();
  }

  /**
   * Rolls back the current transaction.
   * 
   * @param conn The database connection.
   * @throws SQLException If an SQL error occurs.
   */
  protected void rollbackTransaction(Connection conn) throws SQLException {
    conn.rollback();
  }

  /**
   * Sets a parameter for a prepared SQL statement, converting Java types to SQL types.
   * 
   * @param stmt The prepared statement.
   * @param parameterIndex The index of the parameter (1-based).
   * @param value The value to set.
   * @param classType The Java class type of the value.
   * @throws SQLException If an SQL error occurs.
   */
  protected void setParameter(PreparedStatement stmt, int parameterIndex, Object value, Class<?> classType) throws SQLException {
    int sqlType = convertJavaClassToSqlType(classType);

    if (Objects.isNull(value)) {
      stmt.setNull(parameterIndex, sqlType);
    } else {
      switch (sqlType) {
        case Types.DECIMAL:
          stmt.setBigDecimal(parameterIndex, (BigDecimal) value);
          break;
        case Types.DOUBLE:
          stmt.setDouble(parameterIndex, (Double) value);
          break;
        case Types.INTEGER:
          stmt.setInt(parameterIndex, (Integer) value);
          break;
        case Types.OTHER:
          stmt.setObject(parameterIndex, value);
          break;
        case Types.VARCHAR:
          stmt.setString(parameterIndex, (String) value);
          break;
        default:
          throw new DaoException("Unknown parameter type: " + classType);
      }
    }
  }

  /**
   * Converts Java class types to corresponding SQL types.
   * 
   * @param classType The Java class type.
   * @return The corresponding SQL type.
   * @throws DaoException If the class type is unsupported.
   */
  private int convertJavaClassToSqlType(Class<?> classType) {
    if(Integer.class.equals(classType)) {
      return Types.INTEGER;
    }

    if(String.class.equals(classType)) {
      return Types.VARCHAR;
    }

    if(Double.class.equals(classType)) {
      return Types.DOUBLE;
    }

    if(BigDecimal.class.equals(classType)) {
      return Types.DECIMAL;
    }

    if(LocalTime.class.equals(classType)) {
      return Types.OTHER;
    }
    throw new DaoException("Unsupported class type: " + classType.getName());
  }

  /**
   * Retrieves the next sequence number for a specific ID in a table.
   * 
   * @param conn The database connection.
   * @param id The ID to search for.
   * @param tableName The name of the table.
   * @param idName The name of the ID column.
   * @return The next sequence number.
   * @throws SQLException If an SQL error occurs.
   */
  protected Integer getNextSequenceNumber(Connection conn, Integer id, String tableName, String idName) throws SQLException {
    String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + idName + " = ?";

    try(PreparedStatement stmt = conn.prepareStatement(sql)) {
      setParameter(stmt, 1, id, Integer.class);

      try(ResultSet rs = stmt.executeQuery()) {
        if(rs.next()) {
          return rs.getInt(1) + 1;
        }

        return 1;
      }
    }
  }

  /**
   * Retrieves the last inserted ID for a given table.
   * 
   * @param conn The database connection.
   * @param table The table name.
   * @return The last inserted ID.
   * @throws SQLException If an SQL error occurs.
   */
  protected Integer getLastInsertId(Connection conn, String table) throws SQLException {
    String sql = String.format("SELECT LAST_INSERT_ID() FROM %s", table);

    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
      if (rs.next()) {
        return rs.getInt(1);
      }

      throw new SQLException("Unable to retrieve the primary key value. No result set!");
    }
  }

  /**
   * This extracts an object of the given type from a result set. The object must have a
   * zero-argument constructor. It builds an object from a result set using reflection as follows:
   * <ol>
   * <li>The zero-argument constructor is obtained.</li>
   * <li>An object of the given class type is created.</li>
   * <li>A list of fields is obtained using reflection.</li>
   * <li>The field name is converted from Java naming to SQL naming conventions (camel case to snake
   * case). Obviously, for this to work, the Java name must match the column name. So, if the Java
   * name is numServings, the column name must be num_servings.</li>
   * <li>The value is assigned to the field in the object.</li>
   * </ol>
   * 
   * Example: if a query returns values for a recipe, a Recipe object is returned. So:
   * 
   * <pre>
   * String sql = "SELECT * from recipe";
   * ResultSet rs = getResultSetSomehow();
   * 
   * Recipe recipe = extract(rs, Recipe.class);
   * </pre>
   * 
   * Note: if the Java field does not exist in the result set, the value of the field is left
   * unchanged. So, class Recipe has an instance variable:
   * 
   * <pre>
   * List<Ingredient> ingredients = new LinkedList<>();
   * </pre>
   * 
   * Since the result set does not contain a column named "ingredients", the value is left alone and
   * the list initialization is preserved.
   * 
   * @param <T> The Generic for the type of object to create and return.
   * @param rs The result set in which to extract values. The result set must be positioned on the
   *        correct row by the caller.
   * @param classType The actual class type of the object to create.
   * @return A populated class.
   */
  protected <T> T extract(ResultSet rs, Class<T> classType) {
    try {
      Constructor<T> con = classType.getConstructor();
      T obj = con.newInstance();

      /* Get the list of fields and loop through them. */
      for(Field field : classType.getDeclaredFields()) {
        String colName = camelCaseToSnakeCase(field.getName());
        Class<?> fieldType = field.getType();

        field.setAccessible(true);
        Object fieldValue = null;

        try {
          fieldValue = rs.getObject(colName);
        }
        catch(SQLException e) {
          /*
           * An exception caught here means that the field name isn't in the result set. Don't take
           * any action.
           */
        }

        /*
         * Only set the value in the object if there is a value with the same name in the result
         * set. This will preserve instance variables (like lists) that are assigned values when the
         * object is created.
         */
        if(Objects.nonNull(fieldValue)) {
          /*
           * Convert the following types: Time -> LocalTime, and Timestamp -> LocalDateTime.
           */
          if(fieldValue instanceof Time && fieldType.equals(LocalTime.class)) {
            fieldValue = ((Time)fieldValue).toLocalTime();
          }
          else if(fieldValue instanceof Timestamp && fieldType.equals(LocalDateTime.class)) {
            fieldValue = ((Timestamp)fieldValue).toLocalDateTime();
          }

          field.set(obj, fieldValue);
        }
      }

      return obj;

    }
    catch(Exception e) {
      throw new DaoException("Unable to create object of type " + classType.getName(), e);
    }
  }

  /**
   * This converts a camel case value (rowInsertTime) to snake case (row_insert_time).
   * 
   * @param identifier The camelCase identifier.
   * @return The snake_case equivalent.
   */
  private String camelCaseToSnakeCase(String identifier) {
    StringBuilder nameBuilder = new StringBuilder();

    for(char ch : identifier.toCharArray()) {
      if(Character.isUpperCase(ch)) {
        nameBuilder.append('_').append(Character.toLowerCase(ch));
      }
      else {
        nameBuilder.append(ch);
      }
    }

    return nameBuilder.toString();
  }

  /**
   * This class declares the exception throw by the {@link DaoBase} class. It is a thin wrapper for
   * {@link RuntimeException}.
   * 
   * @author Promineo
   *
   */
  @SuppressWarnings("serial")
  static class DaoException extends RuntimeException {

    public DaoException(String message, Throwable cause) {
      super(message, cause);
    }

    public DaoException(String message) {
      super(message);
    }
  }
}
