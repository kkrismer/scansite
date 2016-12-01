package edu.mit.scansite.server.dispatch.handler.features;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dispatch.BootstrapListener;
import edu.mit.scansite.shared.dispatch.features.ShowSequenceMatchesHtmlGetAction;
import edu.mit.scansite.shared.dispatch.features.ShowSequenceMatchesHtmlGetResult;
import edu.mit.scansite.shared.transferobjects.Protein;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ShowSequenceMatchesHtmlGetHandler
		implements
		ActionHandler<ShowSequenceMatchesHtmlGetAction, ShowSequenceMatchesHtmlGetResult> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Provider<ServletContext> contextProvider;

	@Inject
	public ShowSequenceMatchesHtmlGetHandler(
			final Provider<ServletContext> contextProvider) {
		this.contextProvider = contextProvider;
	}

	@Override
	public Class<ShowSequenceMatchesHtmlGetAction> getActionType() {
		return ShowSequenceMatchesHtmlGetAction.class;
	}

	@Override
	public ShowSequenceMatchesHtmlGetResult execute(
			ShowSequenceMatchesHtmlGetAction action, ExecutionContext context)
			throws DispatchException {
		SafeHtmlBuilder html = new SafeHtmlBuilder();
		Protein protein = action.getProtein();
		try {
			if (protein.getIdentifier() != null
					&& protein.getDataSource() != null) {
				protein = ServiceLocator
						.getInstance()
						.getDaoFactory(
								BootstrapListener
										.getDbConnector(contextProvider.get()))
						.getProteinDao()
						.get(protein.getIdentifier(),
								protein.getDataSource());
			}

			html.appendHtmlConstant("<h3>")
					.appendEscaped(
							"Matches of pattern " + action.getSequenceRegex()
									+ " in protein " + protein.toString())
					.appendHtmlConstant("</h3>\n");

			Pattern regex = Pattern.compile(action.getSequenceRegex(),
					Pattern.CASE_INSENSITIVE | Pattern.DOTALL
							| Pattern.MULTILINE);
			Matcher matcher = regex.matcher(protein.getSequence());
			int count = 0;
			while (matcher.find()) {
				html.appendHtmlConstant("<table>\n<tr>\n<td>\n<b>");
				html.append(++count);
				html.appendHtmlConstant(":</b></td>\n<td>\n");
				html.appendHtmlConstant(getSequenceHtml(protein.getSequence(),
						matcher.start(), matcher.end()));
				html.appendHtmlConstant("</td>\n</tr>\n</table>\n");
			}

			return new ShowSequenceMatchesHtmlGetResult(html.toSafeHtml(),
					action.getTopPosition(), action.getLeftPosition());
		} catch (Exception e) {
			logger.error("Error creating match-html for regEx. " + e.toString());
			throw new ActionException(e);
		}
	}

	@Override
	public void rollback(ShowSequenceMatchesHtmlGetAction action,
			ShowSequenceMatchesHtmlGetResult result, ExecutionContext context)
			throws DispatchException {
	}

	private String getSequenceHtml(String sequence, int highlightFromInclusive,
			int highlightToIndexExclusive) {
		StringBuilder html = new StringBuilder();
		html.append("<pre>\n");
		for (int i = 0; i < (int) Math.log10(sequence.length()); ++i) {
			html.append(' ');
		}
		html.append("<b>1</b> ");
		for (int pos = 0; pos < sequence.length(); ++pos) {
			if (pos >= highlightFromInclusive
					&& pos < highlightToIndexExclusive) {
				html.append("<span class=\"phosphoSite\">")
						.append(sequence.charAt(pos)).append("</span>");
			} else {
				html.append(sequence.charAt(pos));
			}

			if ((pos + 1) % 10 == 0) {
				html.append(' ');
			}
			if ((pos + 1) % 100 == 0 && (pos + 1) < sequence.length()) {
				html.append('\n');
				int num = pos + 2; // +1 for starting at index zero, +1 for
									// printing
									// next index
				for (int i = (int) Math.log10(num); i < (int) Math
						.log10(sequence.length()); ++i) {
					html.append(' ');
				}
				html.append("<b>").append(num).append("</b> ");
			}
		}
		html.append("</pre>\n");
		return html.toString();
	}

}
