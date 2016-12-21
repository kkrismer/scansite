package edu.mit.scansite.server.updater;

import edu.mit.scansite.server.dataaccess.file.RunEvidenceInserter;
import edu.mit.scansite.server.motifinserter.RunMotifInserter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by Thomas on 15.12.2016.
 * Runs the following database insertion features for an empty database
 * - DatabaseUpdater
 * - MotifInserter (Mammals & Yeast)
 * - EvidenceInserter
 *
 * @author Thomas
 */

public class RunDatabaseInsertionManager {
    private static final Logger logger = LoggerFactory
            .getLogger(RunUpdater.class);

    private static final String mammalFileIdentifier = "-moma";
    private static final String yeastFileIdentifier  = "-moye";
    private static final String emailIdentifier      = "-mail";
    private static final String evidenceIdentifier   = "-evid";
    private static final String helpIdentifier       = "-help";

    private static String motifMammalFile;
    private static String motifYeastFile;
    private static String motifEmail;
    private static String evidenceFile;

    private static boolean isMammal = false;
    private static boolean isYeast  = false;
    private static boolean isMail   = false;
    private static boolean isEvid   = false;
    private static boolean isHelp   = false;

    /**
      * As a number of parameters is required in this application,
      * it is easily able to mess up with the order of the command line arguemnts.
      * Hence the order of the parameters does not matter, however one needs to use
      * the following declarations for the parameters (including the hyphens/dashes).
      * Please also make sure to actually replace the whole term <parameter> with the
      * appropriate parameter.
      *
      * -moma <path/to/motifMammalsFile>
      * -moye <path/to/motifYeastFile>
      * -mail <user@mit.edu>
      * -evid <path/to/evidenceFile>
      *
      */
    public static void main(String[] args) {
        String[] updaterParams      = new String[0];
        String[] motifMammalsParams = new String[2];
        String[] motifYeastParams   = new String[2];
        String[] evidenceParams     = new String[1];

        for (String arg : args) {
            if (isMammal || isYeast || isMail || isEvid) {
                if (!assignParam(arg)) {
                    logger.error("ERROR! A parameter could not be assigned!\n" +
                            "Parameter arguments should be given like:" +
                            "-moma misc/motifs/motifsMammals/ -moye misc/motifs/motifsYeast/" +
                            " -mail krismer@mit.edu -evid misc/siteEvidence/evidence.txt");
                    return;
                }
            } else if (isHelp) {
                logger.info("Parameter arguments should be given like:" +
                        "-moma misc/motifs/motifsMammals/ -moye misc/motifs/motifsYeast/" +
                        " -mail krismer@mit.edu -evid misc/siteEvidence/evidence.txt");
                return;
            }

            switch (arg) {
                case mammalFileIdentifier:
                    isMammal = true;
                    break;
                case yeastFileIdentifier:
                    isYeast = true;
                    break;
                case emailIdentifier:
                    isMail = true;
                    break;
                case evidenceIdentifier:
                    isEvid = true;
                    break;
                case helpIdentifier:
                    isHelp = true;
                    break;
            }
        }

        motifMammalsParams[0] = motifMammalFile;
        motifMammalsParams[1] = motifEmail;
        motifYeastParams[0]   = motifYeastFile;
        motifYeastParams[1]   = motifEmail;
        evidenceParams[0]     = evidenceFile;

        String errorFile = "";
        if(motifMammalFile == null || motifMammalFile.isEmpty()) {
            errorFile = "the Motif file (mammals)";
        } else if (motifYeastFile == null || motifYeastFile.isEmpty()) {
            errorFile = "The Motif file (yeast)";
        } else if (motifEmail == null || motifEmail.isEmpty()) {
            errorFile = "User email for motif runs";
        } else if (evidenceFile == null || evidenceFile.isEmpty()) {
            errorFile = "The evidence file";
        }

        if (!errorFile.isEmpty()) {
            logger.error(errorFile + " was not assigned correctly. Please check the parameters!\n" +
                    "Parameter arguments should be given like:" +
                    "-moma misc/motifs/motifsMammals/ -moye misc/motifs/motifsYeast/" +
                    " -mail krismer@mit.edu -evid misc/siteEvidence/evidence.txt");
            return;
        }

        //logger does not write info logs to console
        logger.info("Assigned parameters successfully! Launching applications...");
       try {
           logger.info("Running \"RunUpdater\", Database Updater...");
           RunUpdater.main(updaterParams);

           logger.info("Running \"RunMotifInserter\" to insert mammal Motifs...");
           RunMotifInserter.main(motifMammalsParams);

           logger.info("Running \"RunMotifInserter\" to insert yeast Motifs...");
           RunMotifInserter.main(motifYeastParams);

           logger.info("Running \"RunEvidenceInserter\" to insert evidence data...");
           RunEvidenceInserter.main(evidenceParams);
       } catch (Exception ex) {
           logger.error(ex.getMessage());
       }

        logger.info("Deleting temporary files...");
        File tempDir = new File("temp/");
        File scansite4_tempDir = new File("scansite4_temp/");

        try {
            FileUtils.deleteDirectory(tempDir);
        } catch (IOException e) {
            logger.warn(e.getMessage());
            logger.warn("Could not delete the temporary directory [" + tempDir.getAbsolutePath() + "]! Please do so manually!");
        }
        try {
            FileUtils.deleteDirectory(scansite4_tempDir);
        } catch (IOException e) {
            logger.warn(e.getMessage());
            logger.warn("Could not delete the temporary directory [" + scansite4_tempDir.getAbsolutePath() + "]! Please do so manually!");
        }

        logger.info("Done.");
    }

    private static boolean assignParam(String value) {
        if(isMammal) {
            motifMammalFile = value;
            isMammal = false;
            return !motifMammalFile.startsWith("<");
        } else if(isYeast) {
            motifYeastFile = value;
            isYeast = false;
            return !motifYeastFile.startsWith("<");
        } else if(isMail) {
            motifEmail = value;
            isMail = false;
            return !motifEmail.startsWith("<");
        } else if(isEvid) {
            evidenceFile = value;
            isEvid = false;
            return !evidenceFile.startsWith("<");
        }
        return false;
    }

}
