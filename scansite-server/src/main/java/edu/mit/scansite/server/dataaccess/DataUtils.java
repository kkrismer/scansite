package edu.mit.scansite.server.dataaccess;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.shared.transferobjects.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Bernwinkler
 */
public class DataUtils {
    private static final Logger logger = LoggerFactory.getLogger(DataUtils.class);

    public static String getTableName(DataSource dataSource, CommandConstants c) {
        if (dataSource.getType().getShortName().equals("proteins")) {
            return c.getProteinsTableName(dataSource);
        } else if (dataSource.getType().getShortName().equals("orthologs")) {
            return c.getOrthologsTableName(dataSource);
        } else if (dataSource.getType().getShortName().equals("localization")) {
            return c.getLocalizationTableName(dataSource);
        } else {
            logger.warn("Unknown data source type: " + dataSource.getType().getShortName() + " of data source "
                    + dataSource.getShortName());
            return dataSource.getShortName();
        }
    }
}
