package edu.mit.scansite.server.dataaccess.commands.localizationtype;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LocalizationType;

/**
 * @author Konstantin Krismer
 */
public class LocalizationTypesGetAllCommand extends DbQueryCommand<List<LocalizationType>> {
	private DataSource localizationDataSource;

	public LocalizationTypesGetAllCommand(Properties dbAccessConfig, Properties dbConstantsConfig,
			DataSource localizationDataSource) {
		super(dbAccessConfig, dbConstantsConfig);
		this.localizationDataSource = localizationDataSource;
	}

	@Override
	protected List<LocalizationType> doProcessResults(ResultSet result) throws DataAccessException {
		List<LocalizationType> localizationTypes = null;

		try {
			while (result.next()) {
				if (localizationTypes == null) {
					localizationTypes = new LinkedList<>();
				}
				localizationTypes.add(new LocalizationType(result.getInt(c.getcLocalizationTypesId()),
						result.getString(c.getcLocalizationTypesName())));
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return localizationTypes;
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append('*').append(CommandConstants.FROM).append(c.gettLocalizationTypes())
				.append(CommandConstants.WHERE).append(c.getcDataSourcesId()).append(CommandConstants.EQ)
				.append(localizationDataSource.getId());

		return sql.toString();
	}
}
