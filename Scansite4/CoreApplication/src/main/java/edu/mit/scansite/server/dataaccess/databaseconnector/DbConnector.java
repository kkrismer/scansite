package edu.mit.scansite.server.dataaccess.databaseconnector;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.commands.motif.MotifCountGetCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * A simple class for using a database (SELECT, INSERT und UPDATE).
 *
 * @author Thomas Bernwinkler
 * @author Konstantin Krismer
 * @author Tobias Ehrenberger
 */
public class DbConnector {
    private final Logger logger = LoggerFactory.getLogger(DbConnector.class);

    private Connection[] connections;
    private int poolSize;
    private int activeConnectionNo;
    private static DbConnector instance;
    private Properties properties;

    private boolean reestablishMode = false;

    private DbConnector() {
        activeConnectionNo = 0;
        poolSize = 25;
        init();
    }

    private void init() {
        connections = new Connection[poolSize];
        try {
            if (properties == null) {
                properties = ServiceLocator.getDbAccessProperties();
            }
        } catch (DataAccessException e) {
            logger.error("Could not find database connection files. Assuming Web Service is running.");
        }
        for (int i = 0; i < poolSize; i++) {
            establishConnection();
        }
    }

    private void updateActiveConnectionNo() {
        activeConnectionNo++;
        activeConnectionNo = activeConnectionNo % 25;
    }

    public void resizeConnectionPool(int size) {
        poolSize = size;
        init();
    }

    private Connection establishConnection() {
        String urlStr = "DB_CONNECTION_STRING";
        String usrStr = "DB_USER";
        String pwdStr = "DB_PASSWORD";

        try {
            // checking for the correct driver
            // (changed from com.mysql.jdbc.Driver to com.mysql.cj.jdbc.Driver)
            try {
                Class.forName((String) properties.get("DB_DRIVER"));
//				logger.info("Driver loaded!");
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Cannot find the driver in the classpath!", e);
            }
            if (connections[activeConnectionNo] != null && !connections[activeConnectionNo].isClosed())
                return connections[activeConnectionNo];
            try {
                String url = (String) properties.get(urlStr);
                String usr = (String) properties.get(usrStr);
                String pwd = (String) properties.get(pwdStr);
                connections[activeConnectionNo] = DriverManager.getConnection(url, usr, pwd);
                logger.info("New connection was established.");
            } catch (SQLException e) {
                logger.error("Could not establish connection based on configuration\n" + e.getMessage());
            }
        } catch (SQLException e) {
            logger.error("Could not check SQL connection status \n" + e.getMessage());
        }
        return connections[activeConnectionNo];
    }

//    public void setWebServiceProperties(Properties properties) {
//	    this.properties = properties;
//	    logger.info("Prepared Web Service database access");
//    }

    public static DbConnector getInstance() {
        if (instance == null) {
            instance = new DbConnector();
        }
        return instance;
    }

