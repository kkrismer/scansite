package edu.mit.scansite.client.ui.widgets.admin;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import edu.mit.scansite.shared.transferobjects.LightWeightMotif;

/**
 * @author Konstantin Krismer
 */
public class LightWeightMotifCell extends AbstractCell<LightWeightMotif> {
	private static final int MAX_DISPLAY_NAME = 45;

	public LightWeightMotifCell() {
	}

	@Override
	public void render(Context context, LightWeightMotif value,
			SafeHtmlBuilder sb) {
		if (value != null) {
			sb.appendHtmlConstant("<p><strong>");
			sb.appendEscaped(value.getShortName());
			sb.appendHtmlConstant("</strong>");
			sb.appendEscaped(" | "
					+ ((value.getDisplayName().length() > MAX_DISPLAY_NAME) ? value
							.getDisplayName().substring(0, MAX_DISPLAY_NAME)
							+ "..." : value.getDisplayName()));
			sb.appendHtmlConstant("</p>");
		}
	}

}
