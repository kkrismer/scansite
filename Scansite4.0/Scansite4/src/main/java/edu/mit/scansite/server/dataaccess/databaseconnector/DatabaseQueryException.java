package edu.mit.scansite.server.dataaccess.databaseconnector;

import java.io.Serializable;

import edu.mit.scansite.shared.DatabaseException;

/**
 * An exception class for exceptions that occur when querying a Database.
 * 
 * @author Tobias Ehrenberger
 */
public class DatabaseQueryException extends DatabaseException implements
		Serializable {
	private static final long serialVersionUID = -1283434229025403022L;

	public DatabaseQueryException(final String message, final Exception cause) {
		super(message, cause);
	}

	public DatabaseQueryException() {
	}

	public DatabaseQueryException(final String message) {
		super(message);
	}
}
