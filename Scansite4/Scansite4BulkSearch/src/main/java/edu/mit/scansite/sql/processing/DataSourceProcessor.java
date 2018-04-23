package edu.mit.scansite.sql.processing;

import edu.mit.scansite.model.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static edu.mit.scansite.sql.constants.TableDataSources.*;

public class DataSourceProcessor {
    private Logger logger = LoggerFactory.getLogger(DataSourceProcessor.class);

    public List<DataSource> process(PreparedStatement stmt) {
        try {
            ResultSet rs = stmt.executeQuery();
            List<DataSource> dataSources = new ArrayList<>();

            while (rs.next()) {
                DataSource ds = new DataSource();
                if (ProcessingUtils.hasColumn(rs, COLUMN_DATA_SOURCE_ID)) {
                    ds.setDataSourceId(rs.getInt(COLUMN_DATA_SOURCE_ID));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_DATA_SOURCE_TYPE_ID)) {
                    ds.setDataSourceTypeId(rs.getInt(COLUMN_DATA_SOURCE_TYPE_ID));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_IDENTIFIER_TYPE_ID)) {
                    ds.setIdentifierTypeId(rs.getInt(COLUMN_IDENTIFIER_TYPE_ID));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_DATA_SOURCE_SHORT_NAME)) {
                    ds.setDataSourceShortName(rs.getString(COLUMN_DATA_SOURCE_SHORT_NAME));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_DATA_SOURCE_DISPLAY_NAME)) {
                    ds.setDataSourceDisplayName(rs.getString(COLUMN_DATA_SOURCE_DISPLAY_NAME));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_DATA_SOURCE_DESCRIPTION)) {
                    ds.setDataSourceDescription(rs.getString(COLUMN_DATA_SOURCE_DESCRIPTION));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_DATA_SOURCE_DESCRIPTION)) {
                    ds.setDataSourceDescription(rs.getString(COLUMN_DATA_SOURCE_DESCRIPTION));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_DATA_SOURCE_VERSION)) {
                    ds.setDataSourceVersion(rs.getString(COLUMN_DATA_SOURCE_VERSION));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_DATA_SOURCE_LAST_UPDATE)) {
                    ds.setDataSourceLastUpdate(rs.getString(COLUMN_DATA_SOURCE_LAST_UPDATE));
                }
                if (ProcessingUtils.hasColumn(rs, COLUMN_DATA_SOURCE_IS_PRIMARY)) {
                    ds.setDataSourceIsPrimary(rs.getBoolean(COLUMN_DATA_SOURCE_IS_PRIMARY));
                }

                dataSources.add(ds);
            }

            return dataSources;
        } catch (SQLException e) {
            logger.error("Could not retrieve DataSource values from database!", e);
        }
        return null;
    }

}
