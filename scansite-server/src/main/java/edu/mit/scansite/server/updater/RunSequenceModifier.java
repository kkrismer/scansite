package edu.mit.scansite.server.updater;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DaoFactory;
import edu.mit.scansite.server.dataaccess.DataSourceDao;
import edu.mit.scansite.server.dataaccess.ProteinDao;
import edu.mit.scansite.server.dataaccess.commands.protein.CountProteinsCommand;
import edu.mit.scansite.server.dataaccess.commands.protein.ProteinGetNCommand;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.Protein;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 3/24/2017.
 */
public class RunSequenceModifier {
    private static final Logger logger = LoggerFactory.getLogger(RunSequenceModifier.class);

    public static void run() throws ScansiteUpdaterException {
        try {
            DataSource swissProtDataSource = getSwissprotDataSource(ServiceLocator.getDaoFactory());
            CountProteinsCommand countCmd = new CountProteinsCommand(ServiceLocator.getDbAccessProperties(),
                    ServiceLocator.getDbConstantsProperties(), swissProtDataSource);

            ProteinDao proteinDao = ServiceLocator.getDaoFactory().getProteinDao();

            Integer count = countCmd.execute();

            if (count == null) {
                logger.error("Could not read database size! Aborting sequence modifier...");
                return;
            } else  {
                logger.info("Found " + count + " proteins to process. Preparing for processing...");
            }

            int iterationProteinCount = 100;
            logger.info("Loading site evidence ...");

            HashMap<String, List<String> > evidenceMap = ServiceLocator
                    .getDaoFactory().getSiteEvidenceDao().getEvidenceMap();

            logger.info("Starting processing of protein sequences ...");
            for (int i = 0; i < count; i += iterationProteinCount) {
                ProteinGetNCommand proteinCmd = new ProteinGetNCommand(ServiceLocator.getDbAccessProperties(),
                        ServiceLocator.getDbConstantsProperties(),
                        swissProtDataSource, i, iterationProteinCount);


                //List<Protein> test = ServiceLocator.getDaoFactory().getProteinDao().getAll(swissProtDataSource);
                List<Protein> ps = proteinCmd.execute();
                List<Protein> proteins = ServiceLocator.getDaoFactory().getProteinDao().getProteinInformation(ps, swissProtDataSource);
                for (Protein protein : proteins) {
                    if (protein == null) {
                        continue;
                    }

                    String sequence = protein.getSequence();
                    if (sequence == null || sequence.isEmpty()) {
                        continue;
                    }
                    Set<String> accessions = protein.getAnnotation("accession");
                    if (accessions == null) {
                        continue;
                    }
                    List<String> siteList = new ArrayList<>();
                    for (String accession : accessions) {
                        if(evidenceMap.containsKey(accession)) {
                            siteList.addAll(evidenceMap.get(accession));
                        }
                    }

                    boolean foundEvidence = false;
                    for (String site : siteList) {
                        String evidenceSite = site.substring(1);
                        int position = Integer.valueOf(evidenceSite)-1;
                        if (isPotentiallyPhosphorylated(site, 0)) {
                            if(isPotentiallyPhosphorylated(sequence, position)) {
                                sequence = adjustSequencePosition(sequence, position);
                                foundEvidence = true;
                            }
                        } else { // other modifications in site evidence data
                            if (isOtherModification(site, 0)) {
                                if (isOtherModification(sequence, position)) {
                                    logger.info("Detected Arginine/Lysine site: " + site);
                                    //with the current data structure one cen not distinguish between
                                    // acetylation and methylatioon on lysine. One would need to add another column to the
                                    // siteEvidence table which contains the modification type
                                    sequence = adjustSequencePosition(sequence, position);
                                    foundEvidence = true;
                                }
                            } else {
                                logger.warn("Skipped undefined site (not in {S,T,Y,K,R}): " + site);
                            }
                        }
                    }
                    if (foundEvidence) {
                        protein.setSequence(sequence);
                        proteinDao.updateProtein(swissProtDataSource, protein);
                    }

                }
                if (i % 1000 == 0) {
                    logger.info("Processed proteins: " + i);
                }
            }


            logger.info("Finished editing protein sequences");
        } catch (Exception e) {
            logger.error("Could not run Sequence Modifier due to the following error:\n" + e.getMessage());
            throw new ScansiteUpdaterException(e.getMessage());
        }
    }

    private static boolean isPotentiallyPhosphorylated(String s, int position) {
        if (s == null || s.length() <= position) {
            if (s != null) {
                logger.warn("Could not find site position [" + position + "] in sequence: " + s);
            }
            return false;
        }
        char c = s.charAt(position);
        return c == 'S' || c == 'T' || c == 'Y';
    }

    private static boolean isOtherModification(String s, int position) {
        if (s == null || s.length() <= position) {
            if (s != null) {
                logger.warn("Could not find site position [" + position + "] in sequence: " + s);
            }
            return false;
        }
        char c = s.charAt(position);
        return c == 'K' || c == 'R';
    }

    private static String adjustSequencePosition(String sequence, int position) {
        return sequence.substring(0, position)
                + Character.toLowerCase(sequence.charAt(position))
                + (sequence.length() > position ? sequence.substring(position+1) : "");
    }

    private static DataSource getSwissprotDataSource(DaoFactory factory) throws ScansiteUpdaterException, DataAccessException {
        final boolean primaryDataSourceOnly = true;
        DataSourceDao dsDao = factory.getDataSourceDao();
        List<DataSource> dataSources = dsDao.getAll(primaryDataSourceOnly);

        if (dataSources == null) {
            throw new ScansiteUpdaterException("RunSequenceModifier could not load data sources");
        }
        for (DataSource ds : dataSources) {
            if (ds.getShortName().equals("swissprot")){
                return ds;
            }
        }
        return null;
    }

}
