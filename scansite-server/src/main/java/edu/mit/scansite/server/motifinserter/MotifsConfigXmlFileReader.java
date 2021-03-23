package edu.mit.scansite.server.motifinserter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

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

import edu.mit.scansite.server.updater.ScansiteUpdaterException;
import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.IdentifierType;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;

/**
 * @author Konstantin Krismer
 */
public class MotifsConfigXmlFileReader {
	private static final String MOTIFS = "motifs";
	private static final String MOTIF = "motif";
	private static final String MOTIF_SHORT_NAME = "shortName";
	private static final String MOTIF_DISPLAY_NAME = "displayName";
	private static final String MOTIF_GROUP_SHORT_NAME = "groupShortName";
	private static final String MOTIF_GROUP_DISPLAY_NAME = "groupDisplayName";
	private static final String MOTIF_CLASS = "motifClass";
	private static final String MOTIF_IS_PUBLIC = "isPublic";
	private static final String IDENTIFIERS = "identifiers";
	private static final String IDENTIFIER = "identifier";
	private static final String IDENTIFIER_NAME = "name";
	private static final String IDENTIFIER_TYPE_ID = "typeId";

	public List<Motif> readConfig(String configFile)
			throws ScansiteUpdaterException {
		try {
			InputStream in = new FileInputStream(configFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			dBuilder.setEntityResolver(new EntityResolver() {
				@Override
				public InputSource resolveEntity(String publicId,
						String systemId) throws SAXException, IOException {
					if (systemId.contains("Motifs.dtd")) {
						return new InputSource(MotifsConfigXmlFileReader.class
								.getResourceAsStream("/Motifs.dtd"));
					} else {
						return null;
					}
				}
			});

			Document document = dBuilder.parse(in);
			document.getDocumentElement().normalize();

			Element motifsElement = (Element) document
					.getElementsByTagName(MOTIFS).item(0);
			List<Motif> motifs = new LinkedList<>();

			NodeList nodeList = motifsElement.getElementsByTagName(MOTIF);
			for (int i = 0; i < nodeList.getLength(); ++i) {
				Motif motif = new Motif();
				Element node = (Element) nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					motif.setShortName(node
							.getElementsByTagName(MOTIF_SHORT_NAME).item(0)
							.getTextContent().trim());
					motif.setDisplayName(node
							.getElementsByTagName(MOTIF_DISPLAY_NAME).item(0)
							.getTextContent().trim());
					String motifGroupShortName = node
							.getElementsByTagName(MOTIF_GROUP_SHORT_NAME)
							.item(0).getTextContent().trim();
					String motifGroupDisplayName = node
							.getElementsByTagName(MOTIF_GROUP_DISPLAY_NAME)
							.item(0).getTextContent().trim();
					motif.setGroup(new LightWeightMotifGroup(
							motifGroupDisplayName, motifGroupShortName));
					motif.setMotifClass(MotifClass.getDbValue(node
							.getElementsByTagName(MOTIF_CLASS).item(0)
							.getTextContent().trim()));
					motif.setPublic(node.getElementsByTagName(MOTIF_IS_PUBLIC)
							.item(0).getTextContent().trim().equals("1") ? true : false);

					NodeList identifiersList = ((Element) node
							.getElementsByTagName(IDENTIFIERS).item(0))
							.getElementsByTagName(IDENTIFIER);
					for (int j = 0; j < identifiersList.getLength(); ++j) {
						Element identifierNode = (Element) identifiersList
								.item(j);
						if (identifierNode.getNodeType() == Node.ELEMENT_NODE) {
							String identifierName = identifierNode
									.getElementsByTagName(IDENTIFIER_NAME)
									.item(0).getTextContent().trim();
							int identifierTypeId = Integer
									.parseInt(identifierNode
											.getElementsByTagName(
													IDENTIFIER_TYPE_ID).item(0)
											.getTextContent().trim());
							motif.getIdentifiers().add(
									new Identifier(identifierName,
											new IdentifierType(
													identifierTypeId, null)));
						}
					}
					motifs.add(motif);
				}
			}

			return motifs;
		} catch (FileNotFoundException e) {
			throw new ScansiteUpdaterException("Motifs-Configfile ("
					+ configFile + ") not found.", e);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new ScansiteUpdaterException("Motifs-Configfile ("
					+ configFile + ") can not be read.", e);
		}
	}
}
