package projects.exception;
/*
 *  This is an unchecked exception. 
 */

/*
 * I suppressed the warnings to make it cleaner.
 */
@SuppressWarnings("serial")
public class DbException extends RuntimeException {

	// Creates an exception with a message.
	public DbException(String message) {
		super(message);
	}

	// Creates an exception with a cause.
	public DbException(Throwable cause) {
		super(cause);
	}

	// Creates an exception with both message and cause.
	public DbException(String message, Throwable cause) {
		super(message, cause);
	}
}
