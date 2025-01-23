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
    if (Integer.class.equals(classType)) return Types.INTEGER;
    if (String.class.equals(classType)) return Types.VARCHAR;
    if (Double.class.equals(classType)) return Types.DOUBLE;
    if (BigDecimal.class.equals(classType)) return Types.DECIMAL;
    if (LocalTime.class.equals(classType)) return Types.OTHER;

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

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      setParameter(stmt, 1, id, Integer.class);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
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
   * Extracts a Java object of a given type from a result set.
   * 
   * @param <T> The type of the object.
   * @param rs The result set.
   * @param classType The class type of the object.
   * @return The extracted object.
   * @throws DaoException If object creation fails.
   */
  protected <T> T extract(ResultSet rs, Class<T> classType) {
    try {
      Constructor<T> con = classType.getConstructor();
      T obj = con.newInstance();

      for (Field field : classType.getDeclaredFields()) {
        String colName = camelCaseToSnakeCase(field.getName());
        Class<?> fieldType = field.getType();

        field.setAccessible(true);
        Object fieldValue = null;

        try {
          fieldValue = rs.getObject(colName);
        } catch (SQLException e) {
        }

        if (Objects.nonNull(fieldValue)) {
          if (fieldValue instanceof Time && fieldType.equals(LocalTime.class)) {
            fieldValue = ((Time) fieldValue).toLocalTime();
          } else if (fieldValue instanceof Timestamp && fieldType.equals(LocalDateTime.class)) {
            fieldValue = ((Timestamp) fieldValue).toLocalDateTime();
          }

          field.set(obj, fieldValue);
        }
      }

      return obj;

    } catch (Exception e) {
      throw new DaoException("Unable to create object of type " + classType.getName(), e);
    }
  }

  /**
   * Converts camelCase identifiers to snake_case.
   * 
   * @param identifier The camelCase identifier.
   * @return The snake_case equivalent.
   */
  private String camelCaseToSnakeCase(String identifier) {
    StringBuilder nameBuilder = new StringBuilder();

    for (char ch : identifier.toCharArray()) {
      if (Character.isUpperCase(ch)) {
        nameBuilder.append('_').append(Character.toLowerCase(ch));
      } else {
        nameBuilder.append(ch);
      }
    }

    return nameBuilder.toString();
  }

  /**
   * A custom exception for DAO-related errors.
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
