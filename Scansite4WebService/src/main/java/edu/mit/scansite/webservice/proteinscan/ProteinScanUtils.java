package edu.mit.scansite.webservice.proteinscan;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.DataSourceDao;
import edu.mit.scansite.server.dataaccess.ProteinDao;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.webservice.exception.ScansiteWebServiceException;
import edu.mit.scansite.webservice.otherservices.DatasourcesService;

import java.util.List;

/**
 * Created by Thomas on 3/8/2017.
 */
public class ProteinScanUtils {
    public static final String REFERENCE_VERTEBRATA = "Vertebrata";
    public static final String REFERENCE_YEAST = "Saccharomyces cerevisiae";
    private static final String REFERENCE_PARAM_NAME = "/referenceproteome=";
    private static final String MOTIF_PARAM_NAME = "/motifshortnames=";
    // database scan or database search
    private static final String DS_SPECIES_RESTRICTION = "/speciesrestriction=";
    private static final String DS_NO_OF_PHOSPHORYLATIONS = "/numberofphosphorylations=";
    private static final String DS_MW_FROM = "/molweightfrom=";
    private static final String DS_MW_TO = "/molweightto=";
    private static final String DS_PI_FROM = "/isoelectricpointfrom=";
    private static final String DS_PI_TO = "/isoelectricpointto=";
    private static final String DS_KEYWORD = "/keywordrestriction=";
    private static final String DS_SEQUENCE = "/sequencerestriction=";
    // sequence match
    private static final String SM_SPECIES_RESTRICTION = "/speciesrestriction=";
    private static final String SM_PHOSPHORYLATION_NO = "/numberofphosphorylations=";
    private static final String SM_MW_FROM = "/molweightfrom=";
    private static final String SM_MW_TO = "/molweightto=";
    private static final String SM_PI_FROM = "/isoelectricpointfrom=";
    private static final String SM_PI_TO = "/isoelectricpointto=";
    private static final String SM_KEYWORD = "/keywordrestriction=";

    public static String processOptionalParameter(String param){
        String correctValue = param;
        // protein scan parameters
        if (param.contains(REFERENCE_PARAM_NAME)) {
            correctValue = param.substring(REFERENCE_PARAM_NAME.length());
        } else if (param.contains(MOTIF_PARAM_NAME)){
            // database search parameters
            correctValue = param.substring(MOTIF_PARAM_NAME.length());
        }else if (param.contains(DS_SPECIES_RESTRICTION)){
            correctValue = param.substring(DS_SPECIES_RESTRICTION.length());
        }else if (param.contains(DS_NO_OF_PHOSPHORYLATIONS)){
            correctValue = param.substring(DS_NO_OF_PHOSPHORYLATIONS.length());
        }else if (param.contains(DS_MW_FROM)){
            correctValue = param.substring(DS_MW_FROM.length());
        }else if (param.contains(DS_MW_TO)){
            correctValue = param.substring(DS_MW_TO.length());
        }else if (param.contains(DS_PI_FROM)){
            correctValue = param.substring(DS_PI_FROM.length());
        }else if (param.contains(DS_PI_TO)){
            correctValue = param.substring(DS_PI_TO.length());
        }else if (param.contains(DS_KEYWORD)){
            correctValue = param.substring(DS_KEYWORD.length());
        }else if (param.contains(DS_SEQUENCE)){
            correctValue = param.substring(DS_SEQUENCE.length());
        }else if (param.contains(SM_SPECIES_RESTRICTION)){
            // sequence match parameters
            correctValue = param.substring(SM_SPECIES_RESTRICTION.length());
        }else if (param.contains(SM_PHOSPHORYLATION_NO)){
            correctValue = param.substring(SM_PHOSPHORYLATION_NO.length());
        }else if (param.contains(SM_MW_FROM)){
            correctValue = param.substring(SM_MW_FROM.length());
        }else if (param.contains(SM_MW_TO)){
            correctValue = param.substring(SM_MW_TO.length());
        }else if (param.contains(SM_PI_FROM)){
            correctValue = param.substring(SM_PI_FROM.length());
        }else if (param.contains(SM_PI_TO)){
            correctValue = param.substring(SM_PI_TO.length());
        }else if (param.contains(SM_KEYWORD)){
            correctValue = param.substring(SM_KEYWORD.length());
        }
        return correctValue;
    }

    public static String checkReferenceProteome(String referenceProteome) {
        String refProteome;
        if (referenceProteome.toLowerCase().equals("vertebrata")) {
            refProteome = REFERENCE_VERTEBRATA;
        } else if (referenceProteome.toLowerCase().equals("yeast") || referenceProteome.toLowerCase().equals("saccharomyces")
                || referenceProteome.toLowerCase().equals("cerevisiae") || referenceProteome.toLowerCase().equals("saccharomycescerevisiae")) {
            refProteome = REFERENCE_YEAST;
        } else {
            System.out.println("Could not find reference proteome for expression: " + referenceProteome);
            System.out.println("Using default value: " + REFERENCE_VERTEBRATA);
            refProteome = REFERENCE_VERTEBRATA;
        }
        return refProteome;
    }

    public static LightWeightProtein getLightWeightProtein(String proteinIdentifier, String dataSourceShortName) {
        if(proteinIdentifier == null || proteinIdentifier.isEmpty()) {
            throw new ScansiteWebServiceException("ProteinIdentifierScanService: Missing protein identifier!");
        } else {
            try {
                ProteinDao proteinDao = ServiceLocator.getWebServiceInstance().getSvcDaoFactory().getProteinDao();
                DataSourceDao dataSourceDao = ServiceLocator.getWebServiceInstance().getSvcDaoFactory().getDataSourceDao();
                return proteinDao.get(proteinIdentifier, dataSourceDao.get(dataSourceShortName));
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static DataSource getLocalizationDataSource(String errorMessage) throws DataAccessException {
        List<DataSource> dataSources = new DatasourcesService()
                .retrieveDataSources(ServiceLocator.getWebServiceInstance().getDaoFactory());
        if (dataSources == null) {
            throw new ScansiteWebServiceException("Server can not access database. " + errorMessage);
        }
        for (DataSource ds : dataSources) {
            if (ds.getShortName().equals("loctree")){
                return ds;
            }
        }
        return null;
    }
}
