package edu.mit.scansite.client.ui.view.admin;

import java.sql.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.widgets.news.NewsCell;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.transferobjects.NewsEntry;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.util.Validator;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NewsMgmtPageViewImpl extends NewsMgmtPageView {
	private static NewsMgmtPageViewImplUiBinder uiBinder = GWT
			.create(NewsMgmtPageViewImplUiBinder.class);

	interface NewsMgmtPageViewImplUiBinder extends
			UiBinder<Widget, NewsMgmtPageViewImpl> {
	}

	private Presenter presenter;
	private User user;

	@UiField(provided = true)
	CellList<NewsEntry> newsEntriesCellList;

	@UiField
	Label legendLabel;

	@UiField
	TextBox titleTextBox;

	@UiField
	TextArea messageTextArea;

	@UiField
	SubmitButton addButton;

	@UiField
	SubmitButton updateButton;

	@UiField
	SubmitButton deleteButton;

	@UiField
	Hidden newsId;

	public NewsMgmtPageViewImpl(User user) {
		this.user = user;
		applyUserPrivileges();

		NewsCell newsCell = new NewsCell();
		newsEntriesCellList = new CellList<NewsEntry>(newsCell);
		initWidget(uiBinder.createAndBindUi(this));

		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				titleTextBox.getElement().setId("titleTextBoxId");
				messageTextArea.getElement().setId("messageTextAreaId");

				final SingleSelectionModel<NewsEntry> selectionModel = new SingleSelectionModel<NewsEntry>();
				newsEntriesCellList.setSelectionModel(selectionModel);
				selectionModel
						.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
							public void onSelectionChange(
									SelectionChangeEvent event) {
								newsId.setValue(Integer.toString(selectionModel
										.getSelectedObject().getId()));
								titleTextBox.setText(selectionModel
										.getSelectedObject().getTitle());
								messageTextArea.setText(selectionModel
										.getSelectedObject().getText());

								legendLabel.setText("Update news entry");
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
		if (user == null || (!user.isAdmin() && !user.isSuperAdmin())) {
			History.newItem(NavigationEvent.PageId.ADMIN.getId(), true);
		}
	}

	private boolean validateInput() {
		Validator validator = new Validator();
		return validator.validateGivenString(titleTextBox.getText())
				&& validator.validateGivenString(messageTextArea.getText());
	}

	@UiHandler("addButton")
	public void onAddButtonClick(ClickEvent event) {
		if (validateInput()) {
			hideMessage();

			NewsEntry entry = new NewsEntry();
			entry.setDate(new Date(System.currentTimeMillis()));
			entry.setId(-1);
			entry.setText(messageTextArea.getText());
			entry.setTitle(titleTextBox.getText());
			entry.setUser(user);

			presenter.onAddButtonClicked(entry);
		} else {
			showWarningMessage("Input validation failed");
		}
	}

	@UiHandler("updateButton")
	public void onUpdateButtonClick(ClickEvent event) {
		if (validateInput()) {
			hideMessage();

			NewsEntry entry = new NewsEntry();
			entry.setDate(new Date(System.currentTimeMillis()));
			entry.setId(Integer.parseInt(newsId.getValue()));
			entry.setText(messageTextArea.getText());
			entry.setTitle(titleTextBox.getText());
			entry.setUser(user);

			presenter.onUpdateButtonClicked(entry);
		} else {
			showWarningMessage("Input validation failed");
		}
	}

	@UiHandler("deleteButton")
	public void onDeleteButtonClick(ClickEvent event) {
		if (validateInput()) {
			hideMessage();

			NewsEntry entry = new NewsEntry();
			entry.setId(Integer.parseInt(newsId.getValue()));
			entry.setUser(user);

			presenter.onDeleteButtonClicked(entry);
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
		return "News Management";
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
		return NavigationEvent.PageId.ADMIN_NEWS.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.ADMIN_NEWS;
	}

	@Override
	public void displayNewsList(final List<NewsEntry> news) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				newsEntriesCellList.setRowData(news);
			}
		});
	}

	@Override
	public void resetInputFields() {
		titleTextBox.setText("");
		messageTextArea.setText("");
		newsId.setValue("-1");
		updateButton.setVisible(false);
		deleteButton.setVisible(false);
		addButton.setVisible(true);
		legendLabel.setText("Add news entry");
	}
}
