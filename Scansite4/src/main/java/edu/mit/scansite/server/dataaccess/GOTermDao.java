package edu.mit.scansite.server.dataaccess;

import java.util.List;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.EvidenceCode;
import edu.mit.scansite.shared.transferobjects.GOTerm;

/**
 * @author Konstantin Krismer
 */
public interface GOTermDao extends Dao {

	public abstract EvidenceCode addEvidenceCode(EvidenceCode evidenceCode)
			throws DataAccessException;

	public abstract EvidenceCode getEvidenceCode(String code)
			throws DataAccessException;

	public abstract List<EvidenceCode> getAllEvidenceCodes()
			throws DataAccessException;

	public abstract GOTerm addGOTerm(GOTerm goTerm) throws DataAccessException;

	public abstract GOTerm getGOTerm(String id) throws DataAccessException;

	public abstract List<GOTerm> getAllGOTerms() throws DataAccessException;

}