package edu.mit.scansite.server.dataaccess.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.mit.scansite.server.updater.DataSourceMetaInfo;
import edu.mit.scansite.server.updater.DbUpdaterConfig;
import edu.mit.scansite.server.updater.ScansiteUpdaterException;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.DataSourceType;
import edu.mit.scansite.shared.transferobjects.EvidenceCode;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * Reads an dbUpdater xml file and saves its content to an
 * DbUpdaterConfig-class.
 * 
 * @author tobieh
 * @author Konstantin Krismer
 */
public class UpdaterConfigXmlFileReader {
	private static final String GENERAL = "general";
	private static final String TEMP_DIR_FILEPATH = "tempDirPath";
	private static final String INVALID_FILE_PREFIX = "invalidFilesPrefix";
	private static final String DATA_SOURCE_TYPES = "dataSourceTypes";
	private static final String DATA_SOURCE_TYPE = "dataSourceType";
	private static final String ID = "id";
	private static final String SHORT_NAME = "shortName";
	private static final String DISPLAY_NAME = "displayName";
	private static final String IDENTIFIER_TYPES = "identifierTypes";
	private static final String IDENTIFIER_TYPE = "identifierType";
	private static final String IDENTIFIER_TYPE_NAME = "name";
	private static final String EVIDENCE_CODES = "evidenceCodes";
	private static final String EVIDENCE_CODE = "evidenceCode";
	private static final String EVIDENCE_CODE_CODE = "code";
	private static final String EVIDENCE_CODE_NAME = "name";
	private static final String DATA_SOURCES = "dataSources";
	private static final String DATA_SOURCE = "dataSource";
	private static final String DATA_SOURCE_TYPE_ID = "typeId";
	private static final String DATA_SOURCE_IDENTIFIER_TYPE_ID = "identifierTypeId";
	private static final String DATA_SOURCE_URL = "url";
	private static final String DATA_SOURCE_VERSION_URL = "versionUrl";
	private static final String DATA_SOURCE_UPDATER = "updaterClass";
	private static final String DATA_SOURCE_ENCODING = "encoding";
	private static final String DATA_SOURCE_ORGANISM = "organismName";
	private static final String DATA_SOURCE_DESCRIPTION = "description";
	private static final String DATA_SOURCE_ISPRIMARY = "isPrimaryDatasource";

