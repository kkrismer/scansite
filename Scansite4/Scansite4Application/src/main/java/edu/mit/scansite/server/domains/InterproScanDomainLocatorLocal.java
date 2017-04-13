package edu.mit.scansite.server.domains;

import java.io.*;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.dataaccess.file.ConfigReader;
import edu.mit.scansite.server.dataaccess.file.DirectoryManagement;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.transferobjects.DomainPosition;

/**
 * @author Tobieh
 * @author Thomas Bernwinkler
 */
public class InterproScanDomainLocatorLocal extends DomainLocator {
	private static final String CFG_IPRSCAN_BIN = "IPRSCAN_BIN";
	private static final String FILE_BASENAME = "domainRequest_";
	private static final String OUTFILE_SUFFIX = "_out";

	private static final String CMD_IN = " -i ";
	private static final String CMD_OUT = " -o ";
	private static final String CMD_APPS = " -appl Pfam ";
	private static final String CMD_IPR  = "-iprlookup ";
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
			String command = JAVA_CMD + CMD_IN + localFilePath + CMD_OUT
					+ localOutFilePath + CMD_APPS + CMD_IPR + CMD_END;

			Process logProcess = Runtime.getRuntime().exec("cat " + localFilePath);
			int catExitVal = logProcess.waitFor();

			BufferedReader content = new BufferedReader(new
					InputStreamReader(logProcess.getInputStream()));

			String sContent;
			System.out.println("############################################## InterProScan Input ##############################################");
			if (catExitVal != 0) {
				System.out.println("Could not log the input file. File not found.");
			}
			while ((sContent = content.readLine()) != null) {
				System.out.println(sContent);
			}
			System.out.println("############################################## END InterProScan Input ##############################################");

			Process p = Runtime.getRuntime().exec(command, null, new File(iprDir));

			// ##############################################

			BufferedReader stdInput = new BufferedReader(new
					InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new
					InputStreamReader(p.getErrorStream()));

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			String s = null;
			System.out.println("############################################## INFO ##############################################");
			while ((s = stdInput.readLine()) != null) {
				logger.info(s);
				System.out.println(s);
			}
			System.out.println("############################################## END INFO ##############################################");

			// read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			System.out.println("############################################## ERROR ##############################################");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
			System.out.println("############################################## END ERROR ##############################################");

			// ##############################################

			int exitVal = p.waitFor();
			f.delete();

			if (exitVal == 0) {
				return parseInterproScanOutputRAW(localOutFilePath);
			} else {
				throw new DomainLocatorException(
						"Failed to run iprscan! Call:\n" + command
								+ "\nexit code " + String.valueOf(exitVal) + ": ");
			}
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
		final int LINEDATA_LENGTH = 11+2; //IPR code and alternative name
		final int IDX_METHOD = 3;
		final int IDX_NAME = 5;
		final int IDX_START = 6;
		final int IDX_END = 7;
		final int IDX_ID = 4;
		final int IDX_IPR = 11;
		final int IDX_ALT = 12;
		final String NO_DESC = "no description";
		ArrayList<DomainPosition> domains = new ArrayList<DomainPosition>();
		File f = new File(localOutputFilePath);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line = reader.readLine();
			while (line != null) {
				String[] lineData = line.split(SPLIT_PATTERN);
				if (lineData != null && lineData.length == LINEDATA_LENGTH) {
					String name = lineData[IDX_NAME].equalsIgnoreCase(NO_DESC)
                            ? lineData[IDX_ID] : lineData[IDX_NAME];
					String id = lineData[IDX_ID];
					String IPRCode = lineData[IDX_IPR];
					String alternativeName = lineData[IDX_ALT];
					domains.add(new DomainPosition(Integer
							.valueOf(lineData[IDX_START]), Integer
							.valueOf(lineData[IDX_END]), name,
							lineData[IDX_METHOD], id, IPRCode, alternativeName));
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
