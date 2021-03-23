package edu.mit.scansite.client.ui.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.mit.scansite.client.ui.widgets.admin.HistogramEditWidget;
import edu.mit.scansite.client.ui.widgets.admin.LightWeightMotifCell;
import edu.mit.scansite.client.ui.widgets.admin.MotifGroupSelector;
import edu.mit.scansite.client.ui.widgets.admin.MotifIdentifierWidget;
import edu.mit.scansite.client.ui.widgets.features.ChooseUserFileMotifWidget;
import edu.mit.scansite.client.ui.widgets.motifs.MotifClassWidget;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.event.NavigationEvent;
import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.util.Validator;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifMgmtPageViewImpl extends MotifMgmtPageView {
	public static final int NR_OF_HISTOGRAMS = 2;

	private static MotifMgmtPageViewImplUiBinder uiBinder = GWT.create(MotifMgmtPageViewImplUiBinder.class);

	interface MotifMgmtPageViewImplUiBinder extends UiBinder<Widget, MotifMgmtPageViewImpl> {
	}

	private Presenter presenter;
	private Motif motif;
	private User user;
	private List<HistogramEditWidget> histogramWidgets = new ArrayList<HistogramEditWidget>(NR_OF_HISTOGRAMS);

	@UiField
	MotifClassWidget motifClassWidgetSelect;

	@UiField(provided = true)
	CellList<LightWeightMotif> motifCellList;

	@UiField
	TextBox displayNameTextBox;

	@UiField
	TextBox shortNameTextBox;

	@UiField
	MotifGroupSelector motifGroupSelector;

	@UiField
	MotifIdentifierWidget motifIdentifierWidget;

	@UiField
	MotifClassWidget motifClassWidget;

	@UiField
	CheckBox publicCheckBox;

	@UiField
	SubmitButton updateMetaInfoButton;

	@UiField
	SubmitButton deleteButton;

	@UiField
	TextBox displayNameAddTextBox;

	@UiField
	TextBox shortNameAddTextBox;

	@UiField
	MotifGroupSelector motifGroupSelectorAdd;

	@UiField
	MotifIdentifierWidget motifIdentifierWidgetAdd;

	@UiField
	MotifClassWidget motifClassWidgetAdd;

	@UiField
	CheckBox publicAddCheckBox;

	@UiField
	ChooseUserFileMotifWidget chooseUserFileMotifWidget;

	@UiField
	FlowPanel histogramsPanel;

	@UiField
	SubmitButton confirmButton;

	@UiField
	SubmitButton addButton;

	public MotifMgmtPageViewImpl(User user) {
		this.user = user;
		applyUserPrivileges();

		LightWeightMotifCell motifCell = new LightWeightMotifCell();
		motifCellList = new CellList<LightWeightMotif>(motifCell);

		histogramWidgets.add(new HistogramEditWidget());
		histogramWidgets.add(new HistogramEditWidget());

		initWidget(uiBinder.createAndBindUi(this));

		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				displayNameTextBox.getElement().setId("displayNameTextBoxId");
				shortNameTextBox.getElement().setId("shortNameTextBoxId");
				publicCheckBox.getElement().setId("publicCheckBoxId");
				displayNameAddTextBox.getElement().setId("displayNameAddTextBoxId");
				shortNameAddTextBox.getElement().setId("shortNameAddTextBoxId");
				publicAddCheckBox.getElement().setId("publicAddCheckBoxId");

				motifClassWidgetSelect.setUser(user);
				motifClassWidgetAdd.setUser(user);
				motifClassWidget.setUser(user);

				motifGroupSelector.initMotifGroups(MotifClass.MAMMALIAN);
				motifGroupSelectorAdd.initMotifGroups(MotifClass.MAMMALIAN);

				motifIdentifierWidget.init();
				motifIdentifierWidgetAdd.init();

				for (HistogramEditWidget histWidget : histogramWidgets) {
					histWidget.setVisible(false);
					histogramsPanel.add(histWidget);
				}

				disableEditInputFields();
				final SingleSelectionModel<LightWeightMotif> selectionModel = new SingleSelectionModel<LightWeightMotif>();
				motifCellList.setSelectionModel(selectionModel);
				selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					public void onSelectionChange(SelectionChangeEvent event) {
						presenter.onMotifCellListSelectionChange(selectionModel.getSelectedObject());
						hideMessage(1);
						hideMessage(2);
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

	@Override
	public HistogramEditWidget getHistogramEditWidget(int id) {
		if (id >= 0 && id < NR_OF_HISTOGRAMS) {
			return histogramWidgets.get(id);
		}
		return null;
	}

	@Override
	public List<HistogramEditWidget> getHistogramWidgets() {
		return histogramWidgets;
	}

	@Override
	public void setHistogramsVisible(boolean visible) {
		for (HistogramEditWidget widget : histogramWidgets) {
			widget.setVisible(visible);
		}
	}

	@UiHandler("confirmButton")
	public void onConfirmButtonClick(ClickEvent event) {
		Validator validator = new Validator();
		if (validator.validateGivenString(displayNameAddTextBox.getValue())
				&& validator.validateNoSpaces(shortNameAddTextBox.getValue())) {
			List<Identifier> identifiers = motifIdentifierWidgetAdd.getIdentifiers();
			if (identifiers != null && identifiers.size() > 0) {
				hideMessage(1);
				hideMessage(2);

				if (chooseUserFileMotifWidget.getMotifSelection() != null) {
					Motif motif = chooseUserFileMotifWidget.getMotifSelection().getUserMotif();
					motif.setId(-1);
					motif.setDisplayName(displayNameAddTextBox.getValue());
					motif.setShortName(shortNameAddTextBox.getValue());
					motif.setGroup(motifGroupSelectorAdd.getMotifGroup());
					motif.setIdentifiers(motifIdentifierWidgetAdd.getIdentifiers());
					motif.setMotifClass(motifClassWidgetAdd.getMotifClass());
					motif.setPublic(publicAddCheckBox.getValue());
					motif.setSubmitter(user.getEmail());

					confirmButton.setVisible(false);
					presenter.onConfirmButtonClicked(motif);
					addButton.setVisible(true);
				} else {
					showWarningMessage("Input validation failed: no motif has been uploaded", 2);
				}
			} else {
				showWarningMessage("Input validation failed: at least one identifier required", 2);
			}
		} else {
			showWarningMessage("Input validation failed: invalid display or short name", 2);
		}
	}

	@UiHandler("addButton")
	public void onAddButtonClick(ClickEvent event) {
		Validator validator = new Validator();
		if (validator.validateGivenString(displayNameAddTextBox.getValue())
				&& validator.validateNoSpaces(shortNameAddTextBox.getValue())) {
			List<Identifier> identifiers = motifIdentifierWidgetAdd.getIdentifiers();
			if (identifiers != null && identifiers.size() > 0) {
				hideMessage(1);
				hideMessage(2);

				if (chooseUserFileMotifWidget.getMotifSelection() != null) {
					setAddButtonEnabled(false);
					showWaitSymbol();
					
					Motif motif = chooseUserFileMotifWidget.getMotifSelection().getUserMotif();
					motif.setId(-1);
					motif.setDisplayName(displayNameAddTextBox.getValue());
					motif.setShortName(shortNameAddTextBox.getValue());
					motif.setGroup(motifGroupSelectorAdd.getMotifGroup());
					motif.setIdentifiers(motifIdentifierWidgetAdd.getIdentifiers());
					motif.setMotifClass(motifClassWidgetAdd.getMotifClass());
					motif.setPublic(publicAddCheckBox.getValue());
					motif.setSubmitter(user.getEmail());

					presenter.onAddButtonClicked(motif);
				} else {
					showWarningMessage("Input validation failed: no motif has been uploaded", 2);
				}
			} else {
				showWarningMessage("Input validation failed: at least one identifier required", 2);
			}
		} else {
			showWarningMessage("Input validation failed: invalid display or short name", 2);
		}
	}

	@UiHandler("updateMetaInfoButton")
	public void onUpdateMetaInfoButtonClick(ClickEvent event) {
		Validator validator = new Validator();
		if (validator.validateGivenString(displayNameTextBox.getValue())
				&& validator.validateNoSpaces(shortNameTextBox.getValue())) {
			List<Identifier> identifiers = motifIdentifierWidget.getIdentifiers();
			if (identifiers != null && identifiers.size() > 0) {
				if (motif != null) {
					hideMessage(1);
					hideMessage(2);

					motif.setDisplayName(displayNameTextBox.getValue());
					motif.setShortName(shortNameTextBox.getValue());
					motif.setGroup(motifGroupSelector.getMotifGroup());
					motif.setIdentifiers(motifIdentifierWidget.getIdentifiers());
					motif.setMotifClass(motifClassWidget.getMotifClass());
					motif.setPublic(publicCheckBox.getValue());

					presenter.onUpdateButtonClicked(motif);
				} else {
					showWarningMessage("Could not update motif: no motif was selected");
				}
			} else {
				showWarningMessage("Input validation failed: at least one identifier required", 1);
			}
		} else {
			showWarningMessage("Input validation failed: invalid display or short name", 1);
		}
	}

	@UiHandler("deleteButton")
	public void onDeleteButtonClick(ClickEvent event) {
		if (motif != null) {
			hideMessage(1);
			hideMessage(2);
			presenter.onDeleteButtonClicked(motif);
		} else {
			showWarningMessage("Could not delete motif: No motif was selected");
		}
	}

	@UiHandler("motifClassWidget")
	void onMotifClassWidgetValueChange(ValueChangeEvent<MotifClass> event) {
		motifGroupSelector.initMotifGroups(event.getValue());
	}

	@UiHandler("motifClassWidgetSelect")
	void onMotifClassWidgetSelectValueChange(ValueChangeEvent<MotifClass> event) {
		motifGroupSelector.initMotifGroups(event.getValue());
		presenter.onMotifClassSelectionChange(event.getValue());
		disableEditInputFields();
	}

	@UiHandler("motifClassWidgetAdd")
	void onMotifClassWidgetAddValueChange(ValueChangeEvent<MotifClass> event) {
		motifGroupSelectorAdd.initMotifGroups(event.getValue());
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public String getPageTitle() {
		return "Motif Management";
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
		return NavigationEvent.PageId.ADMIN_MOTIF.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.ADMIN_MOTIF;
	}

	@Override
	public void displayMotifList(final List<LightWeightMotif> motifs) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				motifCellList.setRowData(motifs);
			}
		});
	}

	@Override
	public void disableEditInputFields() {
		motif = null;
		displayNameTextBox.setValue("");
		displayNameTextBox.setEnabled(false);
		shortNameTextBox.setValue("");
		shortNameTextBox.setEnabled(false);
		motifGroupSelector.setEnabled(false);
		motifClassWidget.setMotifClass(MotifClass.MAMMALIAN);
		motifIdentifierWidget.setIdentifiers(null);
		publicCheckBox.setValue(false);
		publicCheckBox.setEnabled(false);
		updateMetaInfoButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	@Override
	public void displayMotif(Motif motif) {
		this.motif = motif;
		displayNameTextBox.setValue(motif.getDisplayName());
		displayNameTextBox.setEnabled(true);
		shortNameTextBox.setValue(motif.getShortName());
		shortNameTextBox.setEnabled(true);
		motifGroupSelector.setEnabled(true);
		motifGroupSelector.setMotifGroup(motif.getGroup());
		motifClassWidget.setMotifClass(motif.getMotifClass());
		motifIdentifierWidget.setIdentifiers(motif.getIdentifiers());
		publicCheckBox.setValue(motif.isPublic());
		publicCheckBox.setEnabled(true);
		updateMetaInfoButton.setEnabled(true);
		deleteButton.setEnabled(true);
	}

	@Override
	public void clearAddInputFields() {
		displayNameAddTextBox.setValue("");
		shortNameAddTextBox.setValue("");
		shortNameAddTextBox.setValue("");
		List<Identifier> identifiers = motifIdentifierWidgetAdd.getIdentifiers();
		for (Identifier identifier : identifiers) {
			identifier.setValue("");
		}
		motifIdentifierWidgetAdd.setIdentifiers(identifiers);
		motifClassWidget.setMotifClass(MotifClass.MAMMALIAN);
		publicCheckBox.setValue(false);
		chooseUserFileMotifWidget.reset();
	}
	
	@Override
	public void setAddButtonEnabled(boolean enabled) {
		addButton.setEnabled(enabled);
	}


	@Override
	public void showWaitSymbol() {
		Element waitSpanElement = DOM.getElementById("waitScan");
		waitSpanElement.setAttribute("style", "display: inline;");
	}

	@Override
	public void hideWaitSymbol() {
		Element waitSpanElement = DOM.getElementById("waitScan");
		waitSpanElement.setAttribute("style", "display: none;");
	}
}
