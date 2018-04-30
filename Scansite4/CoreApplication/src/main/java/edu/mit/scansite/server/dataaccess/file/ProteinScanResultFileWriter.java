package edu.mit.scansite.server.dataaccess.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import edu.mit.scansite.shared.transferobjects.ScanResultSite;

/**
 * @author Tobieh
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
			writer.write("MOTIF_NAME");
			writer.write(SEPARATOR);
			writer.write("MOTIF_GROUP");
			writer.write(SEPARATOR);
			writer.write("SCORE");
			writer.write(SEPARATOR);
			writer.write("PERCENTILE");
			writer.write(SEPARATOR);
			writer.write("PROTEIN");
			writer.write(SEPARATOR);
			writer.write("SITE");
			writer.write(SEPARATOR);
			writer.write("SITE_SEQUENCE");
			writer.write(SEPARATOR);
			writer.write("SURFACE_ACCESS_VALUE");
			writer.newLine();
			for (ScanResultSite site : hits) {
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
				writer.write(site.getSiteSequence());
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