    /**
     * Closes given result set hard
     */
    public void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * Closes given PreparedStatement set hard
     */
    public void close(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * Retrieves a connection from the Tomcat connection pool, returns stored
     * single connection in single connection mode
     */
    public Connection getConnection() {
        while (reestablishMode) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                logger.info("Reestablishing database connections...");
            } catch (InterruptedException e) {
                logger.error("Wait while reestablishing database connections was interrupted.");
                e.printStackTrace();
            }
        }
        Connection con = establishConnection();
//        while (con == null) { // RPC thread issue prevention
//            con = establishConnection();
//        }
        checkConnection(con);
        if (poolSize > 1) { //poolSize is reduced to 1 for database setup
            updateActiveConnectionNo(); // which is necessary for disabling auto commits and constraints
        }
        return con;
    }

    private Connection checkConnection (Connection connection) {
        final int timeout = 3;
        try { // if one connection is inactive: reestablish pool
            if(connection == null || !connection.isValid(timeout) || !testConnection()) {
                resetConnections();
            }
        } catch (SQLException e) {
            logger.warn("Failed checking connection. Closing and reestablishing, if possible");
        }
        return connection;
    }


    boolean testConnection() {
        boolean expected = false;
        if(connections.length == 1) {
            return true;
        }
        try {
            MotifCountGetCommand cmd = new MotifCountGetCommand(ServiceLocator.getDbAccessProperties(),
                    ServiceLocator.getDbConstantsProperties(), MotifClass.MAMMALIAN, true);

            String query = "";
            try {
                Statement statement = connections[activeConnectionNo].createStatement();
                query = cmd.doGetSqlStatement();
                ResultSet resultSet = statement.executeQuery(query);

                Integer result = null;
                if (!resultSet.isLast()) {
                    result = cmd.doProcessResults(resultSet);
                }
                DbConnector.getInstance().close(resultSet);
                DbConnector.getInstance().close(statement);

                expected = result > -1;
            } catch (Exception e) {
                DataAccessException e2 = new DataAccessException(
                        "executing SELECT-Statement failed: " + query + " (" + e.getMessage() + ")", e);
                logger.error(e2.getMessage(), e2);
                throw e2;
            }
        } catch (DatabaseException e1) {
            logger.warn("Could not connect to database. Resetting connections");
            e1.printStackTrace();
            try {
                resetConnections();
            } catch (SQLException  e2) {
                logger.error("Resetting connections failed!");
                e2.printStackTrace();
            }
            return false;
        }

        return expected;
    }

    public void resetConnections() throws SQLException {
        reestablishMode = true;
        for (int i = 0; i < connections.length; i++) {
            if (connections[i] != null) {
                connections[i].close();
                connections[i] = null;
            }
        }
        for (int i = 0; i < connections.length; i++) {
            connections[i] = establishConnection();
        }
        reestablishMode = false;
    }


    public void close(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            logger.info("Closed connection");
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * @return The last automatically generated key value
     */
    public int executeInsertQuery(final String query) throws DataAccessException {
        establishConnection();
        try {
            int id = 0;

            Statement statement = connections[activeConnectionNo].createStatement();
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                if (id < resultSet.getInt(1)) {
                    id = resultSet.getInt(1);
                }
            }
            close(resultSet);
            close(statement);
            return id;
        } catch (Exception e) {
            DataAccessException e2 = new DataAccessException(
                    "executing INSERT-Statement failed: " + query, e);
            logger.error(e2.getMessage(), e2);
            throw e2;
        }
    }


    /**
     * @return The number of rows updated or, in case of DDL statements, zero.
     */
    public int executeUpdateQuery(final String query) throws DataAccessException {
        try {
            int id;
            Statement statement = connections[activeConnectionNo].createStatement();
            id = statement.executeUpdate(query);
            close(statement);
            return id;
        } catch (Exception e) {
            DataAccessException e2 = new DataAccessException(
                    "executing UPDATE-Statement failed: " + query, e);
            logger.error(e2.getMessage(), e2);
            throw e2;
        }
    }


    public void setAutoCommit(boolean autoCommit) throws DatabaseAccessException {
        try {
            for (int i = 0; i < poolSize; i++) {
                connections[i].setAutoCommit(autoCommit);
            }
        } catch (Exception e) {
            throw new DatabaseAccessException(e.getMessage(), e);
        }
    }

//    public void setChecks(boolean enabled) throws SQLException, DatabaseAccessException {
//        establishConnection();
//        int checks = enabled ? 1 : 0;
//        if (!enabled) { // disable auto commit before prepared statement manual commit
//            instance.setAutoCommit(enabled);
//        }
//        for (int i = 0; i < poolSize; i++) {
//            PreparedStatement uniqueStmt = connections[i].prepareStatement("SET UNIQUE_CHECKS=?;");
//            uniqueStmt.setInt(1, checks);
//            PreparedStatement foreignKeyStmt = connections[i].prepareStatement("SET FOREIGN_KEY_CHECKS=?;");
//            foreignKeyStmt.setInt(1, checks);
//            connections[i].commit();
//        }
//        if (enabled) { //reenable auto commit after prepared statement manual commit
//            instance.setAutoCommit(enabled);
//        }
//    }
}