	public DbUpdaterConfig readConfig(InputStream configFileStream,
			final InputStream configDTDFileStream)
			throws ScansiteUpdaterException {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			dBuilder.setEntityResolver(new EntityResolver() {
				@Override
				public InputSource resolveEntity(String publicId,
						String systemId) throws SAXException, IOException {
					if (systemId.contains("UpdaterConstants.dtd")) {
						return new InputSource(configDTDFileStream);
					} else {
						return null;
					}
				}
			});

			Document document = dBuilder.parse(configFileStream);
			document.getDocumentElement().normalize();

			Element generalElement = (Element) document.getElementsByTagName(
					GENERAL).item(0);
			Element dataSourceTypesElement = (Element) document
					.getElementsByTagName(DATA_SOURCE_TYPES).item(0);
			Element identifierTypesElement = (Element) document
					.getElementsByTagName(IDENTIFIER_TYPES).item(0);
			Element evidenceCodesElement = (Element) document
					.getElementsByTagName(EVIDENCE_CODES).item(0);
			Element dataSourcesElement = (Element) document
					.getElementsByTagName(DATA_SOURCES).item(0);

			List<DataSourceType> dataSourceTypes = parseDataSourceTypes(dataSourceTypesElement);
			List<IdentifierType> identifierTypes = parseIdentifierTypes(identifierTypesElement);
			List<EvidenceCode> evidenceCodes = parseEvidenceCodes(evidenceCodesElement);
			List<DataSourceMetaInfo> dataSources = parseDataSources(
					dataSourcesElement,
					initDataSourceTypesMap(dataSourceTypes),
					initIdentifierTypesMap(identifierTypes));
			return parseConfig(generalElement, dataSourceTypes,
					identifierTypes, evidenceCodes, dataSources);
		} catch (FileNotFoundException e) {
			throw new ScansiteUpdaterException(
					"DbUpdater-Configfile not found: " + e.getMessage(), e);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new ScansiteUpdaterException(
					"DbUpdater-Configfile cannot be read: " + e.getMessage(), e);
		}
	}

	private DbUpdaterConfig parseConfig(Element generalElement,
			List<DataSourceType> dataSourceTypes,
			List<IdentifierType> identifierTypes,
			List<EvidenceCode> evidenceCodes,
			List<DataSourceMetaInfo> dataSources) {
		String tempDirPath = generalElement
				.getElementsByTagName(TEMP_DIR_FILEPATH).item(0)
				.getTextContent().trim();
		String invalidFilePrefix = generalElement
				.getElementsByTagName(INVALID_FILE_PREFIX).item(0)
				.getTextContent().trim();
		return new DbUpdaterConfig(tempDirPath, invalidFilePrefix,
				dataSourceTypes, identifierTypes, evidenceCodes, dataSources);
	}

	private Map<Integer, DataSourceType> initDataSourceTypesMap(
			List<DataSourceType> dataSourceTypes) {
		Map<Integer, DataSourceType> map = new HashMap<>();

		for (DataSourceType type : dataSourceTypes) {
			map.put(type.getId(), type);
		}

		return map;
	}

	private Map<Integer, IdentifierType> initIdentifierTypesMap(
			List<IdentifierType> identifierTypes) {
		Map<Integer, IdentifierType> map = new HashMap<>();

		for (IdentifierType type : identifierTypes) {
			map.put(type.getId(), type);
		}

		return map;
	}

	private List<DataSourceType> parseDataSourceTypes(
			Element dataSourceTypesElement) {
		List<DataSourceType> dataSourceTypes = new LinkedList<>();
		NodeList nodeList = dataSourceTypesElement
				.getElementsByTagName(DATA_SOURCE_TYPE);
		for (int i = 0; i < nodeList.getLength(); ++i) {
			Element node = (Element) nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				int id = Integer.parseInt(node.getElementsByTagName(ID).item(0)
						.getTextContent().trim());
				String shortName = node.getElementsByTagName(SHORT_NAME)
						.item(0).getTextContent().trim();
				String displayName = node.getElementsByTagName(DISPLAY_NAME)
						.item(0).getTextContent().trim();
				dataSourceTypes.add(new DataSourceType(id, shortName,
						displayName));
			}
		}

		return dataSourceTypes;
	}

	private List<IdentifierType> parseIdentifierTypes(
			Element identifierTypesElement) {
		List<IdentifierType> identifierTypes = new LinkedList<>();
		NodeList nodeList = identifierTypesElement
				.getElementsByTagName(IDENTIFIER_TYPE);
		for (int i = 0; i < nodeList.getLength(); ++i) {
			Element node = (Element) nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				int id = Integer.parseInt(node.getElementsByTagName(ID).item(0)
						.getTextContent().trim());
				String name = node.getElementsByTagName(IDENTIFIER_TYPE_NAME)
						.item(0).getTextContent().trim();
				identifierTypes.add(new IdentifierType(id, name));
			}
		}

		return identifierTypes;
	}

	private List<EvidenceCode> parseEvidenceCodes(Element evidenceCodesElement) {
		List<EvidenceCode> evidenceCodes = new LinkedList<>();
		NodeList nodeList = evidenceCodesElement
				.getElementsByTagName(EVIDENCE_CODE);
		for (int i = 0; i < nodeList.getLength(); ++i) {
			Element node = (Element) nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String code = node.getElementsByTagName(EVIDENCE_CODE_CODE)
						.item(0).getTextContent().trim();
				String name = node.getElementsByTagName(EVIDENCE_CODE_NAME)
						.item(0).getTextContent().trim();
				evidenceCodes.add(new EvidenceCode(code, name));
			}
		}

		return evidenceCodes;
	}

	private List<DataSourceMetaInfo> parseDataSources(
			Element dataSourcesElement,
			Map<Integer, DataSourceType> dataSourceTypesMap,
			Map<Integer, IdentifierType> identifierTypesMap) {
		List<DataSourceMetaInfo> dbUpdaterDataSources = new LinkedList<>();

		NodeList nodeList = dataSourcesElement
				.getElementsByTagName(DATA_SOURCE);
		for (int i = 0; i < nodeList.getLength(); ++i) {
			Element node = (Element) nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				DataSourceType dataSourceType = dataSourceTypesMap.get(Integer
						.parseInt(node
								.getElementsByTagName(DATA_SOURCE_TYPE_ID)
								.item(0).getTextContent().trim()));
				IdentifierType identifierType = identifierTypesMap.get(Integer
						.parseInt(node
								.getElementsByTagName(
										DATA_SOURCE_IDENTIFIER_TYPE_ID).item(0)
								.getTextContent().trim()));
				String shortName = node.getElementsByTagName(SHORT_NAME)
						.item(0).getTextContent().trim();
				String displayName = node.getElementsByTagName(DISPLAY_NAME)
						.item(0).getTextContent().trim();
				String url = node.getElementsByTagName(DATA_SOURCE_URL).item(0)
						.getTextContent().trim();
				String versionUrl = node
						.getElementsByTagName(DATA_SOURCE_VERSION_URL).item(0)
						.getTextContent().trim();
				String updaterClass = node
						.getElementsByTagName(DATA_SOURCE_UPDATER).item(0)
						.getTextContent().trim();
				String encoding = node
						.getElementsByTagName(DATA_SOURCE_ENCODING).item(0)
						.getTextContent().trim();
				String organismName = node
						.getElementsByTagName(DATA_SOURCE_ORGANISM).item(0)
						.getTextContent().trim();
				String description = node
						.getElementsByTagName(DATA_SOURCE_DESCRIPTION).item(0)
						.getTextContent().trim();
				boolean isPrimary = node
						.getElementsByTagName(DATA_SOURCE_ISPRIMARY).item(0)
						.getTextContent().trim().equals("1") ? true : false;

				dbUpdaterDataSources.add(new DataSourceMetaInfo(new DataSource(
						dataSourceType, identifierType, shortName, displayName,
						description, null, new Date(), isPrimary), url,
						versionUrl, updaterClass, encoding, organismName));
			}
		}

		return dbUpdaterDataSources;
	}
}
