package edu.mit.scansite.server.dataaccess.databaseconnector;

import java.io.Serializable;

import edu.mit.scansite.shared.DatabaseException;

/**
 * An exception class for exceptions that occur when accessing a Database.
 * 
 * @author Tobias Ehrenberger
 */
public class DatabaseAccessException extends DatabaseException implements
		Serializable {
	private static final long serialVersionUID = -1628750504033155165L;

	public DatabaseAccessException() {
	}

	public DatabaseAccessException(final String message, final Exception cause) {
		super(message, cause);
	}

	public DatabaseAccessException(final String message) {
		super(message);
	}
}
