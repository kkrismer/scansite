package edu.mit.scansite.server.dispatch.handler.features;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.URIs;
import edu.mit.scansite.shared.dispatch.features.SiteInSequenceHtmlGetAction;
import edu.mit.scansite.shared.dispatch.features.SiteInSequenceHtmlGetResult;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class SiteInSequenceHtmlGetHandler implements
		ActionHandler<SiteInSequenceHtmlGetAction, SiteInSequenceHtmlGetResult> {

	@Override
	public Class<SiteInSequenceHtmlGetAction> getActionType() {
		return SiteInSequenceHtmlGetAction.class;
	}

	@Override
	public SiteInSequenceHtmlGetResult execute(
			SiteInSequenceHtmlGetAction action, ExecutionContext context)
			throws DispatchException {
		SafeHtmlBuilder html = new SafeHtmlBuilder();
		ScanResultSite site = action.getSite();
		String sequence = site.getProtein().getSequence();
		int sitePos = site.getPosition();
		String title = "Location of site " + site.getSite() + " in protein "
				+ site.getProtein().getIdentifier();
		html.appendHtmlConstant("<h3>").appendEscaped(title)
				.appendHtmlConstant("</h3>\n");
		html.appendHtmlConstant("<table>\n<tr>\n<td>\n<pre>\n");
		for (int i = 0; i < (int) Math.log10(sequence.length()); ++i) {
			html.appendHtmlConstant(" ");
		}
		html.appendHtmlConstant("<b>1</b> ");
		for (int pos = 0; pos < sequence.length(); ++pos) {
			if (pos == sitePos) {
				html.appendHtmlConstant(
						"<span class=\"phosphoSite\" style=\"text-decoration: underline\">")
						.append(sequence.charAt(pos))
						.appendHtmlConstant("</span>");
			} else if ((pos >= sitePos - ScansiteConstants.HALF_WINDOW)
					&& (pos <= sitePos + ScansiteConstants.HALF_WINDOW)) {
				html.appendHtmlConstant("<span class=\"phosphoSite\">")
						.append(sequence.charAt(pos))
						.appendHtmlConstant("</span>");
			} else {
				html.append(sequence.charAt(pos));
			}

			if ((pos + 1) % 10 == 0) {
				html.appendHtmlConstant(" ");
			}
			if ((pos + 1) % 100 == 0 && (pos + 1) < sequence.length()) {
				html.appendHtmlConstant("\n");
				int num = pos + 2; // +1 for starting at index zero, +1 for
									// printing next index
				for (int i = (int) Math.log10(num); i < (int) Math
						.log10(sequence.length()); ++i) {
					html.appendHtmlConstant(" ");
				}
				html.appendHtmlConstant("<b>").append(num)
						.appendHtmlConstant("</b> ");
			}
		}
		html.appendHtmlConstant("</pre>\n</td>\n</tr>\n</table>\n");
		html.appendHtmlConstant("<a href=\"")
				.appendEscaped(URIs.getDirectBlastLink(site.getSiteSequence()))
				.appendHtmlConstant(
						"\" target=\"_blank\">Run Protein BLAST on this site sequence</a>");
		return new SiteInSequenceHtmlGetResult(html.toSafeHtml(),
				action.getTopPosition(), action.getLeftPosition());
	}

	@Override
	public void rollback(SiteInSequenceHtmlGetAction action,
			SiteInSequenceHtmlGetResult result, ExecutionContext context)
			throws DispatchException {
	}
}
