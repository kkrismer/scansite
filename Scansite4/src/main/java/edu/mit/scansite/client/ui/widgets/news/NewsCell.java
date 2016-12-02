package edu.mit.scansite.client.ui.widgets.news;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import edu.mit.scansite.shared.transferobjects.NewsEntry;

/**
 * @author Konstantin Krismer
 */
public class NewsCell extends AbstractCell<NewsEntry> {

	public NewsCell() {
	}

	@Override
	public void render(Context context, NewsEntry value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.appendHtmlConstant("<div class=\"newsentry\">");
			sb.appendHtmlConstant("<h3>");
			sb.appendHtmlConstant(value.getTitle());
			sb.appendHtmlConstant("</h3>");
			sb.appendHtmlConstant("<p class=\"metainfo\">");
			sb.appendHtmlConstant(value.getUser().getFirstName() + " "
					+ value.getUser().getLastName());
			sb.appendHtmlConstant(" | ");
			sb.appendHtmlConstant(value.getDate().toString());
			sb.appendHtmlConstant("</p>");
			sb.appendHtmlConstant("<p>");
			sb.appendHtmlConstant(value.getText());
			sb.appendHtmlConstant("</p>");
			sb.appendHtmlConstant("</div>");
		}
	}
}
