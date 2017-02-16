package edu.mit.scansite.server.domains;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.dataaccess.file.ConfigReader;
import edu.mit.scansite.server.dataaccess.file.DirectoryManagement;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.transferobjects.DomainPosition;

/**
 * @author Tobieh
 */
public class InterproScanDomainLocatorLocal extends DomainLocator {
	private static final String CFG_IPRSCAN_BIN = "IPRSCAN_BIN";
	private static final String FILE_BASENAME = "domainRequest_";
	private static final String OUTFILE_SUFFIX = "_out";

	private static final String CMD_IN = " -i ";
	private static final String CMD_OUT = " -o ";
	private static final String CMD_APPS = " -appl Pfam ";
	private static final String CMD_END = "-t p -f TSV -u ./";
	//feel free to adjust the memory use to your preferences
	private static final String JAVA_CMD = "java -Xms512M -Xmx2048M -jar interproscan-5.jar";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param reader
	 *            A configreader that is already initialized with a
	 *            interproScanSsh.properties file.
	 */
	public InterproScanDomainLocatorLocal(ConfigReader reader) {
		super(reader);
	}

	@Override
	public void init() {
	}

	@Override
	public ArrayList<DomainPosition> getDomainPositions(String realPath,
			String sequence) throws DomainLocatorException {
		try {
			DirectoryManagement.prepareDomainSeqDirectory(realPath);
			String seqFileName = FILE_BASENAME
					+ String.valueOf(System.nanoTime());
			String localFilePath = FilePaths.getDomainSeqFilePath(realPath,
					seqFileName);
			String outFileName = seqFileName + OUTFILE_SUFFIX;
			String localOutFilePath = FilePaths.getDomainSeqFilePath(realPath,
					outFileName);

			File f = new File(localFilePath);
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write("> InterProScan required FASTA header\n");
			writer.write(sequence);
			writer.close();

			String iprDir = reader.get(CFG_IPRSCAN_BIN);
			String command = JAVA_CMD + CMD_IN + localFilePath + CMD_OUT + localOutFilePath + CMD_APPS + CMD_END;

			Process p = Runtime.getRuntime().exec(command, null, new File(iprDir));
			int exitVal = p.waitFor();
			f.delete();

			ArrayList<DomainPosition> domains = new ArrayList<DomainPosition>();
			if (exitVal == 0) {
				domains = parseInterproScanOutputRAW(localOutFilePath);
			} else {
				throw new DomainLocatorException(
						"Failed to run iprscan! Call:\n" + command
								+ "\nexit code " + String.valueOf(exitVal)
								+ ": ");
			}
			return domains;
		} catch (DomainLocatorException e) {
			logger.error(e.toString());
			throw e;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new DomainLocatorException(e);
		}
	}

	private ArrayList<DomainPosition> parseInterproScanOutputRAW(
			String localOutputFilePath) throws DomainLocatorException {
		final String SPLIT_PATTERN = "\t";
		final int LINEDATA_LENGTH = 11;
		final int IDX_METHOD = 3;
		final int IDX_NAME = 5;
		final int IDX_START = 6;
		final int IDX_END = 7;
		final int IDX_ID = 4;
		final String NO_DESC = "no description";
		ArrayList<DomainPosition> domains = new ArrayList<DomainPosition>();
		File f = new File(localOutputFilePath);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line = reader.readLine();
			while (line != null) {
				String[] lineData = line.split(SPLIT_PATTERN);
				if (lineData != null && lineData.length == LINEDATA_LENGTH) {
					String name = lineData[IDX_NAME].equalsIgnoreCase(NO_DESC) ? lineData[IDX_ID]
							: lineData[IDX_NAME];
					String id = lineData[IDX_ID];
					domains.add(new DomainPosition(Integer
							.valueOf(lineData[IDX_START]), Integer
							.valueOf(lineData[IDX_END]), name,
							lineData[IDX_METHOD], id));
				}
				line = reader.readLine();
			}
			reader.close();
			f.delete();
			return domains;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DomainLocatorException(e);
		}
	}

}
