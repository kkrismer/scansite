package edu.mit.scansite.server.updater.transliterator;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.AnnotationDao;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.ProteinDao;
import edu.mit.scansite.server.dataaccess.TaxonDao;
import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.server.updater.DataSourceMetaInfo;
import edu.mit.scansite.server.updater.ScansiteUpdaterException;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.OrganismClass;
import edu.mit.scansite.shared.transferobjects.Protein;
import edu.mit.scansite.shared.transferobjects.Taxon;
import edu.mit.scansite.shared.util.ScansiteAlgorithms;

/**
 * A TransliteratorWriter that is used to transliterate a given source to a
 * database.
 * 
 * @author tobieh
 */
public class ProteinTransliteratorDbWriter implements ProteinTransliteratorWriter {

	private BufferedWriter errorWriter;
	private ProteinDao proteinDao;
	private TaxonDao taxonDao;
	private AnnotationDao annoDao;
	private DataSourceMetaInfo dataSourceMetaInfo;
	private final static String PARENT_TAXON_SEPARATOR = CommandConstants.PARENT_TAXON_SEPARATOR;

	private ScansiteAlgorithms algs = new ScansiteAlgorithms();

	/**
	 * Map that is used to not query the database from within the proteinDao to
	 * often. Maps taxon names to parentTaxonLists.
	 */
	private HashMap<String, String> parentsMap = new HashMap<String, String>();

	/**
	 * Map that is used to not query the database from within the proteinDao to
	 * often. Maps taxon names to taxon ids (from db).
	 */
	private HashMap<String, Integer> taxaIds = new HashMap<String, Integer>();

	public ProteinTransliteratorDbWriter(BufferedWriter errorWriter,
			DataSourceMetaInfo dataSourceMetaInfo)
			throws ScansiteUpdaterException {
		this.errorWriter = errorWriter;
		this.dataSourceMetaInfo = dataSourceMetaInfo;
		try {
			DaoFactory factory = ServiceLocator.getDaoFactory();
			proteinDao = factory.getProteinDao();
			taxonDao = factory.getTaxonDao();
			annoDao = factory.getAnnotationDao();
			proteinDao.setUseTempTablesForUpdate(true);
			taxonDao.setUseTempTablesForUpdate(true);
			annoDao.setUseTempTablesForUpdate(true);
		} catch (Exception e) {
			throw new ScansiteUpdaterException(e.getMessage(), e);
		}
	}

	@Override
	public void writeEntrySpacer() throws ScansiteUpdaterException {
	}

	@Override
	public void saveInvalidEntry(String proteinId)
			throws ScansiteUpdaterException {
		try {
			errorWriter.write(proteinId + "\n");
			errorWriter.flush();
		} catch (Exception e) {
			throw new ScansiteUpdaterException(
					"Error writing invalid entry to file.", e);
		}
	}

	@Override
	public void saveEntry(String proteinId,
			HashMap<String, Set<String>> annotations, Double mw, Double pI,
			ArrayList<String> taxa, String species, String sequence)
			throws ScansiteUpdaterException {
		try {
			Taxon tx = createTaxon(taxa, species);
			Protein p = new Protein(proteinId,
					dataSourceMetaInfo.getDataSource(), sequence, tx, mw, pI);
			p.setOrganismClass(getClass(taxa));
			p.setpIPhos1(algs.calculateIsoelectricPoint(sequence, 1));
			p.setpIPhos2(algs.calculateIsoelectricPoint(sequence, 2));
			p.setpIPhos3(algs.calculateIsoelectricPoint(sequence, 3));
			proteinDao.add(p, false, dataSourceMetaInfo.getDataSource());
			if (annotations != null) {
				annoDao.addAnnotations(annotations, proteinId,
						dataSourceMetaInfo.getDataSource());
			}
		} catch (Exception e) {
			saveInvalidEntry(proteinId + "\t---\t" + e.getMessage());
		}
	}

	private OrganismClass getClass(ArrayList<String> taxa) {
		OrganismClass orgClass = OrganismClass.OTHER;
		for (String t : taxa) {
			if (t.matches(".*[Vv]irus.*")) {
				return OrganismClass.VIRUSES;
			} else if (t.matches(".*[Aa]rchaea.*")
					|| t.matches(".*[Bb]acteria.*")) {
				return OrganismClass.BACTERIA;
			} else if (t.matches(".*[Ee]u[ck]aryota.*")) {
				orgClass = OrganismClass.INVERTEBRATA; // all eucaryota that are
														// not
														// vertebrates
			} else if (t.matches(".*[Vv]iridiplantae.*")) {
				return OrganismClass.PLANTS;
			} else if (t.matches(".*[fF]ungi.*") || t.matches(".*[Yy]east.*")
					|| t.matches(".*[sS]accharomyces.*")) {
				return OrganismClass.FUNGI;
			} else if (t.matches(".*[Mm]ammalia.*")
					|| t.matches(".*[Hh]uman.*")
					|| t.matches(".*[Hh]omo Sapiens.*")
					|| t.matches(".*[Mm]ouse.*")
					|| t.matches(".*[Mm]us [Mm]usculus.*")) {
				return OrganismClass.MAMMALIA;
			} else if (t.matches(".*[Vv]ertebrata.*")) {
				orgClass = OrganismClass.VERTEBRATA;
			}
		}
		return orgClass;
	}

	private Taxon createTaxon(ArrayList<String> taxaNames, String species)
			throws ScansiteUpdaterException {
		taxaNames.add(species);
		Taxon finalTaxon = null;
		String parentName = "";
		for (String taxonName : taxaNames) {
			String parentList = parentsMap.get(parentName);
			if (parentList == null) {
				parentList = PARENT_TAXON_SEPARATOR;
			}
			try {
				int id = (taxaIds.containsKey(taxonName)) ? taxaIds
						.get(taxonName) : -1;
				finalTaxon = new Taxon(id, taxonName, parentList,
						taxonName.equals(species));
				id = taxonDao.add(finalTaxon,
						dataSourceMetaInfo.getDataSource());
				taxaIds.put(taxonName, id);
				parentsMap.put(taxonName, parentList + id
						+ PARENT_TAXON_SEPARATOR);
				parentName = taxonName;
				finalTaxon.setId(id);
			} catch (DataAccessException e) {
				throw new ScansiteUpdaterException(e.getMessage(), e);
			}
		}
		return finalTaxon;
	}

	@Override
	public void close() throws ScansiteUpdaterException {
		try {
			errorWriter.close();
		} catch (Exception e) {
			throw new ScansiteUpdaterException(e.getMessage(), e);
		}
	}
}
