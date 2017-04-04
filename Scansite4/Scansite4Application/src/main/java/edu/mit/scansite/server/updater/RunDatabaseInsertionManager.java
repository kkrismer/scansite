package edu.mit.scansite.server.updater;

import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.dataaccess.file.RunEvidenceInserter;
import edu.mit.scansite.server.motifinserter.RunMotifInserter;
import edu.mit.scansite.shared.DatabaseException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Thomas Bernwinkler on 15.12.2016.
 * Runs the following database insertion features for an empty database
 * - DatabaseUpdater
 * - MotifInserter (Mammals & Yeast)
 * - EvidenceInserter
 *
 * @author Thomas Bernwinkler
 */

public class RunDatabaseInsertionManager {
    private static final Logger logger = LoggerFactory.getLogger(RunUpdater.class);

    private static final String mammalFileIdentifier = "-moma";
    private static final String yeastFileIdentifier = "-moye";
    private static final String emailIdentifier = "-mail";
    private static final String evidenceIdentifier = "-evid";
    private static final String helpIdentifier = "-help";
    private static final String sqlScriptIdentifier = "-msql";

    private static String motifMammalFile;
    private static String motifYeastFile;
    private static String motifEmail;
    private static String evidenceFile;
    private static String sqlScriptFile;

    private static boolean isMammal = false;
    private static boolean isYeast = false;
    private static boolean isMail = false;
    private static boolean isEvid = false;
    private static boolean isHelp = false;
    private static boolean isScript = false;

