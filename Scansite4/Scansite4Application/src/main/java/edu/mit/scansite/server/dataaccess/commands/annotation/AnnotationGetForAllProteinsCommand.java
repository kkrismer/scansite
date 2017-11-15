package edu.mit.scansite.server.dataaccess.commands.annotation;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.commands.DbQueryCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Protein;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class AnnotationGetForAllProteinsCommand extends DbQueryCommand<List<Protein>> {

	private List<Protein> proteins;
	private Map<Integer, String> annotationTypes; // id->value
	private Map<String, Protein> protAccToProtein = new HashMap<String, Protein>(); // id->value
	private DataSource dataSource;
	private String regex = null;

	public AnnotationGetForAllProteinsCommand(Properties dbAccessConfig, Properties dbConstantsConfig,
			List<Protein> proteins, Map<Integer, String> annotationTypes, boolean useTempTablesForUpdate,
			DataSource dataSource, String regex) {
		super(dbAccessConfig, dbConstantsConfig);
		setUseOfTempTables(useTempTablesForUpdate);
		this.annotationTypes = annotationTypes;
		this.proteins = proteins;
		this.dataSource = dataSource;
		this.regex = regex;
	}

	@Override
	protected List<Protein> doProcessResults(ResultSet result) throws DataAccessException {
		Set<Protein> ps = new HashSet<Protein>();
		try {
			while (result.next()) {
				String protAcc = result.getString(c.getcProteinsIdentifier());
				String annotation = result.getString(c.getcAnnotationsAnnotation());
				Integer typeId = result.getInt(c.getcAnnotationTypesId());
				Protein p = protAccToProtein.get(protAcc);
				if (p != null) {
					p.addAnnotation(annotationTypes.get(typeId), annotation);
					ps.add(p);
				}
			}
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return new LinkedList<Protein>(ps);
	}

	@Override
	protected String doGetSqlStatement() throws DataAccessException {
		StringBuilder sql = new StringBuilder();
		sql.append(CommandConstants.SELECT).append(c.getcProteinsIdentifier()).append(CommandConstants.COMMA)
				.append(c.getcAnnotationTypesId()).append(CommandConstants.COMMA).append(c.getcAnnotationsAnnotation())
				.append(CommandConstants.FROM).append(c.gettAnnotations(dataSource));
		String conj = CommandConstants.WHERE;
		boolean first = true;
		if (proteins != null && !proteins.isEmpty()) {
			sql.append(conj);
			sql.append(" ( ");
			for (Protein p : proteins) {
				if (!first) {
					sql.append(conj);
				}
				sql.append(c.getcProteinsIdentifier()).append(CommandConstants.EQ)
						.append(CommandConstants.enquote(p.getIdentifier()));
				protAccToProtein.put(p.getIdentifier(), p);
				if (first) {
					first = false;
					conj = CommandConstants.OR;
				}
			}
			sql.append(" ) ");
			conj = CommandConstants.AND;
		}
		if (regex != null && !regex.isEmpty()) {
			sql.append(conj).append(c.getcAnnotationsAnnotation()).append(CommandConstants.REGEXP)
					.append(CommandConstants.enquote(regex));
		}
		return sql.toString();
	}

}
