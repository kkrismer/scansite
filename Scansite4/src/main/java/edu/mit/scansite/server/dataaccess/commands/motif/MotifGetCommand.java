package edu.mit.scansite.server.dataaccess.commands.motif;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.IdentifierType;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGetCommand extends DbQueryCommand<Motif> {

	private AminoAcid aas[] = AminoAcid.values();
	private String shortName;
	private int motifId = -1;

	public MotifGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, String shortName) {
		super(dbAccessConfig, dbConstantsConfig);
		this.shortName = shortName;
	}

	public MotifGetCommand(Properties dbAccessConfig,
			Properties dbConstantsConfig, int motifId) {
		super(dbAccessConfig, dbConstantsConfig);
		this.motifId = motifId;
	}

	@Override
	protected Motif doProcessResults(ResultSet result)
			throws DataAccessException {
		try {
			Motif m = null;
			int motifId = -1;
			Set<Identifier> identifiers = new HashSet<>();
			Identifier currentIdentifier = null;
			while (result.next()) {
				if (motifId == -1) {
					motifId = result.getInt(c.getcMotifsId());
					m = new Motif();
					m.setId(motifId);
					m.setDisplayName(result.getString(c.getcMotifsDisplayName()));
					m.setShortName(result.getString(c.getcMotifsShortName()));
					LightWeightMotifGroup group = new LightWeightMotifGroup();
					group.setId(result.getInt(c.getcMotifGroupsId()));
					m.setGroup(group);
					m.setSubmitter(result.getString(c.getcUsersEmail()));
					m.setOptimalScore(result.getDouble(c.getcMotifsOptScore()));
					m.setPublic(result.getBoolean(c.getcMotifsIsPublic()));
					currentIdentifier = new Identifier(result.getString(c
							.getcMotifIdentifierMappingIdentifier()),
							new IdentifierType(result.getInt(c
									.getcIdentifierTypesId()), result
									.getString(c.getcIdentifierTypesName())));
					identifiers.add(currentIdentifier);
					m.getIdentifiers().add(currentIdentifier);
					m.setMotifClass(MotifClass.getDbValue(result.getString(c
							.getcMotifsMotifClass())));
				}
				currentIdentifier = new Identifier(result.getString(c
						.getcMotifIdentifierMappingIdentifier()),
						new IdentifierType(result.getInt(c
								.getcIdentifierTypesId()), result.getString(c
								.getcIdentifierTypesName())));
				if (!identifiers.contains(currentIdentifier)) {
					m.getIdentifiers().add(currentIdentifier);
					identifiers.add(currentIdentifier);
				}

				int position = result.getInt(c.getcMatrixDataPosition());
				m.setValue(AminoAcid._N, position,
						result.getDouble(c.getcMatrixDataScoreStart()));
				m.setValue(AminoAcid._C, position,
						result.getDouble(c.getcMatrixDataScoreEnd()));
				for (AminoAcid aa : aas) {
					if (!aa.equals(AminoAcid._C) && !aa.equals(AminoAcid._N)) {
						m.setValue(
								aa,
								position,
								result.getDouble(c.getcMatrixDataScorePrefix()
										+ aa.getOneLetterCode()));
					}
				}
			}
			return m;
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();

		sql.append(CommandConstants.SELECT).append(c.getcMotifsId())
				.append(CommandConstants.COMMA)
				.append(c.getcMotifsDisplayName())
				.append(CommandConstants.COMMA).append(c.getcMotifsShortName())
				.append(CommandConstants.COMMA).append(c.getcMotifGroupsId())
				.append(CommandConstants.COMMA).append(c.getcUsersEmail())
				.append(CommandConstants.COMMA).append(c.getcMotifsOptScore())
				.append(CommandConstants.COMMA).append(c.getcMotifsIsPublic())
				.append(CommandConstants.COMMA)
				.append(c.getcMotifsMotifClass())
				.append(CommandConstants.COMMA)
				.append(c.getcMatrixDataPosition())
				.append(CommandConstants.COMMA)
				.append(c.getcMatrixDataScoreEnd())
				.append(CommandConstants.COMMA)
				.append(c.getcMatrixDataScoreStart())
				.append(CommandConstants.COMMA)
				.append(c.getcMotifIdentifierMappingIdentifier())
				.append(CommandConstants.COMMA)
				.append(c.getcIdentifierTypesId())
				.append(CommandConstants.COMMA)
				.append(c.getcIdentifierTypesName());
		for (AminoAcid aa : aas) {
			if (!aa.equals(AminoAcid._C) && !aa.equals(AminoAcid._N)) {
				sql.append(CommandConstants.COMMA)
						.append(c.getcMatrixDataScorePrefix())
						.append(aa.getOneLetterCode());
			}
		}
		sql.append(CommandConstants.FROM).append(c.gettMotifs())
				.append(CommandConstants.INNERJOIN)
				.append(c.gettMotifMatrixData()).append(CommandConstants.USING)
				.append(CommandConstants.LPAR).append(c.getcMotifsId())
				.append(CommandConstants.RPAR)
				.append(CommandConstants.INNERJOIN)
				.append(c.gettMotifIdentifierMapping())
				.append(CommandConstants.USING).append(CommandConstants.LPAR)
				.append(c.getcMotifsId()).append(CommandConstants.RPAR)
				.append(CommandConstants.INNERJOIN)
				.append(c.gettIdentifierTypes()).append(CommandConstants.USING)
				.append(CommandConstants.LPAR)
				.append(c.getcIdentifierTypesId())
				.append(CommandConstants.RPAR).append(CommandConstants.WHERE);
		if (motifId == -1) {
			sql.append(c.getcMotifsShortName()).append(CommandConstants.LIKE)
					.append(CommandConstants.enquote(shortName));
		} else {
			sql.append(c.getcMotifsId()).append(CommandConstants.EQ)
					.append(motifId);
		}
		sql.append(CommandConstants.ORDERBY).append(c.getcMotifsId())
				.append(CommandConstants.COMMA)
				.append(c.getcMatrixDataPosition());

		return sql.toString();
	}
}
