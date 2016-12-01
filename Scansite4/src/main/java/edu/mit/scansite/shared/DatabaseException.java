package edu.mit.scansite.shared;


/**
 * An exception class for exceptions that occur when using the DbConnector.
 * 
 * @author Tobias Ehrenberger
 */
public abstract class DatabaseException extends ScansiteException {
	private static final long serialVersionUID = 3664509933276332711L;

	public DatabaseException() {
		super();
	}

	public DatabaseException(String message, Exception cause) {
		super(message, cause);
	}

	public DatabaseException(Exception cause) {
		super(cause);
	}

	public DatabaseException(final String message) {
		super(message);
	}
}
