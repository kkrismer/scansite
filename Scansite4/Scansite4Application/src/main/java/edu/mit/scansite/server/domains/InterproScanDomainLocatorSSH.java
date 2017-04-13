package edu.mit.scansite.server.domains;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import edu.mit.scansite.server.dataaccess.file.ConfigReader;
import edu.mit.scansite.server.dataaccess.file.DirectoryManagement;
import edu.mit.scansite.server.dataaccess.ssh.SshConnector;
import edu.mit.scansite.server.dataaccess.ssh.SshConnector.SshResult;
import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.transferobjects.DomainPosition;

import static edu.mit.scansite.server.domains.InterproScanDomainLocatorLocal.parseInterproScanOutputRAW;

/**
 * @author Tobieh
 */
public class InterproScanDomainLocatorSSH extends DomainLocator {

	private static final String CFG_IPRSCAN_BIN = "IPRSCAN_BIN";
	private SshConnector connector = new SshConnector();
	private final static String FILE_BASENAME = "domainRequest_";
	private final static String OUTFILE_SUFFIX = "_out";

	private final static String CMD_BASE = "iprscan -cli ";
	private final static String CMD_IN = " -i ";
	private final static String CMD_OUT = " -o ";
	private final static String CMD_APPS = " -appl hmmpfam ";
	private final static String CMD_END = " -seqtype p -format raw ";

	/**
	 * @param reader
	 *            A configreader that is already initialized with a
	 *            interproScanSsh.properties file.
	 */
	public InterproScanDomainLocatorSSH(ConfigReader reader) {
		super(reader);
	}

	@Override
	public void init() {
		connector.init(reader);
	}

	@Override
	public ArrayList<DomainPosition> getDomainPositions(String realPath, String sequence)
			throws DomainLocatorException {
		try {
			connector.connect();
			String seqFileName = FILE_BASENAME
					+ String.valueOf(System.nanoTime());
			DirectoryManagement.prepareDomainSeqDirectory(realPath);
			String localFilePath = FilePaths.getDomainSeqFilePath(realPath, seqFileName);
			String outFileName = seqFileName + OUTFILE_SUFFIX;
			String localOutFilePath = FilePaths
					.getDomainSeqFilePath(realPath, outFileName);

			// create sequence file on server
			File f = new File(localFilePath);
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			writer.write(sequence);
			writer.close();
			connector.copyFileLocalToRemote(localFilePath, seqFileName);
			f.delete();

			// run iprscan and copy result-file to local machine
			String command = reader.get(CFG_IPRSCAN_BIN) + CMD_BASE + CMD_IN
					+ seqFileName + CMD_OUT + outFileName + CMD_APPS + CMD_END;
			SshResult result = connector.runCommand(command);
			connector.copyFileRemoteToLocal(outFileName, localOutFilePath);

			// delete created file(s) on server
			connector.runCommand("rm " + seqFileName);
			connector.runCommand("rm " + outFileName);
			ArrayList<DomainPosition> domains = new ArrayList<DomainPosition>();
			if (result.getExitStatus() == 0) {
				domains = parseInterproScanOutputRAW(localOutFilePath);
			}
			return domains;
		} catch (DomainLocatorException e) {
			throw e;
		} catch (Exception e) {
			throw new DomainLocatorException(e);
		} finally {
			connector.disconnect();
		}
	}


}
