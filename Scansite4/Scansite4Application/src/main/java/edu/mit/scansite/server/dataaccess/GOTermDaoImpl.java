package edu.mit.scansite.server.dataaccess;

import java.util.List;
import java.util.Properties;

import edu.mit.scansite.server.dataaccess.commands.evidencecode.EvidenceCodeAddCommand;
import edu.mit.scansite.server.dataaccess.commands.evidencecode.EvidenceCodeGetCommand;
import edu.mit.scansite.server.dataaccess.commands.evidencecode.EvidenceCodesGetAllCommand;
import edu.mit.scansite.server.dataaccess.commands.goterm.GOTermAddCommand;
import edu.mit.scansite.server.dataaccess.commands.goterm.GOTermGetCommand;
import edu.mit.scansite.server.dataaccess.commands.goterm.GOTermsGetAllCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.EvidenceCode;
import edu.mit.scansite.shared.transferobjects.GOTerm;

/**
 * @author Konstantin Krismer
 */
public class GOTermDaoImpl extends DaoImpl implements GOTermDao {

	public GOTermDaoImpl(Properties dbAccessConfig, Properties dbConstantsConfig) {
		super(dbAccessConfig, dbConstantsConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mit.scansite.server.dataaccess.GOTermDao#addEvidenceCode(edu.mit.scansite
	 * .shared.transferobjects.EvidenceCode)
	 */
	@Override
	public EvidenceCode addEvidenceCode(EvidenceCode evidenceCode) throws DataAccessException {
		try {
			EvidenceCode temp = null;
			if (evidenceCode.getId() <= 0) {
				temp = getEvidenceCode(evidenceCode.getCode());
			}
			if (temp == null) {
				EvidenceCodeAddCommand cmd = new EvidenceCodeAddCommand(dbAccessConfig, dbConstantsConfig,
						evidenceCode);
				evidenceCode.setId(cmd.execute());
				return evidenceCode;
			} else {
				// otherwise evidence code already in db
				return temp;
			}
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.GOTermDao#getEvidenceCode(java.lang.
	 * String)
	 */
	@Override
	public EvidenceCode getEvidenceCode(String code) throws DataAccessException {
		EvidenceCodeGetCommand cmd = new EvidenceCodeGetCommand(dbAccessConfig, dbConstantsConfig, code);
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
	 * @see edu.mit.scansite.server.dataaccess.GOTermDao#getAllEvidenceCodes()
	 */
	@Override
	public List<EvidenceCode> getAllEvidenceCodes() throws DataAccessException {
		EvidenceCodesGetAllCommand cmd = new EvidenceCodesGetAllCommand(dbAccessConfig, dbConstantsConfig);
		try {
			return cmd.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.GOTermDao#addGOTerm(edu.mit.scansite.
	 * shared.transferobjects.GOTerm)
	 */
	@Override
	public GOTerm addGOTerm(GOTerm goTerm) throws DataAccessException {
		try {
			GOTerm temp = getGOTerm(goTerm.getId());
			if (temp == null) {
				GOTermAddCommand cmd = new GOTermAddCommand(dbAccessConfig, dbConstantsConfig, goTerm);
				cmd.execute();
				return goTerm;
			} else {
				// otherwise evidence code already in db
				return temp;
			}
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.GOTermDao#getGOTerm(java.lang.String)
	 */
	@Override
	public GOTerm getGOTerm(String id) throws DataAccessException {
		GOTermGetCommand cmd = new GOTermGetCommand(dbAccessConfig, dbConstantsConfig, id);
		try {
			return cmd.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mit.scansite.server.dataaccess.GOTermDao#getAllGOTerms()
	 */
	@Override
	public List<GOTerm> getAllGOTerms() throws DataAccessException {
		GOTermsGetAllCommand cmd = new GOTermsGetAllCommand(dbAccessConfig, dbConstantsConfig);
		try {
			return cmd.execute();
		} catch (DatabaseException e) {
			logger.error(e.getMessage(), e);
			throw new DataAccessException(e.getMessage(), e);
		}
	}
}
