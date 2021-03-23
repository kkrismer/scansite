package edu.mit.scansite.server.updater;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DropTableCommand;
import edu.mit.scansite.server.dataaccess.commands.RenameTableCommand;
import edu.mit.scansite.server.dataaccess.commands.localization.CreateLocalizationGOTermsTableCommand;
import edu.mit.scansite.server.dataaccess.commands.localization.CreateLocalizationTableCommand;
import edu.mit.scansite.server.updater.transliterator.FileTransliterator;
import edu.mit.scansite.server.updater.transliterator.LocalizationTransliteratorDbWriter;
import edu.mit.scansite.server.updater.transliterator.ScansiteLocalizationFileTransliterator;
import edu.mit.scansite.shared.DatabaseException;

/**
 * @author Konstantin Krismer
 */
public abstract class LocalizationDbUpdater extends DbUpdater {

	@Override
	protected FileTransliterator getScansiteFileTransliterator(
			String errorFilePath) throws ScansiteUpdaterException {
		BufferedWriter errorWriter;
		try {
			errorWriter = new BufferedWriter(new FileWriter(new File(
					errorFilePath)));
			LocalizationTransliteratorDbWriter writer = new LocalizationTransliteratorDbWriter(
					errorWriter, dataSourceMetaInfo);
			ScansiteLocalizationFileTransliterator transliterator = new ScansiteLocalizationFileTransliterator(
					getReaders(), writer);
			return transliterator;
		} catch (IOException e) {
			throw new ScansiteUpdaterException(
					"Error accessing temporary files", e);
		}
	}

	@Override
	protected void createTables() throws ScansiteUpdaterException {
		try {
			CreateLocalizationTableCommand cmd = new CreateLocalizationTableCommand(
					ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(),
					dataSourceMetaInfo.getDataSource());
			cmd.execute();
		} catch (Exception e) {
			logger.error("Cannot create localization table: " + e.getMessage(),
					e);
			throw new ScansiteUpdaterException(
					"Cannot create localization table", e);
		}

		try {
			CreateLocalizationGOTermsTableCommand cmd = new CreateLocalizationGOTermsTableCommand(
					ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(),
					dataSourceMetaInfo.getDataSource());
			cmd.execute();
		} catch (Exception e) {
			logger.error(
					"Cannot create localization GO term table: "
							+ e.getMessage(), e);
			throw new ScansiteUpdaterException(
					"Cannot create localization GO term table", e);
		}
	}

	@Override
	protected void renameTables() throws ScansiteUpdaterException {
		try {
			CommandConstants cmdConst = CommandConstants.instance(ServiceLocator.getDbConstantsProperties());
			RenameTableCommand cmd = new RenameTableCommand(ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(), cmdConst
					.getLocalizationTableName(dataSourceMetaInfo.getDataSource()),
					CommandConstants.getOldTable(cmdConst.getLocalizationTableName(
							dataSourceMetaInfo.getDataSource())));

			// renaming existing localization table to old localization table
			try {
				cmd.execute();
			} catch (Exception e) {
				logger.info("there is no old localization table of data source "
						+ dataSourceMetaInfo.getDataSource().getShortName());
			}

			// renaming existing localization GO term table to old localization
			// GO term table
			cmd.setFromTableName(cmdConst
					.getLocalizationGOTermsTableName(dataSourceMetaInfo
							.getDataSource()));
			cmd.setToTableName(CommandConstants.getOldTable(cmdConst
					.getLocalizationGOTermsTableName(dataSourceMetaInfo
							.getDataSource())));
			try {
				cmd.execute();
			} catch (Exception e) {
				logger.info("there is no old localization GO term table of data source "
						+ dataSourceMetaInfo.getDataSource().getShortName());
			}

			// renaming temp localization table to localization table
			cmd.setFromTableName(CommandConstants.getTempTable(cmdConst
					.getLocalizationTableName(dataSourceMetaInfo
							.getDataSource())));
			cmd.setToTableName(cmdConst
					.getLocalizationTableName(dataSourceMetaInfo
							.getDataSource()));
			cmd.execute();

			// renaming temp localization GO term table to localization table
			cmd.setFromTableName(CommandConstants.getTempTable(cmdConst
					.getLocalizationGOTermsTableName(dataSourceMetaInfo
							.getDataSource())));
			cmd.setToTableName(cmdConst
					.getLocalizationGOTermsTableName(dataSourceMetaInfo
							.getDataSource()));
			cmd.execute();
		} catch (DatabaseException e) {
			logger.error(
					"renaming temp localization tables to standard localization tables failed ("
							+ dataSourceMetaInfo.getDataSource().getShortName()
							+ "): " + e.getMessage(), e);
		}
	}

	@Override
	protected void dropOldTables() throws ScansiteUpdaterException {
		try {
			DropTableCommand cmd = new DropTableCommand(ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(), null);
			CommandConstants cmdConst = CommandConstants.instance(ServiceLocator.getDbConstantsProperties());
			// order is important: first drop go terms m:n table, then
						// localization main table
			cmd.setTableName(CommandConstants.getOldTable(cmdConst
					.getLocalizationGOTermsTableName(dataSourceMetaInfo
							.getDataSource())));
			cmd.execute();

			cmd.setTableName(CommandConstants.getOldTable(cmdConst
					.getLocalizationTableName(dataSourceMetaInfo
							.getDataSource())));
			cmd.execute();
		} catch (DatabaseException e) {
			logger.info("dropping old localization tables failed, there are no old tables of "
					+ dataSourceMetaInfo.getDataSource().getShortName());
		}
	}

	@Override
	protected void dropTempTables() throws ScansiteUpdaterException {
		try {
			DropTableCommand cmd = new DropTableCommand(ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(), null);
			CommandConstants cmdConst = CommandConstants.instance(ServiceLocator.getDbConstantsProperties());
			// order is important: first drop go terms m:n table, then
			// localization main table
			cmd.setTableName(CommandConstants.getTempTable(cmdConst
					.getLocalizationGOTermsTableName(dataSourceMetaInfo
							.getDataSource())));
			cmd.execute();

			cmd.setTableName(CommandConstants.getTempTable(cmdConst
					.getLocalizationTableName(dataSourceMetaInfo
							.getDataSource())));
			cmd.execute();
		} catch (DatabaseException e) {
			logger.error(
					"dropping temp localization tables failed of data source "
							+ dataSourceMetaInfo.getDataSource().getShortName()
							+ ": " + e.getMessage(), e);
		}
	}

	@Override
	protected void updateHistogramData() throws ScansiteUpdaterException {
		// nothing to do here
	}
}