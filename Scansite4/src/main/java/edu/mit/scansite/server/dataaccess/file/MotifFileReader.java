package edu.mit.scansite.server.dataaccess.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.util.ScansiteScoring;

/**
 * Class for creating motifs from files.
 * 
 * @author tobieh
 */
public class MotifFileReader {

	private static final String LINE_SPLIT_REGEX = "\\s";
	private HashMap<Integer, AminoAcid> aaMap;
	private double[] rowMins = new double[ScansiteConstants.WINDOW_SIZE];
	boolean isXDefined = false;

	public MotifFileReader() {
	}

	/**
	 * @param fileName
	 *            The file that contains a scansite motif.
	 * @return The Motif defined in the given file.
	 * @throws DataAccessException
	 *             Is thrown if the file is not found or cannot be read for
	 *             another reason.
	 */
	public Motif getMotif(String fileName) throws DataAccessException,
			ScansiteFileFormatException {
		Motif m = new Motif();
		readMotif(fileName, m);
		applyDefaults(m);
		applyOptimalScore(m);
		applyFixedCenters(m);
		return m;
	}

	/**
	 * Reads the motif from the file.
	 * 
	 * @param fileName
	 *            The name of the motif file.
	 * @param m
	 *            The motif.
	 * @param lineNr
	 *            The line
	 * @throws ScansiteFileFormatException
	 * @throws DataAccessException
	 */
	private void readMotif(String fileName, Motif m)
			throws ScansiteFileFormatException, DataAccessException {
		int lineNr = 0;
		try {
			BufferedReader reader = getReader(fileName);
			String line = reader.readLine();
			aaMap = parseAALine(line);
			line = reader.readLine();

			// read file line by line and apply values to AAs and minimum values
			// to X
			while (line != null && lineNr < ScansiteConstants.WINDOW_SIZE) {
				String values[] = line.split(LINE_SPLIT_REGEX);
				if (values.length != aaMap.size()) {
					throw new ScansiteFileFormatException(
							"Number of entries per line inconsistent!");
				}

				for (int i = 0; i < values.length; ++i) {
					rowMins[lineNr] = Double.MAX_VALUE;
					double value = 1;
					try {
						value = Double.valueOf(values[i]); // parse value from
															// file
					} catch (Exception e) {
						throw new ScansiteFileFormatException("Value in line "
								+ lineNr + " can not be parsed as a number!", e);
					}
					m.setValue(aaMap.get(i), lineNr, value); // apply value to
																// matrix
					if (aaMap.get(i).isSingleAa()) {
						if (value < rowMins[lineNr]) {
							rowMins[lineNr] = value;
						}
					}
				}

				if (!isXDefined) {
					m.setValue(AminoAcid.X, lineNr, rowMins[lineNr]);
				}
				lineNr++;
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			throw new DataAccessException(
					"Error reading matrix file. File not found: " + fileName, e);
		} catch (IOException e) {
			throw new DataAccessException("Error reading matrix file: "
					+ fileName, e);
		}
		if (lineNr < ScansiteConstants.WINDOW_SIZE) {
			throw new ScansiteFileFormatException("A whole window ("
					+ ScansiteConstants.WINDOW_SIZE
					+ ") has to be defined!\n You just defined " + lineNr
					+ " positions.");
		}
	}

	/**
	 * Calculates the given motif's optimal score.
	 * 
	 * @param m
	 *            The motif.
	 */
	private void applyOptimalScore(Motif m) {
		ScansiteScoring scoring = new ScansiteScoring();
		scoring.calculateOptimalScore(m);
	}

	/**
	 * Applying default values to the given motif m, i.e. assigning min-values
	 * and fixed positions, default 0s and 1s and calculating the optimal score.
	 * 
	 * @param m
	 *            The motif that the defaults are applied to.
	 * @return The post-processed motif m.
	 */
	private void applyDefaults(Motif m) {
		Collection<AminoAcid> definedAas = aaMap.values();
		for (AminoAcid aa : AminoAcid.values()) {
			if (!definedAas.contains(aa)) { // aa not defined in given file!
				for (int row = 0; row < ScansiteConstants.WINDOW_SIZE; ++row) {
					double rowMin = rowMins[row];
					double value = rowMin;
					switch (aa) {
					case B:
						if (definedAas.contains(AminoAcid.D)) {
							double dValue = m.getValue(AminoAcid.D, row);
							value = (definedAas.contains(AminoAcid.N)) ? (dValue + m
									.getValue(AminoAcid.N, row)) / 2.0
									: (dValue + rowMin) / 2.0;
						} else {
							value = (definedAas.contains(AminoAcid.N)) ? (rowMin + m
									.getValue(AminoAcid.N, row)) / 2.0 : rowMin;
						}
						break;
					case Z:
						if (definedAas.contains(AminoAcid.E)) {
							double eValue = m.getValue(AminoAcid.E, row);
							value = (definedAas.contains(AminoAcid.Q)) ? (eValue + m
									.getValue(AminoAcid.Q, row)) / 2.0
									: (eValue + rowMin) / 2.0;
						} else {
							value = (definedAas.contains(AminoAcid.Q)) ? (rowMin + m
									.getValue(AminoAcid.Q, row)) / 2.0 : rowMin;
						}
						break;
					case J:
						if (definedAas.contains(AminoAcid.I)) {
							double iValue = m.getValue(AminoAcid.I, row);
							value = (definedAas.contains(AminoAcid.L)) ? (iValue + m
									.getValue(AminoAcid.L, row)) / 2.0
									: (iValue + rowMin) / 2.0;
						} else {
							value = (definedAas.contains(AminoAcid.L)) ? (rowMin + m
									.getValue(AminoAcid.L, row)) / 2.0 : rowMin;
						}
						break;
					case U:
						value = (definedAas.contains(AminoAcid.C)) ? m
								.getValue(AminoAcid.C, row) : rowMin;
						break;
					case O:
						value = (definedAas.contains(AminoAcid.K)) ? m
								.getValue(AminoAcid.K, row) : rowMin;
						break;
					case _N:
						value = 0;
						break;
					case _C:
						value = 0;
						break;
					case X:
					default:
						value = rowMin;
					}
					m.setValue(aa, row, value);
				}
			}
		}
	}

	/**
	 * @param line
	 *            The first line of the input file which contains those amino
	 *            acids that are defined in this particular file.
	 * @return A Map that maps the index of the amino acids (order in given
	 *         first line) to their amino acid values.
	 * @throws ScansiteFileFormatException
	 *             Is thrown if a non-existant amino acid is defined in the
	 *             file.
	 */
	private HashMap<Integer, AminoAcid> parseAALine(String line)
			throws ScansiteFileFormatException {
		HashMap<Integer, AminoAcid> aaMap = new HashMap<Integer, AminoAcid>();
		if (line != null) {
			String[] aas = line.split(LINE_SPLIT_REGEX);
			for (int i = 0; i < aas.length; ++i) {
				AminoAcid aa = AminoAcid.getValue(aas[i]);
				if (aa != null) {
					aaMap.put(i, aa);
					if (aa.equals(AminoAcid.X)) {
						isXDefined = true;
					}
				} else {
					throw new ScansiteFileFormatException(
							aas[i]
									+ " is not an amino acid that can be used to define a motif-matrix!");
				}
			}
		}
		return aaMap;
	}

	private BufferedReader getReader(String fileName)
			throws FileNotFoundException {
		return new BufferedReader(new FileReader(new File(fileName)));
	}

	private void applyFixedCenters(Motif m) throws ScansiteFileFormatException {
		ArrayList<AminoAcid> fixedCenters = m.getFixedCenters();
		if (fixedCenters.isEmpty()) {
			throw new ScansiteFileFormatException(
					"At least one fixed center (AA with central value "
							+ ScansiteConstants.FIXED_SITE_SCORE
							+ ") has to be defined (usually S, T or Y)!");
		}
	}

}