    /**
     * As a number of parameters is required in this application,
     * it is easily able to mess up with the order of the command line arguemnts.
     * Hence the order of the parameters does not matter, however one needs to use
     * the following declarations for the parameters (including the hyphens/dashes).
     * Please also make sure to actually replace the whole term <parameter> with the
     * appropriate parameter.
     * <p>
     * -moma <path/to/motifMammalsFile>
     * -moye <path/to/motifYeastFile>
     * -mail <user@mit.edu>
     * -evid <path/to/evidenceFile>
     * -msql <path/to/scansiteDb.sql>
     * <p>
     * -help Display example call parameters
     */
    public static void main(String[] args) {
        String[] updaterParams = new String[0];
        String[] motifMammalsParams = new String[2];
        String[] motifYeastParams = new String[2];
        String[] evidenceParams = new String[1];

        String exampleCall = "Parameter arguments should be given like:" +
                " -moma misc/motifs/motifsMammals/ -moye misc/motifs/motifsYeast/" +
                " -mail krismer@mit.edu -evid misc/siteEvidence/evidence.txt" +
                " -msql misc/database/scansiteDb.sql";

        for (String arg : args) {
            if (isMammal || isYeast || isMail || isEvid || isScript) {
                if (!assignParam(arg)) {
                    logger.error("ERROR! A parameter could not be assigned!\n" + exampleCall);
                    return;
                }
            } else if (isHelp) {
                logger.info(exampleCall + " -dbip 127.0.0.1");
                return;
            }

            switch (arg) {
                case mammalFileIdentifier:
                    isMammal = true;
                    break;
                case yeastFileIdentifier:
                    isYeast = true;
                    break;
                case emailIdentifier:
                    isMail = true;
                    break;
                case evidenceIdentifier:
                    isEvid = true;
                    break;
                case helpIdentifier:
                    isHelp = true;
                    break;
                case sqlScriptIdentifier:
                    isScript = true;
                    break;
            }
        }

        motifMammalsParams[0] = motifMammalFile;
        motifMammalsParams[1] = motifEmail;
        motifYeastParams[0] = motifYeastFile;
        motifYeastParams[1] = motifEmail;
        evidenceParams[0] = evidenceFile;

        String errorFile = "";
        if (motifMammalFile == null || motifMammalFile.isEmpty()) {
            errorFile += "the Motif file (mammals)\n";
        } else if (motifYeastFile == null || motifYeastFile.isEmpty()) {
            errorFile += "The Motif file (yeast)\n";
        } else if (motifEmail == null || motifEmail.isEmpty()) {
            errorFile += "User email for motif runs\n";
        } else if (evidenceFile == null || evidenceFile.isEmpty()) {
            errorFile += "The evidence file\n";
        }

        cleanTempFiles();

        if (!errorFile.isEmpty()) {
            logger.error(errorFile + " was not assigned correctly. Please check the parameters!\n" + exampleCall);
            return;
        }

        if (sqlScriptFile.isEmpty()) {
            logger.info("Warning! Could not find the SQL script for a database reset!");
        }

        logger.info("Assigned parameters successfully! Launching applications...");

        // single long time connection as previously used for setting up the database
        // also required for disabling auto commit and constraints
        DbConnector.getInstance().resizeConnectionPool(1);
        try {
            logger.info("Resetting database...");
            resetDatabase();
        } catch (SQLException | DatabaseException e) {
            logger.error(e.getMessage());
            logger.error("Could not reset database due to an exception!");
            System.exit(1);
        }

        try {
            logger.info("Running \"RunUpdater\", Database Updater...");
            RunUpdater.main(updaterParams);

            logger.info("Running \"RunEvidenceInserter\" to insert evidence data...");
            RunEvidenceInserter.main(evidenceParams);

            //sequence rework
            logger.info("Running SequenceMotifier to adjust protein sequences according to their site evidence data");
            RunSequenceModifier.run();

            logger.info("Running \"RunMotifInserter\" to insert mammal Motifs...");
            RunMotifInserter.main(motifMammalsParams);

            logger.info("Running \"RunMotifInserter\" to insert yeast Motifs...");
            RunMotifInserter.main(motifYeastParams);

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        cleanTempFiles();
        logger.info("Done.");
    }

    /**
     * This method deletes the temp as well as the scansite4_temp folder together with all
     * the files they contain. Those files are only created to transliterate the downloads.
     * This way it is possible to remove the ``trash'' after filling the database.
     * To be on the safe side, this method is called before (in case anything crashed before)
     * the database insertion manager runs and after its run (free disc storage ASAP)
     */
    private static void cleanTempFiles() {
        logger.info("Deleting temporary files...");
        File tempDir = new File("temp/");
        File scansite4_tempDir = new File("scansite4_temp/");

        try {
            FileUtils.deleteDirectory(tempDir);
        } catch (IOException e) {
            logger.warn(e.getMessage());
            logger.warn("Could not delete the temporary directory [" + tempDir.getAbsolutePath() + "]! Please do so manually!");
        }
        try {
            FileUtils.deleteDirectory(scansite4_tempDir);
        } catch (IOException e) {
            logger.warn(e.getMessage());
            logger.warn("Could not delete the temporary directory [" + scansite4_tempDir.getAbsolutePath() + "]! Please do so manually!");
        }
    }

    /**
     * This method clears the database by dropping the existing database and creating
     * a new one based on the sql file. This way a new clean database is available
     * right before filling it with data. Hence errors that would appear by filling
     * a not properly set up database can be avoided.
     */
    private static void resetDatabase() throws SQLException, DatabaseException {
        String sqlCommands;
        String inserts;
        try {
            Charset enconding = StandardCharsets.UTF_8;
            byte[] content = Files.readAllBytes(Paths.get(sqlScriptFile));
            sqlCommands = new String(content, enconding);
            int start = sqlCommands.indexOf("CREATE TABLE");
            sqlCommands = sqlCommands.substring(start);

            int insertStart = sqlCommands.indexOf("INSERT INTO");
            int insertStop = sqlCommands.indexOf("CREATE TABLE", insertStart);
            inserts = sqlCommands.substring(insertStart, insertStop);
            sqlCommands = sqlCommands.replace(inserts, "");

        } catch (IOException e) {
            logger.error("Could not read the SQL file for the database reset!");
            return;
        }

        final String database = "scansite4";

        Connection conn = DbConnector.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        if (databaseExists(conn, database)) {
            stmt.executeUpdate(String.format("DROP DATABASE %s;", database));
        }
        stmt.executeUpdate(String.format("CREATE DATABASE %s", database));
        stmt.execute(String.format("USE %s;", database));

        final String endOfCommand = ";";
        runDatabaseCommands(stmt, sqlCommands, "CREATE TABLE", endOfCommand);
        runDatabaseCommands(stmt, inserts, "INSERT INTO", endOfCommand);

        stmt.close();
        conn.close();
    }

    /**
     * This method checks the meta data of the database in order to check
     * whether the target database currently exists
     *
     * @param connection   A valid database connection that can be used for accessing the meta data
     * @param databaseName The name of the database that is looked for in the meta data
     */
    private static boolean databaseExists(Connection connection, String databaseName) throws SQLException {
        ResultSet resultSet = connection.getMetaData().getCatalogs();
        while (resultSet.next()) {
            if (resultSet.getString(1).equals(databaseName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Executes the required SQL statements in a long string based on separators
     *
     * @param stmt           A Statement that is able to execute SQL CREATE and UPDATE statements
     * @param sqlCommands    The String value which contains one or several SQL statements
     * @param beginOfCommand Identifier for the beginning` of a new command i.e. "CREATE TABLE" or "INSERT INTO"
     * @param endOfCommand   Identifierr for the end of a SQL statement i.e. ";"
     */
    private static void runDatabaseCommands(Statement stmt, String sqlCommands, String beginOfCommand, String endOfCommand) throws SQLException {
        while (!sqlCommands.isEmpty()) {
            String nextCommand = sqlCommands.substring(sqlCommands.indexOf(beginOfCommand), sqlCommands.indexOf(endOfCommand) + 1);
            sqlCommands = sqlCommands.replace(nextCommand, "");
            while (sqlCommands.startsWith(" ") || sqlCommands.startsWith("\n") || sqlCommands.startsWith("\r")) {
                sqlCommands = sqlCommands.substring(1);
            }
            stmt.executeUpdate(nextCommand);
        }
    }

    /**
     * Checks if the requirement for assigning the value to a parameter has been met
     * assigns the value and checks if the value mistakenly starts with < as the
     * description samples are like <example parameter>
     *
     * @param value The String value which is assigned to a designated parameter
     */
    private static boolean assignParam(String value) {
        if (isMammal) {
            motifMammalFile = value;
            isMammal = false;
            return !motifMammalFile.startsWith("<");
        } else if (isYeast) {
            motifYeastFile = value;
            isYeast = false;
            return !motifYeastFile.startsWith("<");
        } else if (isMail) {
            motifEmail = value;
            isMail = false;
            return !motifEmail.startsWith("<");
        } else if (isEvid) {
            evidenceFile = value;
            isEvid = false;
            return !evidenceFile.startsWith("<");
        } else if (isScript) {
            sqlScriptFile = value;
            isScript = false;
            return !sqlScriptFile.startsWith("<");
        }
        return false;
    }

}
