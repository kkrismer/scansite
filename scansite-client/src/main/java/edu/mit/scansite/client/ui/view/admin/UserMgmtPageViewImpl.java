package edu.mit.scansite.client.ui.view.admin;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.mit.scansite.client.ui.widgets.admin.UserCell;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.event.NavigationEvent;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.User.UserGroup;
import edu.mit.scansite.shared.util.Formatter;
import edu.mit.scansite.shared.util.Validator;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserMgmtPageViewImpl extends UserMgmtPageView {

	private static UserMgmtPageViewImplUiBinder uiBinder = GWT.create(UserMgmtPageViewImplUiBinder.class);

	interface UserMgmtPageViewImplUiBinder extends UiBinder<Widget, UserMgmtPageViewImpl> {
	}

	private Presenter presenter;
	private User user;
	private List<User> users;

	@UiField(provided = true)
	CellList<User> usersCellList;

	@UiField
	Label legendLabel;

	@UiField
	TextBox emailTextBox;

	@UiField
	TextBox firstNameTextBox;

	@UiField
	TextBox lastNameTextBox;

	@UiField
	PasswordTextBox passwordTextBox;

	@UiField
	PasswordTextBox repeatPasswordTextBox;

	@UiField
	RadioButton advancedUserRadioButton;

	@UiField
	RadioButton collaboratorRadioButton;

	@UiField
	RadioButton adminRadioButton;

	@UiField
	SubmitButton addButton;

	@UiField
	SubmitButton updateButton;

	@UiField
	SubmitButton deleteButton;

	public UserMgmtPageViewImpl(User user) {
		this.user = user;
		applyUserPrivileges();

		UserCell userCell = new UserCell(); // TODO update UserCell
		usersCellList = new CellList<User>(userCell);
		initWidget(uiBinder.createAndBindUi(this));

		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				emailTextBox.getElement().setId("emailTextBoxId");
				firstNameTextBox.getElement().setId("firstNameTextBoxId");
				lastNameTextBox.getElement().setId("lastNameTextBoxId");
				passwordTextBox.getElement().setId("passwordTextBoxId");
				repeatPasswordTextBox.getElement().setId("repeatPasswordTextBoxId");

				final SingleSelectionModel<User> selectionModel = new SingleSelectionModel<User>();
				usersCellList.setSelectionModel(selectionModel);
				selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						emailTextBox.setValue(selectionModel.getSelectedObject().getEmail());
						firstNameTextBox.setValue(selectionModel.getSelectedObject().getFirstName());
						lastNameTextBox.setValue(selectionModel.getSelectedObject().getLastName());

						passwordTextBox.setValue("");
						repeatPasswordTextBox.setValue("");

						if (selectionModel.getSelectedObject().isAdmin()) {
							adminRadioButton.setValue(true);
						} else if (selectionModel.getSelectedObject().isCollaborator()) {
							collaboratorRadioButton.setValue(true);
						} else {
							advancedUserRadioButton.setValue(true);
						}

						legendLabel.setText("Edit user profile");
						updateButton.setVisible(true);
						deleteButton.setVisible(true);
						addButton.setVisible(false);
						hideMessage();
					}
				});
			}
		});
	}

	private void applyUserPrivileges() {
		if (user == null || (!user.isCollaborator() && !user.isAdmin())) {
			History.newItem(NavigationEvent.PageId.ADMIN.getId(), true);
		}
	}

	private boolean validateInput() {
		Validator validator = new Validator();
		return validator.validateEmail(emailTextBox.getValue())
				&& validator.validateGivenString(firstNameTextBox.getValue())
				&& validator.validateGivenString(lastNameTextBox.getValue());
	}

	private boolean isUniqueEmail(String email) {
		if (users != null) {
			for (User user : users) {
				if (user.getEmail().equalsIgnoreCase(email)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@UiHandler("addButton")
	public void onAddButtonClick(ClickEvent event) {
		if (validateInput()) {
			if (isUniqueEmail(emailTextBox.getValue())) {
				if (passwordTextBox.getValue().equals(repeatPasswordTextBox.getValue())) {
					hideMessage();

					User user = new User();
					user.setEmail(emailTextBox.getValue().toLowerCase());
					user.setFirstName(firstNameTextBox.getValue());
					user.setLastName(lastNameTextBox.getValue());
					Formatter f = new Formatter();
					user.setPassword(f.formatPassword(passwordTextBox.getValue()));
					if (adminRadioButton.getValue()) {
						user.setUserGroup(UserGroup.ADMIN);
					} else if (collaboratorRadioButton.getValue()) {
						user.setUserGroup(UserGroup.COLLABORATOR);
					} else {
						user.setUserGroup(UserGroup.ADVANCEDUSER);
					}

					presenter.onAddButtonClicked(user);
				} else {
					showWarningMessage("Passwords don't match");
				}
			} else {
				showWarningMessage("E-mail address already in use");
			}
		} else {
			showWarningMessage("Input validation failed");
		}
	}

	@UiHandler("updateButton")
	public void onUpdateButtonClick(ClickEvent event) {
		if (validateInput()) {
			if ((!passwordTextBox.getValue().isEmpty()
					&& passwordTextBox.getValue().equals(repeatPasswordTextBox.getValue()))
					|| passwordTextBox.getValue().isEmpty()) {

				hideMessage();

				User user = new User();
				user.setEmail(emailTextBox.getValue().toLowerCase());
				user.setFirstName(firstNameTextBox.getValue());
				user.setLastName(lastNameTextBox.getValue());
				if (!passwordTextBox.getValue().isEmpty()) {
					Formatter f = new Formatter();
					user.setPassword(f.formatPassword(passwordTextBox.getValue()));
				}

				if (adminRadioButton.getValue()) {
					user.setUserGroup(UserGroup.ADMIN);
				} else if (collaboratorRadioButton.getValue()) {
					user.setUserGroup(UserGroup.COLLABORATOR);
				} else {
					user.setUserGroup(UserGroup.ADVANCEDUSER);
				}

				presenter.onUpdateButtonClicked(user);
			} else {
				showWarningMessage("Passwords don't match");
			}
		} else {
			showWarningMessage("Input validation failed");
		}
	}

	@UiHandler("deleteButton")
	public void onDeleteButtonClick(ClickEvent event) {
		if (validateInput()) {
			hideMessage();

			User user = new User();
			user.setEmail(emailTextBox.getValue().toLowerCase());

			presenter.onDeleteButtonClicked(user);
		} else {
			showWarningMessage("Input validation failed");
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public String getPageTitle() {
		return "User Management";
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
		return NavigationEvent.PageId.ADMIN_USER.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.ADMIN_USER;
	}

	@Override
	public void displayUserList(final List<User> users) {
		this.users = users;
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				usersCellList.setRowData(users);
			}
		});
	}

	@Override
	public void resetInputFields() {
		emailTextBox.setValue("");
		firstNameTextBox.setValue("");
		lastNameTextBox.setValue("");
		passwordTextBox.setValue("");
		repeatPasswordTextBox.setValue("");
		collaboratorRadioButton.setValue(true);
		updateButton.setVisible(false);
		deleteButton.setVisible(false);
		addButton.setVisible(true);
		legendLabel.setText("Add new user");
	}
}
