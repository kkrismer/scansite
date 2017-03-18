package edu.mit.scansite.client.ui.widgets.features;

import java.util.List;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.ImagePaths;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.dispatch.features.ProteinCheckOracleAction;
import edu.mit.scansite.shared.dispatch.features.ProteinCheckOracleResult;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.IdentifierType;
import edu.mit.scansite.shared.transferobjects.LightWeightProtein;
import edu.mit.scansite.shared.transferobjects.states.ChooseProteinWidgetState;
import edu.mit.scansite.shared.util.Formatter;
import edu.mit.scansite.shared.util.Validator;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ChooseProteinWidget extends ScansiteWidget implements
		HasValueChangeHandlers<DataSource>, Stateful<ChooseProteinWidgetState> {
	interface ChooseProteinWidgetUiBinder extends
			UiBinder<Widget, ChooseProteinWidget> {
	}

	private static ChooseProteinWidgetUiBinder uiBinder = GWT
			.create(ChooseProteinWidgetUiBinder.class);

	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private MultiWordSuggestOracle identifierOracle = new MultiWordSuggestOracle();
	private boolean oracleRequestPending = false;
	private boolean enableSequenceInput = true;

	@UiField
	RadioButton searchModeProteinIdentifierRadioButton;

	@UiField
	RadioButton searchModeInputSequenceRadioButton;

	@UiField(provided = true)
	DataSourceWidget dataSourceWidget;

	@UiField(provided = true)
	SuggestBox identifier = new SuggestBox(identifierOracle);

	@UiField
	Button checkIdentifier;

	@UiField
	TextBox proteinName;

	@UiField
	TextArea proteinSequence;

	public @UiConstructor ChooseProteinWidget(final boolean initDataSources) {
		dataSourceWidget = new DataSourceWidget(initDataSources);
		identifier.setAccessKey('/');
		identifier
				.setLimit(ScansiteConstants.MAX_SUGGESTIONS_PROTEIN_ACCESSIONS);
		identifier.getElement().setId("identifierId");
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				proteinName.getElement().setId("proteinNameId");
				proteinSequence.getElement().setId("proteinSequenceId");
				updateSuggestionsForOracle();
			}
		});
	}

	@UiHandler("searchModeProteinIdentifierRadioButton")
	public void onSearchModeProteinIdentifierRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("calcMolWeightAccession").setAttribute("style",
				"display: block;");
		DOM.getElementById("calcMolWeightSequence").setAttribute("style",
				"display: none;");
		ValueChangeEvent.fire(this, dataSourceWidget.getDataSource());
	}

	@UiHandler("searchModeInputSequenceRadioButton")
	public void onSearchModeInputSequenceRadioButtonValueChange(
			ValueChangeEvent<Boolean> e) {
		DOM.getElementById("calcMolWeightAccession").setAttribute("style",
				"display: none;");
		DOM.getElementById("calcMolWeightSequence").setAttribute("style",
				"display: block;");
		ValueChangeEvent.fire(this, new DataSource(-1));
	}

	@UiHandler("dataSourceWidget")
	public void onDataSourceWidgetValueChange(ValueChangeEvent<DataSource> event) {
		setIdentifierType(event.getValue().getIdentifierType());
		ValueChangeEvent.fire(this, event.getValue());
	}

	@UiHandler("identifier")
	public void onKeyUp(KeyUpEvent event) {
		int kc = event.getNativeKeyCode();
		if (!KeyUpEvent.isArrow(kc) && kc != KeyCodes.KEY_TAB
				&& kc != KeyCodes.KEY_CTRL && kc != KeyCodes.KEY_ALT
				&& kc != KeyCodes.KEY_ESCAPE && kc != KeyCodes.KEY_END
				&& kc != KeyCodes.KEY_HOME && kc != KeyCodes.KEY_PAGEDOWN
				&& kc != KeyCodes.KEY_PAGEUP && kc != KeyCodes.KEY_SHIFT) {
			updateSuggestionsForOracle();
		}
	}

	@UiHandler("identifier")
	public void onKeyDown(KeyDownEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			updateSuggestionsForOracle();
		}
	}

	@UiHandler("checkIdentifier")
	public void onClick(ClickEvent event) {
		updateSuggestionsForOracle();
	}

	private void updateSuggestionsForOracle() {
		String value = identifier.getValue();
		if (!oracleRequestPending
				&& value != null
				&& value.length() >= ScansiteConstants.MIN_LENGTH_PROTEIN_ORACLE_QUERY) {
			showSymbol(ImagePaths.getStaticImagePath(ImagePaths.WAIT_SMALL));
			oracleRequestPending = true;
			if (dataSourceWidget.getDataSource() != null) {
				dispatch.execute(
						new ProteinCheckOracleAction(dataSourceWidget
								.getDataSource(), value),
						new AsyncCallback<ProteinCheckOracleResult>() {
							@Override
							public void onFailure(Throwable caught) {
								hideSymbol();
								oracleRequestPending = false;
							}

							@Override
							public void onSuccess(
									ProteinCheckOracleResult result) {
								hideSymbol();
								if (result.isSuccess()) {
									identifierOracle.clear();
									if (result.getProteinIdentifiers().size() == 1) {
										identifierOracle.addAll(result
												.getProteinIdentifiers());
										identifier.showSuggestionList();
										showSymbol("img/right.png");
									} else if (result.getProteinIdentifiers()
											.size() > 1) {
										identifierOracle.addAll(result
												.getProteinIdentifiers());
										identifier.showSuggestionList();
									} else {
										showSymbol("img/wrong.png");
									}
								} else {
									showSymbol("img/wrong.png");
								}
								oracleRequestPending = false;
							}
						});
			} else {
				showSymbol("img/wrong.png");
				showErrorMessage("no protein data source available");
			}
		}
	}

	public void setIdentifierType(final IdentifierType identifierType) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				if (identifierType != null) {
					builder.appendHtmlConstant("Protein / gene identifier<br /><span style=\"font-size: 0.9em;\">(");
					builder.appendEscaped(identifierType.getName() + ")");
					builder.appendHtmlConstant("</span>");
				}
				DOM.getElementById("pAcc").setInnerSafeHtml(
						builder.toSafeHtml());
			}
		});
	}

	public boolean inputValidation() {
		boolean isValid = false;
		if (searchModeProteinIdentifierRadioButton.getValue()) {
			isValid = !identifier.getValue().isEmpty()
					&& dataSourceWidget.getDataSource() != null;
		} else {
			Formatter formatter = new Formatter();
			Validator validator = new Validator();
			if (proteinName.getValue().length() == 0) {
				proteinName.setValue("unnamed protein");
			}
			if (proteinSequence.getValue().length() > 0) {
				proteinSequence.setValue(formatter
						.formatSequence(proteinSequence.getValue()));
			}
			isValid = validator.validateProteinSequence(proteinSequence
					.getValue());
		}
		return isValid;
	}

	public LightWeightProtein getProtein() {
		LightWeightProtein protein = null;
		if (inputValidation()) {
			if (searchModeProteinIdentifierRadioButton.getValue()) {
				protein = new LightWeightProtein(identifier.getValue(),
						dataSourceWidget.getDataSource());
			} else {
				protein = new LightWeightProtein(proteinName.getValue(),
						proteinSequence.getValue());
			}
		}
		return protein;
	}

	public void setDataSources(List<DataSource> dataSources) {
		dataSourceWidget.setDataSources(dataSources);
	}

	public void showSymbol(final String symbolPath) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				Element waitSpanElement = DOM.getElementById("wait");
				waitSpanElement.setAttribute("style",
						"display: inline; background-image: url(" + symbolPath
								+ ");");
			}
		});
	}

	public void hideSymbol() {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				Element waitSpanElement = DOM.getElementById("wait");
				waitSpanElement.setAttribute("style", "display: none;");
			}
		});
	}

	public boolean isEnableSequenceInput() {
		return enableSequenceInput;
	}

	public void setEnableSequenceInput(final boolean enableSequenceInput) {
		this.enableSequenceInput = enableSequenceInput;
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				searchModeInputSequenceRadioButton
						.setEnabled(enableSequenceInput);
			}
		});
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<DataSource> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public ChooseProteinWidgetState getState() {
		return new ChooseProteinWidgetState(dataSourceWidget.getState(),
				searchModeProteinIdentifierRadioButton.getValue(),
				searchModeInputSequenceRadioButton.getValue(),
				identifier.getValue(), proteinName.getValue(),
				proteinSequence.getValue());
	}

	@Override
	public void setState(final ChooseProteinWidgetState state) {
		if (state != null) {
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					if (state.isSearchModeInputSequenceRadioButtonValue()) {
						searchModeInputSequenceRadioButton.setValue(true, true);
					} else {
						searchModeProteinIdentifierRadioButton.setValue(true,
								true);
					}
					identifier.setValue(state.getIdentifier());
					proteinName.setValue(state.getProteinName());
					proteinSequence.setValue(state.getProteinSequence());
					dataSourceWidget.setState(state.getDataSourceWidgetState());
				}
			});
		}
	}
}
