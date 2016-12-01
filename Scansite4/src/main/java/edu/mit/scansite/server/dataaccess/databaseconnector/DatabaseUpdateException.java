package edu.mit.scansite.server.dataaccess.databaseconnector;

import java.io.Serializable;

import edu.mit.scansite.shared.DatabaseException;

/**
 * An exception class for exceptions that occur when executing DML- or
 * DDL-statements on a Database.
 * 
 * @author Tobias Ehrenberger
 */
public class DatabaseUpdateException extends DatabaseException implements
		Serializable {
	private static final long serialVersionUID = 8423788126254230264L;

	public DatabaseUpdateException(final String message, final Exception cause) {
		super(message, cause);
	}

	public DatabaseUpdateException() {
	}

	public DatabaseUpdateException(final String message) {
		super(message);
	}
}
