package edu.mit.scansite.server.dataaccess;

import edu.mit.scansite.server.dataaccess.commands.CommandConstants;
import edu.mit.scansite.shared.transferobjects.DataSource;

import java.util.Map;
import java.util.Set;

/**
 * @author Thomas Bernwinkler
 */
public class TableSizeInfo {

    private static int uniprotSwissprotCountCache = 0;
    private static int uniprotTremblCountCache = 0;
    private static int ensemblHumanCountCache = 0;
    private static int ensemblMouseCountCache = 0;
    private static int ncbiGenpeptCountCache = 0;
    private static int yeastSgdCountCache = 0;
    private static int swissprotOrthologyCountCache = 0;
    private static int ncbiHomologeneCountCache = 0;
    private static int loctreeCountCache = 0;

    private static final String PROT_SWISSPROT     = "proteins_swissprot";
    private static final String PROT_TREMBL        = "proteins_trembl";
    private static final String PROT_ENSEMBL_HUMAN = "proteins_ensemblHuman";
    private static final String PROT_ENSEMBL_MOUSE = "proteins_ensemblMouse";
    private static final String PROT_GENPEPT       = "proteins_genpept";
    private static final String PROT_YEAST         = "proteins_yeast";

    private static final String ORTH_SWISSPROT     = "orthologs_swissprotorthology";
    private static final String ORTH_HOMOLOGENE    = "orthologs_homologene";

    private static final String LOC_LOCTREE        = "localization_loctree";


    public static void init(Map<DataSource, Integer> tableSizes, CommandConstants cc) {

        Set<DataSource> dataSources = tableSizes.keySet();
        for (DataSource dataSource : dataSources) {
            final String tableName = DataUtils.getTableName(dataSource, cc);
            switch (tableName) {
                case PROT_SWISSPROT:
                    uniprotSwissprotCountCache = tableSizes.get(dataSource);
                    break;
                case PROT_TREMBL:
                    uniprotTremblCountCache  = tableSizes.get(dataSource);
                    break;
                case PROT_ENSEMBL_HUMAN:
                    ensemblHumanCountCache = tableSizes.get(dataSource);
                    break;
                case PROT_ENSEMBL_MOUSE:
                    ensemblMouseCountCache = tableSizes.get(dataSource);
                    break;
                case PROT_GENPEPT:
                    ncbiGenpeptCountCache = tableSizes.get(dataSource);
                    break;
                case PROT_YEAST:
                    yeastSgdCountCache = tableSizes.get(dataSource);
                    break;
                case ORTH_SWISSPROT:
                    swissprotOrthologyCountCache = tableSizes.get(dataSource);
                    break;
                case ORTH_HOMOLOGENE:
                    ncbiHomologeneCountCache = tableSizes.get(dataSource);
                    break;
                case LOC_LOCTREE:
                    loctreeCountCache = tableSizes.get(dataSource);
                    break;
            }
        }
    }

    public static int getCount(String tableName) {
        switch (tableName) {
            case PROT_SWISSPROT:
                return uniprotSwissprotCountCache;
            case PROT_TREMBL:
                return uniprotTremblCountCache;
            case PROT_ENSEMBL_HUMAN:
                return ensemblHumanCountCache;
            case PROT_ENSEMBL_MOUSE:
                return ensemblMouseCountCache;
            case PROT_GENPEPT:
                return ncbiGenpeptCountCache;
            case PROT_YEAST:
                return yeastSgdCountCache;
            case ORTH_SWISSPROT:
                return swissprotOrthologyCountCache;
            case ORTH_HOMOLOGENE:
                return ncbiHomologeneCountCache;
            case LOC_LOCTREE:
                return loctreeCountCache;
            default:
                return 0;
        }
    }
}
