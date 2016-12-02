package edu.mit.scansite.server.domains;

import java.util.ArrayList;

import edu.mit.scansite.server.dataaccess.file.ConfigReader;
import edu.mit.scansite.shared.transferobjects.DomainPosition;

/**
 * Class for locating domains.
 * 
 * @author tobieh
 */
public abstract class DomainLocator {

	protected ConfigReader reader;

	public DomainLocator(ConfigReader reader) {
		this.reader = reader;
	}

	public abstract void init();

	public abstract ArrayList<DomainPosition> getDomainPositions(String realPath, String sequence)
			throws DomainLocatorException;
}
