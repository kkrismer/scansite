package edu.mit.scansite.server.updater.transliterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import edu.mit.scansite.server.updater.ScansiteUpdaterException;

/**
 * @author Tobieh
 */
public interface ProteinTransliteratorWriter {

  void writeEntrySpacer() throws ScansiteUpdaterException;

  void saveInvalidEntry(String proteinId) throws ScansiteUpdaterException;

  void saveEntry(String proteinId, HashMap<String, Set<String>> annotations,
      Double mw, Double pI, ArrayList<String> taxons, String species, String sequence) throws ScansiteUpdaterException;

  void close() throws ScansiteUpdaterException;
}
