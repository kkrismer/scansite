package edu.mit.scansite.client.ui.widgets.features;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.transferobjects.OrganismClass;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;
import edu.mit.scansite.shared.transferobjects.states.DbRestrictionWidgetState;

/**
 * @author Konstantin Krismer
 */
public class DbRestrictionWidget extends ScansiteWidget implements
		Stateful<DbRestrictionWidgetState> {
	interface DbRestrictionWidgetUiBinder extends
			UiBinder<Widget, DbRestrictionWidget> {
	}

	public static final int NR_PHOSPHOSITES = 3;

	private static DbRestrictionWidgetUiBinder uiBinder = GWT
			.create(DbRestrictionWidgetUiBinder.class);

	private boolean showSequenceRegExField = true;

	@UiField
	ListBox organismClassListBox;

	@UiField
	Button organismClassHelpButton;

	@UiField
	HTML organismClassHelp;

	@UiField
	TextBox speciesRegExTextBox;

	@UiField
	Button speciesRegExHelpButton;

	@UiField
	HTML speciesRegExHelp;

	@UiField
	ListBox phosphorylatedSitesListBox;

	@UiField
	Button phosphorylatedSitesHelpButton;

	@UiField
	HTML phosphorylatedSitesHelp;

	@UiField
	TextBox molecularWeightRangeFromTextBox;

	@UiField
	TextBox molecularWeightRangeToTextBox;

	@UiField
	Button molecularWeightRangeHelpButton;

	@UiField
	HTML molecularWeightRangeHelp;

	@UiField
	Button isoelectricPointRangeHelpButton;

	@UiField
	TextBox isoelectricPointRangeFromTextBox;

	@UiField
	TextBox isoelectricPointRangeToTextBox;

	@UiField
	HTML isoelectricPointRangeHelp;

	@UiField
	TextBox keywordRegExTextBox;

	@UiField
	Button keywordRegExHelpButton;

	@UiField
	HTML keywordRegExHelp;

	@UiField
	TextBox sequenceRegExTextBox;

	@UiField
	Button sequenceRegExHelpButton;

	@UiField
	HTML sequenceRegExHelp;

	public DbRestrictionWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				if (!showSequenceRegExField) {
					DOM.getElementById("sequenceRegExSection").setAttribute(
							"style", "display: none");
				}
				initOrganismClassListBox();
				initPhosphorylatedSitesListBox();
				organismClassListBox.getElement().setId(
						"organismClassListBoxId");
				speciesRegExTextBox.getElement().setId("speciesRegExTextBoxId");
				phosphorylatedSitesListBox.getElement().setId(
						"phosphorylatedSitesListBoxId");
				molecularWeightRangeFromTextBox.getElement().setId(
						"molecularWeightRangeFromTextBoxId");
				isoelectricPointRangeFromTextBox.getElement().setId(
						"isoelectricPointRangeFromTextBoxId");
				keywordRegExTextBox.getElement().setId("keywordRegExTextBoxId");
				sequenceRegExTextBox.getElement().setId(
						"sequenceRegExTextBoxId");
			}
		});
	}

	public boolean isShowSequenceRegExField() {
		return showSequenceRegExField;
	}

	public void setShowSequenceRegExField(boolean showSequenceRegExField) {
		this.showSequenceRegExField = showSequenceRegExField;
	}

	@UiHandler("organismClassHelpButton")
	public void onOrganismClassHelpButtonClick(ClickEvent event) {
		showPopupPanel(event, organismClassHelp);
	}

	@UiHandler("speciesRegExHelpButton")
	public void onSingleSpeciesHelpButtonClick(ClickEvent event) {
		showPopupPanel(event, speciesRegExHelp);
	}

	@UiHandler("molecularWeightRangeHelpButton")
	public void onMolecularWeightRangeHelpButtonClick(ClickEvent event) {
		showPopupPanel(event, molecularWeightRangeHelp);
	}

	@UiHandler("isoelectricPointRangeHelpButton")
	public void onIsoelectricPointRangeHelpButtonClick(ClickEvent event) {
		showPopupPanel(event, isoelectricPointRangeHelp);
	}

	@UiHandler("phosphorylatedSitesHelpButton")
	public void onPhosphorylatedSitesHelpButtonClick(ClickEvent event) {
		showPopupPanel(event, phosphorylatedSitesHelp);
	}

	@UiHandler("keywordRegExHelpButton")
	public void onKeywordSearchHelpButtonClick(ClickEvent event) {
		showPopupPanel(event, keywordRegExHelp);
	}

	@UiHandler("sequenceRegExHelpButton")
	public void onSequenceContainsHelpButtonClick(ClickEvent event) {
		showPopupPanel(event, sequenceRegExHelp);
	}

	private void initOrganismClassListBox() {
		for (OrganismClass organismClass : OrganismClass.values()) {
			organismClassListBox.addItem(organismClass.getDisplayName());
		}
	}

	private void initPhosphorylatedSitesListBox() {
		for (int i = 0; i <= NR_PHOSPHOSITES; ++i) {
			phosphorylatedSitesListBox.addItem(String.valueOf(i));
		}
	}

	public boolean inputValidation() {
		Double doubleValue = getMolecularWeightFrom();
		if (doubleValue == null) {
			molecularWeightRangeFromTextBox.setText("");
		}
		doubleValue = getMolecularWeightTo();
		if (doubleValue == null) {
			molecularWeightRangeToTextBox.setText("");
		}
		doubleValue = getIsoelectricPointFrom();
		if (doubleValue == null) {
			isoelectricPointRangeFromTextBox.setText("");
		}
		doubleValue = getIsoelectricPointTo();
		if (doubleValue == null) {
			isoelectricPointRangeToTextBox.setText("");
		}
		return true;
	}

	public OrganismClass getOrganismClass() {
		return OrganismClass.values()[organismClassListBox.getSelectedIndex()];
	}

	public void setOrganismClass(final OrganismClass organismClass) {
		if(organismClass != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					for (int i = 0; i < OrganismClass.values().length; ++i) {
						if (organismClass.equals(OrganismClass.values()[i])) {
							organismClassListBox.setSelectedIndex(i);
							return;
						}
					}
				}
			});
		}
	}

	public String getSpeciesRegEx() {
		return speciesRegExTextBox.getValue();
	}

	public void setSpeciesRegEx(final String speciesRegEx) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				speciesRegExTextBox.setValue(speciesRegEx);
			}
		});
	}

	public int getPhosphorylatedSites() {
		return phosphorylatedSitesListBox.getSelectedIndex();
		// index == value
	}

	public void setPhosphorylatedSites(final int phosphorylatedSites) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				phosphorylatedSitesListBox
						.setSelectedIndex(phosphorylatedSites);
				// index == value
			}
		});
	}

	public Double getMolecularWeightFrom() {
		Double v1 = getDouble(molecularWeightRangeFromTextBox.getValue());
		Double v2 = getDouble(molecularWeightRangeToTextBox.getValue());
		if (v1 == null) {
			return null;
		} else if (v2 == null) {
			return v1;
		} else {
			return Math.min(v1, v2);
		}
	}

	public void setMolecularWeightFrom(final Double molecularWeightFrom) {
		if (molecularWeightFrom != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					molecularWeightRangeFromTextBox
							.setValue(molecularWeightFrom.toString());
				}
			});
		}
	}

	public Double getMolecularWeightTo() {
		Double v1 = getDouble(molecularWeightRangeToTextBox.getValue());
		Double v2 = getDouble(molecularWeightRangeFromTextBox.getValue());
		if (v1 == null) {
			return null;
		} else if (v2 == null) {
			return v1;
		} else {
			return Math.max(v1, v2);
		}
	}

	public void setMolecularWeightTo(final Double molecularWeightTo) {
		if (molecularWeightTo != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					molecularWeightRangeToTextBox.setValue(molecularWeightTo
							.toString());
				}
			});
		}
	}

	public Double getIsoelectricPointFrom() {
		Double v1 = getDouble(isoelectricPointRangeFromTextBox.getValue());
		Double v2 = getDouble(isoelectricPointRangeToTextBox.getValue());
		if (v1 == null) {
			return null;
		} else if (v2 == null) {
			return v1;
		} else {
			return Math.min(v1, v2);
		}
	}

	public void setIsoelectricPointFrom(final Double isoelectricPointFrom) {
		if(isoelectricPointFrom != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					isoelectricPointRangeFromTextBox.setValue(isoelectricPointFrom
							.toString());
				}
			});
		}
	}

	public Double getIsoelectricPointTo() {
		Double v1 = getDouble(isoelectricPointRangeToTextBox.getValue());
		Double v2 = getDouble(isoelectricPointRangeFromTextBox.getValue());
		if (v1 == null) {
			return null;
		} else if (v2 == null) {
			return v1;
		} else {
			return Math.max(v1, v2);
		}
	}

	public void setIsoelectricPointTo(final Double isoelectricPointTo) {
		if(isoelectricPointTo != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					isoelectricPointRangeToTextBox.setValue(isoelectricPointTo
							.toString());
				}
			});
		}
	}

	public String getKeywordRegEx() {
		return keywordRegExTextBox.getValue();
	}

	public void setKeywordRegEx(final String keywordRegEx) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				keywordRegExTextBox.setValue(keywordRegEx);
			}
		});
	}

	public String getSequenceRegEx() {
		return sequenceRegExTextBox.getValue();
	}

	public void setSequenceRegEx(final String sequenceRegEx) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				sequenceRegExTextBox.setValue(sequenceRegEx);
			}
		});
	}

	public RestrictionProperties getRestrictionProperties() {
		List<String> sequenceRegEx = null;
		if (!getSequenceRegEx().equals("")) {
			sequenceRegEx = new LinkedList<>();
			sequenceRegEx.add(getSequenceRegEx());
		}
		RestrictionProperties properties = new RestrictionProperties(
				getOrganismClass(), getSpeciesRegEx(),
				getPhosphorylatedSites(), getMolecularWeightFrom(),
				getMolecularWeightTo(), getIsoelectricPointFrom(),
				getIsoelectricPointTo(), getKeywordRegEx(), sequenceRegEx);

		return properties;
	}

	public void setRestrictionProperties(RestrictionProperties properties) {
		setOrganismClass(properties.getOrganismClass());
		setSpeciesRegEx(properties.getSpeciesRegEx());
		setPhosphorylatedSites(properties.getPhosphorylatedSites());
		setMolecularWeightFrom(properties.getMolecularWeightFrom());
		setMolecularWeightTo(properties.getMolecularWeightTo());
		setIsoelectricPointFrom(properties.getIsoelectricPointFrom());
		setIsoelectricPointTo(properties.getIsoelectricPointTo());
		setKeywordRegEx(properties.getKeywordRegEx());
		if (properties.getSequenceRegEx() != null
				&& !properties.getSequenceRegEx().isEmpty()) {
			setSequenceRegEx(properties.getSequenceRegEx().get(0));
		} else {
			setSequenceRegEx("");
		}
	}

	private Double getDouble(String s) {
		if (s == null || s.isEmpty()) {
			return null;
		} else {
			try {
				return Double.valueOf(s);
			} catch (Exception e) {
				return null;
			}
		}
	}

	private void showPopupPanel(ClickEvent event, HTML html) {
		html.setVisible(true);
		final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
		simplePopup.setWidget(html);
		// Reposition the popup relative to the button
		Widget source = (Widget) event.getSource();
		int left = source.getAbsoluteLeft() + 10;
		int top = source.getAbsoluteTop() + 10;
		simplePopup.setPopupPosition(left, top);
		simplePopup.show();
	}

	@Override
	public DbRestrictionWidgetState getState() {
		return new DbRestrictionWidgetState(getRestrictionProperties());
	}

	@Override
	public void setState(DbRestrictionWidgetState state) {
		if (state != null) {
			setRestrictionProperties(state.getRestrictionProperties());
		}
	}
}
