package edu.mit.scansite.server.dataaccess;

import java.util.ArrayList;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Konstantin Krismer
 */
public interface UserDao extends Dao {

	/**
	 * @param email
	 * @param password
	 * @return NULL if no user is found with the given email and password.
	 * @throws DataAccessException
	 */
	public abstract User get(String email, String password)
			throws DataAccessException;

	public abstract void add(User entry) throws DataAccessException;

	public abstract boolean update(User entry, boolean changePassword)
			throws DataAccessException;

	public abstract void delete(String email) throws DataAccessException;

	public abstract User get(String email) throws DataAccessException;

	public abstract ArrayList<User> getAll() throws DataAccessException;

}