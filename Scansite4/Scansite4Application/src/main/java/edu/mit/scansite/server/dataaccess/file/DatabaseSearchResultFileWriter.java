package edu.mit.scansite.server.dataaccess.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * @author Tobieh
 * @author Konstantin Krismer
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
			writer.write("score");
			writer.write(SEPARATOR);
			writer.write("protein_accession");
			writer.write(SEPARATOR);
			for (int i = 0; i < nMotifs; ++i) {
				writer.write("site_");
				writer.write(motifs.get(i).getDisplayName());
				writer.write(SEPARATOR);
				writer.write("seq_");
				writer.write(motifs.get(i).getDisplayName());
				writer.write(SEPARATOR);
			}
			writer.write("protein_mw");
			writer.write(SEPARATOR);
			writer.write("protein_pi");
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
						// first, remove HTML tags from sequence
						writer.write(site.getSites().get(i).getSiteSequence().replaceAll("\\<.*?\\>", ""));
						writer.write(SEPARATOR);
					}
				} else {
					writer.write(site.getSite().getSite());
					writer.write(SEPARATOR);
					// first, remove HTML tags from sequence
					writer.write(site.getSite().getSiteSequence().replaceAll("\\<.*?\\>", ""));
					writer.write(SEPARATOR);
				}
				writer.write(String.valueOf(site.getProtein()
						.getMolecularWeight()));
				writer.write(SEPARATOR);
				writer.write(String.valueOf(site.getProtein().getpI()));
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
