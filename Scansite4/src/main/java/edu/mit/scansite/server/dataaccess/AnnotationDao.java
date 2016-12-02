package edu.mit.scansite.server.dataaccess;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Protein;

/**
 * @author Konstantin Krismer
 */
public interface AnnotationDao extends Dao {

	public abstract void setUseTempTablesForUpdate(
			boolean useTempTablesForUpdate);

	public abstract int addAnnotationType(String name)
			throws DataAccessException;

	public abstract int getAnnotationTypeId(String name)
			throws DataAccessException;

	public abstract void addAnnotations(Map<String, Set<String>> annotations,
			String proteinId, DataSource dataSource) throws DataAccessException;

	public abstract void addAnnotation(String type, String annotation,
			String proteinId, DataSource dataSource) throws DataAccessException;

	public abstract Map<String, Set<String>> get(String proteinId,
			DataSource dataSource) throws DataAccessException;

	public abstract Map<String, Set<String>> get(String proteinId,
			DataSource dataSource, String regex) throws DataAccessException;

	public abstract List<Protein> getForAllProteins(List<Protein> proteins,
			DataSource dataSource, String regex) throws DataAccessException;

	public abstract String getProteinAccessionNr(String accessionAnnotation,
			DataSource dataSource) throws DataAccessException;

}