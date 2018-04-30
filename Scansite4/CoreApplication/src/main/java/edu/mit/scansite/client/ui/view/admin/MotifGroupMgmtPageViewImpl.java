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
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.widgets.admin.MotifGroupCell;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.util.Validator;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupMgmtPageViewImpl extends MotifGroupMgmtPageView {

	private static MotifGroupMgmtPageViewImplUiBinder uiBinder = GWT
			.create(MotifGroupMgmtPageViewImplUiBinder.class);

	interface MotifGroupMgmtPageViewImplUiBinder extends
			UiBinder<Widget, MotifGroupMgmtPageViewImpl> {
	}

	public MotifGroupMgmtPageViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private Presenter presenter;
	private User user;

	@UiField(provided = true)
	CellList<LightWeightMotifGroup> motifGroupCellList;

	@UiField
	Label legendLabel;

	@UiField
	TextBox displayNameTextBox;

	@UiField
	TextBox shortNameTextBox;

	@UiField
	SubmitButton addButton;

	@UiField
	SubmitButton updateButton;

	@UiField
	SubmitButton deleteButton;

	@UiField
	Hidden motifGroupId;

	public MotifGroupMgmtPageViewImpl(User user) {
		this.user = user;
		applyUserPrivileges();

		MotifGroupCell motifGroupCell = new MotifGroupCell();
		motifGroupCellList = new CellList<LightWeightMotifGroup>(motifGroupCell);
		initWidget(uiBinder.createAndBindUi(this));

		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				displayNameTextBox.getElement().setId("displayNameTextBoxId");
				shortNameTextBox.getElement().setId("shortNameTextBoxId");

				final SingleSelectionModel<LightWeightMotifGroup> selectionModel = new SingleSelectionModel<LightWeightMotifGroup>();
				motifGroupCellList.setSelectionModel(selectionModel);
				selectionModel
						.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
							public void onSelectionChange(
									SelectionChangeEvent event) {
								motifGroupId.setValue(Integer
										.toString(selectionModel
												.getSelectedObject().getId()));
								displayNameTextBox.setText(selectionModel
										.getSelectedObject().getDisplayName());
								shortNameTextBox.setText(selectionModel
										.getSelectedObject().getShortName());

								legendLabel.setText("Edit motif group");
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
		return validator.validateGivenString(displayNameTextBox.getValue())
				&& validator.validateGivenString(shortNameTextBox.getValue());
	}

	@UiHandler("addButton")
	public void onAddButtonClick(ClickEvent event) {
		if (validateInput()) {
			hideMessage();

			LightWeightMotifGroup motifGroup = new LightWeightMotifGroup();
			motifGroup.setId(-1);
			motifGroup.setDisplayName(displayNameTextBox.getValue());
			motifGroup.setShortName(shortNameTextBox.getValue());

			presenter.onAddButtonClicked(motifGroup);
		} else {
			showWarningMessage("Input validation failed");
		}
	}

	@UiHandler("updateButton")
	public void onUpdateButtonClick(ClickEvent event) {
		if (validateInput()) {
			hideMessage();

			LightWeightMotifGroup motifGroup = new LightWeightMotifGroup();
			motifGroup.setId(Integer.parseInt(motifGroupId.getValue()));
			motifGroup.setDisplayName(displayNameTextBox.getValue());
			motifGroup.setShortName(shortNameTextBox.getValue());

			presenter.onUpdateButtonClicked(motifGroup);
		} else {
			showWarningMessage("Input validation failed");
		}
	}

	@UiHandler("deleteButton")
	public void onDeleteButtonClick(ClickEvent event) {
		if (validateInput()) {
			hideMessage();

			LightWeightMotifGroup motifGroup = new LightWeightMotifGroup();
			motifGroup.setId(Integer.parseInt(motifGroupId.getValue()));

			presenter.onDeleteButtonClicked(motifGroup);
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
		return "Motif Group Management";
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
		return NavigationEvent.PageId.ADMIN_MOTIF_GROUP.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.ADMIN_MOTIF_GROUP;
	}

	@Override
	public void displayMotifGroupList(final List<LightWeightMotifGroup> motifGroups) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				motifGroupCellList.setRowData(motifGroups);
			}
		});
	}

	@Override
	public void resetInputFields() {
		displayNameTextBox.setValue("");
		shortNameTextBox.setValue("");
		motifGroupId.setValue("-1");
		updateButton.setVisible(false);
		deleteButton.setVisible(false);
		addButton.setVisible(true);
		legendLabel.setText("Add motif group");
	}
}
