package edu.mit.scansite.client.ui.widgets.admin;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserCell extends AbstractCell<User> {

	public UserCell() {
	}

	@Override
	public void render(Context context, User value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.appendHtmlConstant("<p><strong>");
			sb.appendHtmlConstant(value.getFirstName() + " "
					+ value.getLastName());
			sb.appendHtmlConstant("</strong><br />");
			sb.appendHtmlConstant(value.getEmail());
			sb.appendHtmlConstant(" | ");
			if (value.isSuperAdmin()) {
				sb.appendHtmlConstant("Super Administrator");
			} else if (value.isAdmin()) {
				sb.appendHtmlConstant("Administrator");
			} else {
				sb.appendHtmlConstant("Collaborator");
			}
			sb.appendHtmlConstant("</p>");
		}
	}
}