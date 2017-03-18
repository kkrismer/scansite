package edu.mit.scansite.server.dataaccess.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.SiteEvidenceDao;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.transferobjects.EvidenceResource;

/**
 * Runnable class for filling the siteEvidence-database table with evidence-data
 * of experimentally verified phosphorylation sites.
 * 
 * @author tobieh
 * @author Konstantin Krismer
 */
public class RunEvidenceInserter {
	private static final Logger logger = LoggerFactory
			.getLogger(RunEvidenceInserter.class);

	private static final int ACC_INDEX = 0;
	private static final int SITE_INDEX = 1;
	private static final int RESOURCE_NAME_INDEX = 2;
	private static final int RESOURCE_LINK_INDEX = 3;

	private static final String USAGE_TEXT = "USAGE:\n"
			+ "  Please give the relative path to a file that contains information about\n"
			+ "  experimentally verified phosphorylation files as a parameter to run the \n"
			+ "  program. Each line in the file has to have the following format:\n"
			+ "PROTEIN_ACCESSION[TAB]SITE[TAB]RESOURCE[TAB]GENERIC_LINK\n\n"
			+ "  The GENERIC_LINK has to contain two entries "
			+ EvidenceResource.URI_REPLACEMENT_ACCESSION
			+ " and "
			+ EvidenceResource.URI_REPLACEMENT_SITE
			+ " that will be replaced\n"
			+ "  by the program with the protein's site and accession, respectively.\n"
			+ "  Example line:\n"
			+ "RB_RAT\\tT245\\tPhosphosite\\thttp://www.phosphosite.org/uniprotAccSiteAction.do?id="
			+ EvidenceResource.URI_REPLACEMENT_ACCESSION
			+ "&site="
			+ EvidenceResource.URI_REPLACEMENT_SITE
			+ "\n"
			+ "  \n"
			+ "  Also, please make sure that a cfg/ directory with the Scansite3-relevant\n"
			+ "  configuration files is in your current working directory.\n\n"
			+ "  Example usage:\n"
			+ "  java -Djava.awt.headless=true -jar thisProgram.jar evidenceData.tsv\n"
			+ "\n";

	/**
	 * @param args java application terminal / console arguments
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println(USAGE_TEXT);
			return;
		}
		String filePath = args[0];
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			System.out
					.println("The file '"
							+ filePath
							+ "' was not found. Please try again with a different file!");
			return;
		}
		int count = 0;
		SiteEvidenceDao evDao;
		int errors = 0;
		
		try {
			DaoFactory factory = ServiceLocator.getDaoFactory();
			logger.info("disables auto-commit, unique and foreign key checks");
			factory.getDataSourceDao().disableChecks();
			evDao = factory.getSiteEvidenceDao();

			String line;
			int lineNr = 0;
			line = reader.readLine();
			Set<String> resourceNames = new HashSet<>();
			Set<String> sites = new HashSet<>();
			while (line != null) {
				++lineNr;
				String[] lineArr = line.split("\t");
				if (lineArr.length == 4) {
					try {
						String acc = lineArr[ACC_INDEX];
						String site = lineArr[SITE_INDEX];
						String resource = lineArr[RESOURCE_NAME_INDEX];
						String link = lineArr[RESOURCE_LINK_INDEX];
						if (acc != null && !acc.isEmpty() && site != null
								&& !site.isEmpty() && resource != null
								&& !resource.isEmpty()) {
							if(!resourceNames.contains(resource)) {
								evDao.addResource(resource, link);
								resourceNames.add(resource);
							}
							if(!sites.contains(site + acc + resource)) {
								evDao.addSite(site, acc, resource);
								sites.add(site + acc + resource);
							}
							++count;
						} else {
							throw new ScansiteFileFormatException(
									"Error in line " + lineNr);
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						++errors;
						System.err.println("Error in line #" + lineNr + ": "
								+ line);
					}
				} else {
					++errors;
					System.err
							.println("Error in line #" + lineNr + ": " + line);
				}
				line = reader.readLine();
			}

			logger.info("enabling auto-commit, unique and foreign key checks");
			factory.getDataSourceDao().enableChecks();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			System.out.println("Error reading line. Aborting... :( ");
			if (count > 0) {
				System.out
						.println("Some entries have already been added to the database, but NOT ALL!");
			}
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			System.out
					.println("Error - can not access configuration files for database access!\n They must be in relative dir 'cfg/''");
			return;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

		logger.info("Done!\n" + count
				+ " entries have been added to the database.\n" + errors
				+ " lines failed and have not been saved.\n");
	}
}
