package edu.mit.scansite.client.ui.widgets.features;

import java.util.List;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.URIs;
import edu.mit.scansite.shared.transferobjects.DomainPosition;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class DomainInformationWidget extends ScansiteWidget {
	private VerticalPanel mainPanel = new VerticalPanel();

	public DomainInformationWidget(LightWeightProtein protein,
			List<DomainPosition> domainPositions) {
		initWidget(mainPanel);
		StringBuilder html = new StringBuilder();
		html.append("<b>Predicted PFAM-Domains (from InterProScan): </b> ");
		boolean first = true;
		for (DomainPosition pos : domainPositions) {
			if (first) {
				first = false;
			} else {
				html.append(", ");
			}
			html.append("<a href=\'")
					.append(URIs.directPfamLink(pos.getDomainId()))
					.append("\' target=\'_blank\'>");
			html.append(pos.getName());
			html.append("</a>  (");
			html.append(pos.getFrom()).append(" - ").append(pos.getTo())
					.append(')');
		}
		html.append("<br/>")
				.append("Note: The domains' positions are retrieved from InterProScan, therefore the numbers may differ slightly from PFAM-retrieved domains.");
		if (protein.getDataSource() != null
				&& protein.getDataSource().getShortName()
						.equalsIgnoreCase("swissprot")) {
			html.append("<br/> Go to <a href='"
					+ URIs.directPfamLinkByProteinAccession(protein.getIdentifier())
					+ "' target='_blank'>PFAM</a>.");
		}
		mainPanel.add(new HTML(html.toString()));
		mainPanel.setWidth("100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	}
}
