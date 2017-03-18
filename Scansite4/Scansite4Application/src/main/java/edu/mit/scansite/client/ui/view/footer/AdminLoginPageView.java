package edu.mit.scansite.client.ui.view.footer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.LoginEvent;
import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.shared.Breadcrumbs;

/**
 * @author Konstantin Krismer
 */
public class AdminLoginPageView extends PageView {
	interface AdminLoginPageViewUiBinder extends
			UiBinder<Widget, AdminLoginPageView> {
	}

	private static AdminLoginPageViewUiBinder uiBinder = GWT
			.create(AdminLoginPageViewUiBinder.class);


	@UiField
	TextBox emailAddressTextBox;

	@UiField
	PasswordTextBox passwordTextBox;
	
	@UiField
	SubmitButton loginButton;
	
	public AdminLoginPageView() {
		initWidget(uiBinder.createAndBindUi(this));

		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				emailAddressTextBox.getElement().setId("emailAddressTextBoxId");
				passwordTextBox.getElement().setId("passwordTextBoxId");
			}
		});
	}
	
	@UiHandler("loginButton")
	public void onLoginButtonClick(ClickEvent event) {
		if(emailAddressTextBox.getText().isEmpty() || passwordTextBox.getText().isEmpty()) {
			showWarningMessage("E-mail or password were empty");
		} else {
			hideMessage();
			EventBus.instance().fireEvent(new LoginEvent(emailAddressTextBox.getText(), passwordTextBox.getText()));
		}
	}

	@Override
	public String getPageTitle() {
		return "Administrator and Collaborator Area";
	}

	@Override
	public boolean isMajorNavigationPage() {
		return false;
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
