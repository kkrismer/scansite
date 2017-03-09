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
    private static final String SPECIES_RESTRICTION = "/speciesrestriction=";
    private static final String NO_OF_PHOSPHORYLATIONS = "/numberofphosphorylations=";
    private static final String MW_FROM = "/molweightfrom=";
    private static final String MW_TO = "/molweightto=";
    private static final String PI_FROM = "/isoelectricpointfrom=";
    private static final String PI_TO = "/isoelectricpointto=";
    private static final String KEYWORD = "/keywordrestriction=";
    private static final String SEQUENCE = "/sequencerestriction=";

    public static String processOptionalParameter(String param){
        String correctValue = param;
        if (param.contains(REFERENCE_PARAM_NAME)) {
            correctValue = param.substring(REFERENCE_PARAM_NAME.length());
        } else if (param.contains(MOTIF_PARAM_NAME)){
            correctValue = param.substring(MOTIF_PARAM_NAME.length());
        }else if (param.contains(SPECIES_RESTRICTION)){
            correctValue = param.substring(SPECIES_RESTRICTION.length());
        }else if (param.contains(NO_OF_PHOSPHORYLATIONS)){
            correctValue = param.substring(NO_OF_PHOSPHORYLATIONS.length());
        }else if (param.contains(MW_FROM)){
            correctValue = param.substring(MW_FROM.length());
        }else if (param.contains(MW_TO)){
            correctValue = param.substring(MW_TO.length());
        }else if (param.contains(PI_FROM)){
            correctValue = param.substring(PI_FROM.length());
        }else if (param.contains(PI_TO)){
            correctValue = param.substring(PI_TO.length());
        }else if (param.contains(KEYWORD)){
            correctValue = param.substring(KEYWORD.length());
        }else if (param.contains(SEQUENCE)){
            correctValue = param.substring(SEQUENCE.length());
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
