package edu.mit.scansite.client.ui.view.footer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.LogoutEvent;
import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Konstantin Krismer
 */
public class AdminPageView extends PageView {

	private static AdminPageViewUiBinder uiBinder = GWT
			.create(AdminPageViewUiBinder.class);

	interface AdminPageViewUiBinder extends UiBinder<Widget, AdminPageView> {
	}
	
	@UiField
	Label nameLabel;
	
	@UiField
	Label levelLabel;

	@UiField
	SubmitButton logoutButton;

	public AdminPageView(User user) {
		initWidget(uiBinder.createAndBindUi(this));
		
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				if(user != null) {
					nameLabel.setText(user.getFirstName() + " " + user.getLastName());
					if(user.isAdmin()) {
						DOM.getElementById("adminMainNav").setAttribute("style",
								"display: block;");
						DOM.getElementById("collaboratorMainNav").setAttribute("style",
								"display: none;");
						DOM.getElementById("advancedUserNote").setAttribute("style",
								"display: none;");
						levelLabel.setText("Level 3 - Administrator");
						levelLabel.addStyleName("green");
					} else if(user.isCollaborator()) {
						DOM.getElementById("adminMainNav").setAttribute("style",
								"display: none;");
						DOM.getElementById("collaboratorMainNav").setAttribute("style",
								"display: block;");
						DOM.getElementById("advancedUserNote").setAttribute("style",
								"display: none;");
						levelLabel.setText("Level 2 - Collaborator");
						levelLabel.addStyleName("green");
					} else {
						DOM.getElementById("adminMainNav").setAttribute("style",
								"display: none;");
						DOM.getElementById("collaboratorMainNav").setAttribute("style",
								"display: none;");
						DOM.getElementById("advancedUserNote").setAttribute("style",
								"display: block;");
						levelLabel.setText("Level 1 - Advanced User");
						levelLabel.addStyleName("green");
					}
				}
			}
		});
		
	}

	@UiHandler("logoutButton")
	public void onLogoutButtonClick(ClickEvent event) {
		EventBus.instance().fireEvent(new LogoutEvent());
	}

	@Override
	public String getPageTitle() {
		return "Administrator and Collaborator Area";
	}

	@Override
	public boolean isMajorNavigationPage() {
		return true;
	}

	@Override
	public String getMajorPageId() {
		return getPageId();
	}

	@Override
	public String getPageId() {
		return NavigationEvent.PageId.ADMIN.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.ADMIN;
	}
}
