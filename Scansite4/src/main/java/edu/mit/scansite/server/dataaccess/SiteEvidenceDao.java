package edu.mit.scansite.server.dataaccess;

import java.util.List;
import java.util.Set;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.EvidenceResource;

/**
 * @author Konstantin Krismer
 */
public interface SiteEvidenceDao extends Dao {

	public abstract List<EvidenceResource> getSiteEvidence(
			Set<String> accessions, String site) throws DataAccessException;

	public abstract void addSite(String site, String accession,
			String resource) throws DataAccessException;

	public abstract void addResource(String resource, String resourceLink) throws DataAccessException;

}