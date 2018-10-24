package edu.mit.scansite.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Konstantin Krismer
 */
public class NavigationBarView extends Composite implements View {
	interface NavigationBarViewUiBinder extends
			UiBinder<Widget, NavigationBarView> {
	}

	private static NavigationBarViewUiBinder uiBinder = GWT
			.create(NavigationBarViewUiBinder.class);

	public NavigationBarView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void showAdminArea(User user) {
		if (user != null) {
			DOM.getElementById("adminLink").setAttribute("style",
					"display: block;");
			
			if (user.isAdmin() || user.isSuperAdmin()) {
				DOM.getElementById("adminSubnav").setAttribute("style",
						"display: block;");
				DOM.getElementById("motifMgmtLink").setAttribute("style",
						"display: block;");
				DOM.getElementById("motifGroupMgmtLink").setAttribute("style",
						"display: block;");
				DOM.getElementById("newsMgmtLink").setAttribute("style",
						"display: block;");
				if (user.isSuperAdmin()) {
					DOM.getElementById("userMgmtLink").setAttribute("style",
							"display: block;");
				}
			}
		} else {
			DOM.getElementById("adminLink").setAttribute("style",
					"display: none;");
			DOM.getElementById("adminSubnav").setAttribute("style",
					"display: none;");
			DOM.getElementById("motifMgmtLink").setAttribute("style",
					"display: none;");
			DOM.getElementById("motifGroupMgmtLink").setAttribute("style",
					"display: none;");
			DOM.getElementById("newsMgmtLink").setAttribute("style",
					"display: none;");
			DOM.getElementById("userMgmtLink").setAttribute("style",
					"display: none;");
		}
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	public void select(String majorPageId) {
		NodeList<Element> listItems = Document.get().getElementsByTagName("li");
		Element listItem = null;
		for (int i = 0; i < listItems.getLength(); ++i) {
			listItem = listItems.getItem(i);
			if (!listItem.getId().isEmpty()) {
				if (listItem.getId().equalsIgnoreCase(majorPageId + "Link")) {
					listItem.setClassName("sel");
				} else {
					if (!listItem.getId().equalsIgnoreCase("adminLink")) {
						listItem.setClassName("nosel");
					} else {
						listItem.setClassName("");
					}
				}
			}
		}
	}
}
