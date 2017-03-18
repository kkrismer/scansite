package edu.mit.scansite.server.dataaccess;

import java.util.ArrayList;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.user.UserAddCommand;
import edu.mit.scansite.server.dataaccess.commands.user.UserDeleteCommand;
import edu.mit.scansite.server.dataaccess.commands.user.UserGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.user.UserGetCommand;
import edu.mit.scansite.server.dataaccess.commands.user.UserUpdateCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserDaoImpl extends DaoImpl implements UserDao {

	public UserDaoImpl(Properties dbAccessConfig, Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.UserDao#get(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public User get(String email, String password) throws DataAccessException {
		UserGetCommand command = new UserGetCommand(dbAccessConfig,
				dbConstantsConfig, email, password);
		User user = null;
		try {
			user = command.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Retrieving user from DB failed.", e);
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.UserDao#add(edu.mit.scansite.shared
	 * .transferobjects.User)
	 */
	@Override
	public void add(User entry) throws DataAccessException {
		UserAddCommand command = new UserAddCommand(dbAccessConfig,
				dbConstantsConfig, entry);
		try {
			command.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.UserDao#update(edu.mit.scansite.shared
	 * .transferobjects.User, boolean)
	 */
	@Override
	public boolean update(User entry, boolean changePassword)
			throws DataAccessException {
		UserUpdateCommand command = new UserUpdateCommand(dbAccessConfig,
				dbConstantsConfig, entry, changePassword);
		try {
			return command.execute() > 0;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Updating user in DB failed.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.UserDao#delete(java.lang.String)
	 */
	@Override
	public void delete(String email) throws DataAccessException {
		UserDeleteCommand command = new UserDeleteCommand(dbAccessConfig, dbConstantsConfig, email);
		try {
			command.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Deleting user from DB failed.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.UserDao#get(java.lang.String)
	 */
	@Override
	public User get(String email) throws DataAccessException {
		UserGetCommand command = new UserGetCommand(dbAccessConfig,
				dbConstantsConfig, email);
		User user;
		try {
			user = command.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Retrieving user from DB failed.", e);
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.UserDao#getAll()
	 */
	@Override
	public ArrayList<User> getAll() throws DataAccessException {
		UserGetAllCommand command = new UserGetAllCommand(dbAccessConfig,
				dbConstantsConfig);
		ArrayList<User> users;
		try {
			users = command.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException("Retrieving users from DB failed.",
					e);
		}
		return users;
	}

}
