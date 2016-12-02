package edu.mit.scansite.server.dispatch.handler.features;

import java.text.DecimalFormat;
import java.util.List;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import edu.mit.scansite.shared.dispatch.features.ShowMotifsForExpectedSiteGetAction;
import edu.mit.scansite.shared.dispatch.features.ShowMotifsForExpectedSiteGetResult;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import edu.mit.scansite.shared.transferobjects.SequencePattern;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ShowMotifsForExpectedSiteGetHandler
		implements
		ActionHandler<ShowMotifsForExpectedSiteGetAction, ShowMotifsForExpectedSiteGetResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ShowMotifsForExpectedSiteGetResult execute(
			ShowMotifsForExpectedSiteGetAction action, ExecutionContext context)
			throws DispatchException {
		SafeHtmlBuilder html = new SafeHtmlBuilder();

		List<ScanResultSite> hits = action.getHits();
		try {
			String title;
			if (hits.size() == 1) {
				title = "One motif found for expected phosphorylation sites in "
						+ hits.get(0).getProtein().getIdentifier();
			} else {
				title = hits.size()
						+ " motifs found for expected phosphorylation sites in "
						+ hits.get(0).getProtein().getIdentifier();
			}
			html.appendHtmlConstant("<h3>").appendEscaped(title)
					.appendHtmlConstant("</h3>\n");
			html.appendHtmlConstant("<p><span style=\"font-weight: bold\">Sequence patterns of this query (expected phosphorylation sites are colored in blue):</span><br />");
			for (SequencePattern pattern : action.getPatterns()) {
				html.appendHtmlConstant(" - ")
						.appendHtmlConstant(pattern.getHtmlFormattedRegEx())
						.appendHtmlConstant("<br />");
			}
			html.appendHtmlConstant("</p>\n");

			for (ScanResultSite hit : hits) {
				html.appendHtmlConstant(getHtmlForHit(hit));
			}

			return new ShowMotifsForExpectedSiteGetResult(html.toSafeHtml(),
					action.getTopPosition(), action.getLeftPosition());
		} catch (Exception e) {
			logger.error("Error creating motifs-for-expected-site-html: "
					+ e.getMessage());
			throw new ActionException(e);
		}
	}

	@Override
	public Class<ShowMotifsForExpectedSiteGetAction> getActionType() {
		return ShowMotifsForExpectedSiteGetAction.class;
	}

	@Override
	public void rollback(ShowMotifsForExpectedSiteGetAction arg0,
			ShowMotifsForExpectedSiteGetResult arg1, ExecutionContext arg2)
			throws DispatchException {
	}

	private String getHtmlForHit(ScanResultSite hit) {
		StringBuilder html = new StringBuilder();
		DecimalFormat score = new DecimalFormat("0.000");
		DecimalFormat percentile = new DecimalFormat("0.000%");
		DecimalFormat surfaceAccess = new DecimalFormat("0.0000");
		html.append("<div class=\"motif\">\n");
		html.append("<h2>").append(hit.getMotif().getShortName())
				.append("</h2>\n");
		html.append("<table>\n<tr>\n<td>Score:</td><td>")
				.append(score.format(hit.getScore())).append("</td>\n</tr>\n");
		html.append("<tr>\n<td>Percentile:</td><td>")
				.append(percentile.format(hit.getPercentile()))
				.append("</td>\n</tr>\n");
		html.append("<tr>\n<td>Motif:</td><td>")
				.append(hit.getMotif().getDisplayName()).append(" (")
				.append(hit.getMotif().getShortName()).append(')')
				.append("</td>\n</tr>\n");
		html.append("<tr>\n<td>Motif group:</td><td>")
				.append(hit.getMotif().getGroup().getDisplayName())
				.append(" (").append(hit.getMotif().getGroup().getShortName())
				.append(')').append("</td>\n</tr>\n");
		html.append("<tr>\n<td>Site:</td><td>").append(hit.getSite())
				.append("</td>\n</tr>\n");
		html.append("<tr>\n<td>Site sequence:</td><td>")
				.append(hit.getSiteSequence()).append("</td>\n</tr>\n");
		html.append("<tr>\n<td>Surface accessibility:</td><td>")
				.append(surfaceAccess.format(hit.getSurfaceAccessValue()))
				.append("</td>\n</tr>\n");
		html.append("</table>\n</div>\n");

		return html.toString();
	}
}
