package edu.mit.scansite.server.dataaccess.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Tobieh
 */
public class DatabaseSearchResultFileWriter extends
		ResultFileWriter<DatabaseSearchScanResultSite> {

	private int nMotifs = 1;
	private boolean isMultiple = false;
	private List<Motif> motifs = new ArrayList<Motif>();

	public DatabaseSearchResultFileWriter(boolean isMultiple, int nMotifs,
			List<Motif> motifs) {
		this.isMultiple = isMultiple;
		this.nMotifs = nMotifs;
		this.motifs = motifs;
	}

	public DatabaseSearchResultFileWriter(List<Motif> motifs) {
		this.motifs = motifs;
	}

	@Override
	public String writeResults(String realPath,
			List<DatabaseSearchScanResultSite> sites)
			throws ResultFileWriterException {
		String currentFilePath = getFilePath(realPath);
		BufferedWriter writer = null;
		try {
			DirectoryManagement.prepareDirectory(currentFilePath, true);
			writer = new BufferedWriter(new FileWriter(currentFilePath));
			writer.write("SCORE");
			writer.write(SEPARATOR);
			writer.write("PROTEIN_ACCESSION");
			writer.write(SEPARATOR);
			for (int i = 0; i < nMotifs; ++i) {
				writer.write("SITE_");
				writer.write(motifs.get(i).getDisplayName());
				writer.write(SEPARATOR);
				writer.write("SEQ_");
				writer.write(motifs.get(i).getDisplayName());
				writer.write(SEPARATOR);
			}
			writer.write("PROTEIN_MW");
			writer.write(SEPARATOR);
			writer.write("PROTEIN_PI");
			writer.write(SEPARATOR);
			writer.newLine();
			for (DatabaseSearchScanResultSite site : sites) {
				writer.write(String.valueOf(site.getCombinedScore()));
				writer.write(SEPARATOR);
				writer.write(site.getProtein().getIdentifier());
				writer.write(SEPARATOR);
				if (isMultiple) {
					for (int i = 0; i < site.getSites().size(); ++i) {
						writer.write(site.getSites().get(i).getSite());
						writer.write(SEPARATOR);
						writer.write(site.getSites().get(i).getSiteSequence());
						writer.write(SEPARATOR);
					}
				} else {
					writer.write(site.getSite().getSite());
					writer.write(SEPARATOR);
					writer.write(site.getSite().getSiteSequence());
					writer.write(SEPARATOR);
				}
				writer.write(String.valueOf(site.getProtein()
						.getMolecularWeight()));
				writer.write(SEPARATOR);
				writer.write(String.valueOf(site.getProtein().getpI()));
				writer.write(SEPARATOR);
				writer.newLine();
			}
			writer.close();
			return currentFilePath;
		} catch (Exception e) {
			throw new ResultFileWriterException(e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
				throw new ResultFileWriterException(e);
			}
		}
	}

}
