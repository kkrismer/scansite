package edu.mit.scansite.sql.statements;

import edu.mit.scansite.sql.constants.AvailableStatements;
import edu.mit.scansite.sql.constants.TableDataSources;
import edu.mit.scansite.sql.constants.TableMotifGroups;
import edu.mit.scansite.sql.constants.TableMotifs;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static edu.mit.scansite.sql.constants.GeneralConstants.*;

public class PreparedStatements {
    private Logger logger = LoggerFactory.getLogger(PreparedStatement.class);
    private static final String talbeNamePlaceHolder = "<table-name>";

    // STATEMENT DEFINITIONS
    private static final String getDataSourceDisplayNamesStatement = String.format("%s %s %s %s %s %s = ?",
            SELECT, TableDataSources.COLUMN_DATA_SOURCE_DISPLAY_NAME,
            FROM, TableDataSources.TABLE_NAME,
            WHERE, TableDataSources.COLUMN_DATA_SOURCE_TYPE_ID);


    private static final String getDataSourceNamesStatement = String.format("%s %s, %s %s %s %s %s = ?",
            SELECT, TableDataSources.COLUMN_DATA_SOURCE_SHORT_NAME, TableDataSources.COLUMN_DATA_SOURCE_DISPLAY_NAME,
            FROM, TableDataSources.TABLE_NAME,
            WHERE, TableDataSources.COLUMN_DATA_SOURCE_TYPE_ID);

    private static final String getMotifDisplayNamesStatement = String.format("%s %s %s %s",
            SELECT, TableMotifs.COLUMN_MOTIF_DISPLAY_NAME,
            FROM, TableMotifs.TABLE_NAME);


    private static final String getMotifGroupDisplayNamesStatement = String.format("%s %s %s %s",
            SELECT, TableMotifGroups.COLUMN_MOTIF_GROUP_DISPLAY_NAME,
            FROM, TableMotifGroups.TABLE_NAME);


    private static final String getMatchingIdentifiersStatement = String.format("%s %s %s %s %s %s %s ?", SELECT,
        PROTEIN_IDENTIFIER,
            FROM, talbeNamePlaceHolder,
            WHERE, PROTEIN_IDENTIFIER, LIKE);






    // RETRIEVE STATEMENT TO EXECUTE
    public PreparedStatement getStatement(Connection connection, AvailableStatements statement, List<List<String>> parameters) {
        String selectedStatement = null;
        switch (statement) {
            case DATA_SOURCES_DISPLAY_NAME_STATEMENT:
                selectedStatement = getDataSourceDisplayNamesStatement;
                break;
            case MOTIF_DISPLAY_NAMES_STATEMENT:
                selectedStatement = getMotifDisplayNamesStatement;
                break;
            case MOTIF_GROUP_DISPLAY_NAMES_STATEMENT:
                selectedStatement = getMotifGroupDisplayNamesStatement;
                break;
            case PROTEIN_IDENTIFIER_STATEMENT_ENSEMBL_HUMAN:
                selectedStatement = getMatchingIdentifiersStatement.replace(talbeNamePlaceHolder, TABLE_PROTEINS_ENSEMBL_HUMAN);
                break;
            case PROTEIN_IDENTIFIER_STATEMENT_ENSEMBL_MOUSE:
                selectedStatement = getMatchingIdentifiersStatement.replace(talbeNamePlaceHolder, TABLE_PROTEINS_ENSEMBL_MOUSE);
                break;
            case PROTEIN_IDENTIFIER_STATEMENT_SWISSPROT:
                selectedStatement = getMatchingIdentifiersStatement.replace(talbeNamePlaceHolder, TABLE_PROTEINS_SWISSPROT);
                break;
            case PROTEIN_IDENTIFIER_STATEMENT_YEAST:
                selectedStatement = getMatchingIdentifiersStatement.replace(talbeNamePlaceHolder, TABLE_PROTEINS_YEAST);
                break;
            case DATA_SOURCE_NAMES_STATEMENT:
                selectedStatement = getDataSourceNamesStatement;
                break;
        }

        if (!StringUtils.isBlank(selectedStatement)) {
            try {
                PreparedStatement stmt = connection.prepareStatement(selectedStatement);
                if (parameters != null) {
                    for (int i = 0; i < parameters.size(); i++) {
                        // TYPE - VALUE
                        if (parameters.get(i).size() == 2) {
                            stmt = setParameter(stmt, i+1, parameters.get(i));
                        }
                    }
                }

                return stmt;
            } catch (SQLException e) {
                logger.error("Could not create prepared statement!", e);
            }
        }

        return null;
    }


    private PreparedStatement setParameter(PreparedStatement stmt, int position, List<String> data) throws SQLException {
        final String type = data.get(0);
        final String value = data.get(1);
        if (type.equalsIgnoreCase("String")) {
            stmt.setString(position, value);
        } else if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("integer")) {
            stmt.setInt(position, Integer.parseInt(value));
        } else if (type.equalsIgnoreCase("Double")) {
            stmt.setDouble(position, Double.parseDouble(value));
        } else if (type.equalsIgnoreCase("Bool") || type.equalsIgnoreCase("Boolean")) {
            stmt.setBoolean(position, Boolean.parseBoolean(value));
        }
        return stmt;
    }
}
