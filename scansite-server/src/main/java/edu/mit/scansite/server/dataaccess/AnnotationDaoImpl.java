package edu.mit.scansite.server.dataaccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import edu.mit.scansite.server.dataaccess.commands.annotation.AnnoTypeAddCommand;
import edu.mit.scansite.server.dataaccess.commands.annotation.AnnoTypeGetIdCommand;
import edu.mit.scansite.server.dataaccess.commands.annotation.AnnotationAddCommand;
import edu.mit.scansite.server.dataaccess.commands.annotation.AnnotationGetCommand;
import edu.mit.scansite.server.dataaccess.commands.annotation.AnnotationGetForAllProteinsCommand;
import edu.mit.scansite.server.dataaccess.commands.annotation.AnnotationGetProteinAccCommand;
import edu.mit.scansite.server.dataaccess.commands.annotation.AnnotationTypeGetAllCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Protein;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class AnnotationDaoImpl extends DaoImpl implements AnnotationDao {

	private Map<String, Integer> annotationTypes = new HashMap<String, Integer>();
	private Map<Integer, String> annotationTypesRev = null;

	public AnnotationDaoImpl(Properties dbAccessConfig, Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.AnnotationDao#setUseTempTablesForUpdate(
	 * boolean)
	 */
	@Override
	public void setUseTempTablesForUpdate(boolean useTempTablesForUpdate) {
		this.useTempTablesForUpdate = useTempTablesForUpdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.AnnotationDao#addAnnotationType(java.lang.
	 * String)
	 */
	@Override
	public int addAnnotationType(String name) throws DataAccessException {
		int id = getAnnotationTypeId(name);
		if (id == -1) {
			AnnoTypeAddCommand cmd = new AnnoTypeAddCommand(dbAccessConfig, dbConstantsConfig, name);
			try {
				id = cmd.execute();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new DataAccessException(e.getMessage(), e);
			}
		}
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.AnnotationDao#getAnnotationTypeId(java.
	 * lang.String)
	 */
	@Override
	public int getAnnotationTypeId(String name) throws DataAccessException {
		AnnoTypeGetIdCommand cmd = new AnnoTypeGetIdCommand(dbAccessConfig, dbConstantsConfig, name);
		Integer id = -1;
		try {
			id = cmd.execute();
			id = (id == null) ? -1 : id;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.AnnotationDao#addAnnotations(java.util.
	 * Map, java.lang.String, edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public void addAnnotations(Map<String, Set<String>> annotations, String proteinId, DataSource dataSource)
			throws DataAccessException {
		for (String key : annotations.keySet()) {
			for (String value : annotations.get(key)) {
				addAnnotation(key, value, proteinId, dataSource);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.AnnotationDao#addAnnotation(java.lang.
	 * String, java.lang.String, java.lang.String,
	 * edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public void addAnnotation(String type, String annotation, String proteinId, DataSource dataSource)
			throws DataAccessException {
		Integer typeId = annotationTypes.get(type);
		if (typeId == null) { // check if in map
			typeId = getAnnotationTypeId(type); // check if in database
			if (typeId == -1) { // add to database
				typeId = addAnnotationType(type);
				annotationTypes.put(type, typeId);
			}
		}
		if (typeId != null && typeId >= 0) {
			AnnotationAddCommand cmd = new AnnotationAddCommand(dbAccessConfig, dbConstantsConfig, typeId, annotation,
					proteinId, useTempTablesForUpdate, dataSource);
			try {
				cmd.execute();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new DataAccessException(e.getMessage(), e);
			}
		} else {
			throw new DataAccessException(
					"Annotation --" + type + "-" + annotation + "-- can not be added to database :(");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.AnnotationDao#get(java.lang.String,
	 * edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public Map<String, Set<String>> get(String proteinId, DataSource dataSource) throws DataAccessException {
		AnnotationGetCommand cmd = new AnnotationGetCommand(dbAccessConfig, dbConstantsConfig, proteinId,
				useTempTablesForUpdate, dataSource);
		Map<String, Set<String>> as = null;
		try {
			as = cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return as;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.AnnotationDao#get(java.lang.String,
	 * edu.mit.scansite.shared.transferobjects.DataSource, java.lang.String)
	 */
	@Override
	public Map<String, Set<String>> get(String proteinId, DataSource dataSource, String regex)
			throws DataAccessException {
		if (regex == null) {
			return get(proteinId, dataSource);
		}
		AnnotationGetCommand cmd = new AnnotationGetCommand(dbAccessConfig, dbConstantsConfig, proteinId,
				useTempTablesForUpdate, dataSource, regex);
		HashMap<String, Set<String>> as = null;
		try {
			as = cmd.execute();
			if (as == null || as.isEmpty()) {
				return null;
			} else {
				return get(proteinId, dataSource);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.AnnotationDao#getForAllProteins(java.util.
	 * List, edu.mit.scansite.shared.transferobjects.DataSource, java.lang.String)
	 */
	@Override
	public List<Protein> getForAllProteins(List<Protein> proteins, DataSource dataSource, String regex)
			throws DataAccessException {
		try {
			if (annotationTypesRev == null) {
				annotationTypesRev = getAnnotationTypes();
			}
			AnnotationGetForAllProteinsCommand cmd = new AnnotationGetForAllProteinsCommand(dbAccessConfig,
					dbConstantsConfig, proteins, annotationTypesRev, useTempTablesForUpdate, dataSource, regex);
			return cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	private Map<Integer, String> getAnnotationTypes() throws DataAccessException {
		AnnotationTypeGetAllCommand cmd = new AnnotationTypeGetAllCommand(dbAccessConfig, dbConstantsConfig);
		try {
			return cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.AnnotationDao#getProteinAccessionNr(java.
	 * lang.String, edu.mit.scansite.shared.transferobjects.DataSource)
	 */
	@Override
	public String getProteinAccessionNr(String accessionAnnotation, DataSource dataSource) throws DataAccessException {
		AnnotationGetProteinAccCommand cmd = new AnnotationGetProteinAccCommand(dbAccessConfig, dbConstantsConfig,
				accessionAnnotation, useTempTablesForUpdate, dataSource);
		String proteinId = null;
		try {
			proteinId = cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
		return proteinId;
	}
}
