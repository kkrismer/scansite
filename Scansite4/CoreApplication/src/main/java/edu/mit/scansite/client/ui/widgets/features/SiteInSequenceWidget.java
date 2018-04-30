package edu.mit.scansite.client.ui.widgets.features;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.URIs;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.DomainPosition;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SiteInSequenceWidget extends ScansiteWidget {
	private FlowPanel mainPanel = new FlowPanel();

	private ScanResultSite site;
	private LightWeightProtein protein;

	private Integer position = null;
	private AminoAcid relativeAa = null;
	private AminoAcid centerAa = null;
	private ArrayList<DomainPosition> domainPositions = null;

	private SiteInSequenceWidget(ScanResultSite site,
			LightWeightProtein protein, AminoAcid aa, AminoAcid centerAa,
			Integer position, ArrayList<DomainPosition> domainPositions) {
		initWidget(mainPanel);
		this.site = site;
		this.protein = protein;
		this.relativeAa = aa;
		this.centerAa = centerAa;
		this.position = position;
		this.domainPositions = domainPositions;
		init();
	}

	/**
	 * Displays a site within a protein.
	 * 
	 * @param site
	 *            A site as returned by protein scans.
	 */
	public SiteInSequenceWidget(ScanResultSite site) {
		this(site, site.getProtein(), null, null, null, null);
	}

	/**
	 * Displays a protein sequence.
	 * 
	 * @param proteinName
	 *            Protein name / accession / ID.
	 * @param sequence
	 *            Protein sequence.
	 */
	public SiteInSequenceWidget(LightWeightProtein protein) {
		this(null, protein, null, null, null, null);
	}

	/**
	 * Displays a protein's amino acid composition for a the given aminoacid aa
	 * relative to the given center amino acid.
	 * 
	 * @param proteinName
	 *            Protein name / accession / ID.
	 * @param sequence
	 *            Protein sequence.
	 * @param aa
	 *            relative amino acid.
	 * @param centerAa
	 *            center amino acid.
	 * @param position
	 *            The position of the relative aminoAcid in relation to the
	 *            center amino acid [-7,+7] (or the position in the whole window
	 *            [0,14]).
	 * @param centerIsZeroPosition
	 *            TRUE if the position is from range [-7,+7], FALSE if it is
	 *            from range [0,14].
	 */
	public SiteInSequenceWidget(LightWeightProtein protein, AminoAcid aa,
			AminoAcid centerAa, int position, boolean centerIsZeroPosition) {
		this(null, protein, aa, centerAa, (centerIsZeroPosition) ? position
				: position - ScansiteConstants.HALF_WINDOW, null);
	}

	/**
	 * Displays a protein's amino acid composition for a the given aminoacid aa
	 * relative to the given center amino acid.
	 * 
	 * @param proteinName
	 *            Protein name / accession / ID.
	 * @param sequence
	 *            Protein sequence.
	 * @param aa
	 *            relative amino acid.
	 * @param centerAa
	 *            center amino acid.
	 * @param position
	 *            The position of the relative aminoAcid in relation to the
	 *            center amino acid [-7,+7] (or the position in the whole window
	 *            [0,14]).
	 * @param centerIsZeroPosition
	 *            TRUE if the position is from range [-7,+7], FALSE if it is
	 *            from range [0,14].
	 * @param domainPositions
	 *            A list of domainPositions, or null if no domains need to be
	 *            highlighted.
	 */
	public SiteInSequenceWidget(LightWeightProtein protein, AminoAcid aa,
			AminoAcid centerAa, int position, boolean centerIsZeroPosition,
			ArrayList<DomainPosition> domainPositions) {
		this(null, protein, aa, centerAa, (centerIsZeroPosition) ? position
				: position - ScansiteConstants.HALF_WINDOW, domainPositions);
	}

	private void init() {
		HTML htmlWidget = new HTML();

		StringBuilder html = new StringBuilder();
		StringBuilder highlightedSites = new StringBuilder();
		int nHighlightedSites = 0;
		int sitePos = (site == null) ? -1 : site.getPosition();
		html.append("<pre>");
		boolean domainPos = false;
		for (int i = 0; i < (int) Math.log10(protein.getSequence().length()); ++i) { // print
			// whitespace
			html.append(' ');
		}
		html.append("<b>1</b> ");
		for (int seqPos = 0; seqPos < protein.getSequence().length(); ++seqPos) {
			if (position == null) { // print sequence or site
				if (sitePos >= 0 && seqPos == sitePos) {
					highlightedSites
							.append(protein.getSequence().charAt(seqPos))
							.append(seqPos + 1).append("; ");
					nHighlightedSites++;
					html.append("<font color='blue'><b><u>")
							.append(protein.getSequence().charAt(seqPos))
							.append("</u></b></font>");
				} else if (sitePos >= 0
						&& (seqPos >= sitePos - ScansiteConstants.HALF_WINDOW)
						&& (seqPos <= sitePos + ScansiteConstants.HALF_WINDOW)) {
					html.append("<font color='blue'>")
							.append(protein.getSequence().charAt(seqPos))
							.append("</font>");
				} else {
					html.append(protein.getSequence().charAt(seqPos));
				}
			} else { // print amino acid composition in sequence
				int relPosCenterAa = seqPos + position;
				int relPosRelAa = seqPos + ((-1) * position);
				DomainPosition domain = getDomainPositionName(seqPos + 1);
				String highlightCenterAaColor = "#1F0AF5";
				String highlightRelativeAaColor = "#F57D0A";
				if (domain != null) {
					String domainColor = !domain.isColorSet() ? "#0AF5DB"
							: "rgb(" + String.valueOf(domain.getColorR()) + ","
									+ String.valueOf(domain.getColorG()) + ","
									+ String.valueOf(domain.getColorB()) + ")";
					html.append(
							"<span title='" + domain.getName()
									+ "'style='background-color: ")
							.append(domainColor).append(";'>");
					highlightCenterAaColor = "#1E90FF";
					highlightRelativeAaColor = "#DCDCDC";
					domainPos = true;
				}
				if (relPosCenterAa >= 0
						&& relPosCenterAa < protein.getSequence().length()
						&& protein.getSequence().charAt(seqPos) == centerAa
								.getOneLetterCode()
						&& protein.getSequence().charAt(relPosCenterAa) == relativeAa
								.getOneLetterCode()) {
					highlightedSites
							.append(protein.getSequence().charAt(seqPos))
							.append(seqPos + 1).append("; ");
					nHighlightedSites++;
					html.append("<font color='").append(highlightCenterAaColor)
							.append("'><b><u>")
							.append(protein.getSequence().charAt(seqPos))
							.append("</u></b></font>");
				} else if (relPosRelAa >= 0
						&& relPosRelAa < protein.getSequence().length()
						&& protein.getSequence().charAt(seqPos) == relativeAa
								.getOneLetterCode()
						&& protein.getSequence().charAt(relPosRelAa) == centerAa
								.getOneLetterCode()) {
					html.append("<font color='")
							.append(highlightRelativeAaColor)
							.append("'><b><u>")
							.append(protein.getSequence().charAt(seqPos))
							.append("</u></b></font>");
				} else {
					html.append(protein.getSequence().charAt(seqPos));
				}
			}

			if ((seqPos + 1) % 10 == 0 && (seqPos + 1) % 50 != 0) {
				html.append(' ');
			}
			if (domainPos) {
				html.append("</span>");
				domainPos = false;
			}
			if ((seqPos + 1) % 50 == 0
					&& (seqPos + 1) < protein.getSequence().length()) {
				html.append('\n');
				int num = seqPos + 2; // +1 for starting at index zero, +1 for
										// printing next index
				for (int i = (int) Math.log10(num); i < (int) Math
						.log10(protein.getSequence().length()); ++i) { // print
																		// whitespace
					html.append(' ');
				}
				html.append("<b>").append(num).append("</b> ");
			}
		}
		html.append("</pre>");
		htmlWidget.setHTML(html.toString());
		HTML titleLabel;
		if (site != null) {
			titleLabel = new HTML("<h3>Location of site <emph>"
					+ site.getSite() + "</emph> in protein <emph>"
					+ protein.getIdentifier() + "</emph></h3>");
		} else if (position != null) {
			titleLabel = new HTML("<h4>Composition of <emph>"
					+ relativeAa.getFullName() + "s</emph>, "
					+ String.valueOf(position) + " residues from <emph>"
					+ centerAa.getFullName() + "s</emph> in protein <emph>"
					+ protein.getIdentifier() + "</emph></h4>");
		} else {
			titleLabel = new HTML("<h4>Sequence of protein <emph>"
					+ protein.getIdentifier() + "</emph></h4>");
		}
		mainPanel.add(titleLabel);
		mainPanel.add(htmlWidget);
		if (nHighlightedSites > 0) {
			mainPanel.add(new HTML("<b>Highlighted Site"
					+ (nHighlightedSites > 1 ? "s" : "") + "</b>:<br />"
					+ highlightedSites.toString()));
		}
		if (domainPositions != null && !domainPositions.isEmpty()) {
			mainPanel
					.add(new Label(
							"Tip: Mouse-over regions with background-color to see the highlighted domains' names."));
		}

		if (site != null) {
			Anchor blastAnchor = new Anchor("BLAST this site!", false,
					URIs.getDirectBlastLink(site.getSiteSequence()), "_blank");
			blastAnchor.addStyleName("blastLink");
			mainPanel.add(blastAnchor);
		}
	}

	private DomainPosition getDomainPositionName(int seqPos) {
		if (domainPositions != null && !domainPositions.isEmpty()) {
			DomainPosition dp = null;
			for (DomainPosition pos : domainPositions) {
				if (pos.getFrom() <= seqPos && seqPos <= pos.getTo()) {
					dp = pos;
				}
			}
			return dp;
		}
		return null;
	}
}
