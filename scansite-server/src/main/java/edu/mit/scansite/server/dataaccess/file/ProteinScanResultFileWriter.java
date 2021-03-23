package edu.mit.scansite.server.dataaccess.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ProteinScanResultFileWriter extends
		ResultFileWriter<ScanResultSite> {

	public ProteinScanResultFileWriter() {
	}

	@Override
	public String writeResults(String realPath, List<ScanResultSite> hits)
			throws ResultFileWriterException {
		String currentFilePath = getFilePath(realPath);
		BufferedWriter writer = null;
		try {
			DirectoryManagement.prepareDirectory(currentFilePath, true);
			writer = new BufferedWriter(new FileWriter(currentFilePath));
			writer.write("motif_gene_symbol");
			writer.write(SEPARATOR);
			writer.write("motif_uniprot_entry_name");
			writer.write(SEPARATOR);
			writer.write("motif_name");
			writer.write(SEPARATOR);
			writer.write("motif_group");
			writer.write(SEPARATOR);
			writer.write("score");
			writer.write(SEPARATOR);
			writer.write("percentile");
			writer.write(SEPARATOR);
			writer.write("protein");
			writer.write(SEPARATOR);
			writer.write("site");
			writer.write(SEPARATOR);
			writer.write("site_sequence");
			writer.write(SEPARATOR);
			writer.write("surface_accessibility_value");
			writer.newLine();
			for (ScanResultSite site : hits) {
				List<Identifier> identifiers = site.getMotif().getIdentifiers();
				String geneSymbol = "";
				String uniprotEntryName = "";
				for(Identifier identifier : identifiers) {
					if(identifier.getType().getId() == 4) {
						geneSymbol = identifier.getValue();
					} else if(identifier.getType().getId() == 6) {
						uniprotEntryName = identifier.getValue();
					}
				}
				writer.write(geneSymbol);
				writer.write(SEPARATOR);
				writer.write(uniprotEntryName);
				writer.write(SEPARATOR);
				writer.write(site.getMotif().getDisplayName());
				writer.write(SEPARATOR);
				writer.write(site.getMotif().getGroup() == null ? "-" : site
						.getMotif().getGroup().getDisplayName());
				writer.write(SEPARATOR);
				writer.write(String.valueOf(site.getScore()));
				writer.write(SEPARATOR);
				writer.write(String.valueOf(site.getPercentile()));
				writer.write(SEPARATOR);
				writer.write(site.getProtein().getIdentifier());
				writer.write(SEPARATOR);
				writer.write(site.getSite());
				writer.write(SEPARATOR);
				writer.write(site.getSiteSequence().replaceAll("\\<.*?\\>", ""));
				writer.write(SEPARATOR);
				writer.write(String.valueOf(site.getSurfaceAccessValue()));
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
