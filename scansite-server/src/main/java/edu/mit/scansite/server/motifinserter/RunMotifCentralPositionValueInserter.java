package edu.mit.scansite.server.motifinserter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.commands.motif.MotifGetCommand;
import edu.mit.scansite.server.dataaccess.commands.motifrealcentralvalues.MotifValuesAddCommand;
import edu.mit.scansite.shared.DatabaseException;
import edu.mit.scansite.shared.transferobjects.Motif;

/**
 * Requires only the path of all the relevant motif files are in (PSSMs with
 * central position values != 21)
 * 
 * @author Thomas Bernwinkler
 */
public class RunMotifCentralPositionValueInserter {
	public static void main(String[] args) {
		String path = args[0];
		if (!path.endsWith("/")) {
			path += "/";
		}

		File folder = new File(path);
		File[] files = folder.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				System.out.println("Processing file " + files[i].getName());
				handleMotif(files[i]);
			}
		}
	}

	private static void handleMotif(File input) {
		String fileEnding = ".txt";
		int len = fileEnding.length();
		String motifShortName = input.getName().substring(0, input.getName().length() - len);

		// parse file and save to Double[]
		List<String> content = readLines(input);
		String[] columnLabels = content.get(0).split("\t");

		int sColumnIdx = 0;
		int tColumnIdx = 0;
		int yColumnIdx = 0;

		for (int i = 0; i < columnLabels.length; i++) {
			if (columnLabels[i].equals("S")) {
				sColumnIdx = i;
			} else if (columnLabels[i].equals("T")) {
				tColumnIdx = i;
			} else if (columnLabels[i].equals("Y")) {
				yColumnIdx = i;
			}
		}

		int lineNo = 8; // zero based + heading + {-7;0}
		double sColumnValue = Double.valueOf((content.get(lineNo).split("\t"))[sColumnIdx]);
		double tColumnValue = Double.valueOf((content.get(lineNo).split("\t"))[tColumnIdx]);
		double yColumnValue = Double.valueOf((content.get(lineNo).split("\t"))[yColumnIdx]);

		Double[] values = { sColumnValue, tColumnValue, yColumnValue };

		try {
			MotifGetCommand motifGetCommand = new MotifGetCommand(ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(), motifShortName);
			Motif m = motifGetCommand.execute();

			MotifValuesAddCommand cmd = new MotifValuesAddCommand(ServiceLocator.getDbAccessProperties(),
					ServiceLocator.getDbConstantsProperties(), values, m.getId());

			cmd.execute();

		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	private static List<String> readLines(File file) {
		List<String> content = new ArrayList<>();
		String fileName = file.getAbsolutePath();
		String line;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				content.add(line);
			}

			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + file.getName() + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + file.getName() + "'");
			ex.printStackTrace();
		}
		return content;
	}
}
