package edu.mit.scansite.client.ui.widgets.motifs;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.*;
import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.URIs;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.MotifGetAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGetResult;
import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.states.MotifInfoWidgetState;

/**
 * @author Konstantin Krismer
 */
public class MotifInfoWidget extends ScansiteWidget implements
		Stateful<MotifInfoWidgetState>, HasValueChangeHandlers<MotifClass> {
	interface MotifInfoWidgetUiBinder extends UiBinder<Widget, MotifInfoWidget> {
	}

	public static final int IMAGE_HEIGHT = 750;
	public static final int IMAGE_WIDTH = 1000;

	private static MotifInfoWidgetUiBinder uiBinder = GWT
			.create(MotifInfoWidgetUiBinder.class);
	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private MotifClass currentMotifClass = MotifClass.MAMMALIAN;
	private User user;

    private MotifGetResult latestResult;
	private boolean displayToggled;

	@UiField
	MotifClassWidget motifClassWidget;

	@UiField
	ListBox motifListBox;

	@UiField
    Button toggleButton;

	@UiField
	FlowPanel consensus;

	@UiField
	SpanElement motif;

	@UiField
	SpanElement motifGroup;

	@UiField
	SpanElement geneInfo;

	public MotifInfoWidget(final User user) {
		this.user = user;
        latestResult = null;
        displayToggled = false;
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				motifClassWidget.setUser(user);
				getMotifs(currentMotifClass);
				motifClassWidget.setUser(user);
				motifListBox.getElement().setId("motifListBoxId");
			}
		});
	}

	@UiHandler("motifClassWidget")
	public void onMotifClassValueChange(ValueChangeEvent<MotifClass> event) {
		setMotifClass(event.getValue());
	}

	@UiHandler("motifListBox")
	void onMotifListBoxChange(ChangeEvent event) {
		String motifShortName = motifListBox.getValue(motifListBox
				.getSelectedIndex());
		if (motifShortName != null) {
			dispatch.execute(new MotifGetAction(motifShortName),
					new AsyncCallback<MotifGetResult>() {
						@Override
						public void onFailure(Throwable caught) {
							EventBus.instance().fireEvent(
									new MessageEvent(
											MessageEventPriority.ERROR,
											"Fetching motifs failed", this
													.getClass().toString(),
											caught));
						}

						@Override
						public void onSuccess(MotifGetResult result) {
							if (result.isSuccess()) {
								hideMessage();
								motif.setInnerText(result.getMotif()
										.getDisplayName());
								motifGroup.setInnerText(result.getMotifGroup()
										.getDisplayName());
								List<Identifier> identifiers = result
										.getIdentifiers();
								if (identifiers != null
										&& !identifiers.isEmpty()) {
									SafeHtmlBuilder links = new SafeHtmlBuilder();
									for (int i = 0; i < identifiers.size(); ++i) {
										if (i > 0) {
											links.appendEscaped(", ");
										}
										links.appendHtmlConstant("<a target=\"_blank\" href=\""
												+ URIs.getDirectIdentifierInfoLink(identifiers
														.get(i))
												+ "\">"
												+ identifiers.get(i).getValue()
												+ "</a>");
									}
									geneInfo.setInnerSafeHtml(links
											.toSafeHtml());
								}
								consensus.clear();
								Image motifImage = new Image(result.getMotifLogoUrl());
								motifImage.setPixelSize(
										(int) (IMAGE_WIDTH * 0.7),
										(int) (IMAGE_HEIGHT * 0.7));
								consensus.add(motifImage);
								latestResult = result;
							} else {
								EventBus.instance().fireEvent(
										new MessageEvent(
												MessageEventPriority.ERROR,
												result.getErrorMessage(), this
														.getClass().toString(),
												null));
							}
						}
					});
		}
	}


	@UiHandler("toggleButton")
    public void onButtonClick(ClickEvent event) {
	    if (latestResult == null) {
	        toggleButton.setText("Could not load data");
	        return;
        }
        consensus.clear();
        Image motifImage;
        if (displayToggled) { // normal logo
            motifImage = new Image(latestResult.getMotifLogoUrl());
	        displayToggled = false;
	        toggleButton.setText("Switch to polar & non-polar coloring");
        } else { // group colored logo
            motifImage = new Image(latestResult.getToggleMotifLogoUrl());
	        displayToggled = true;
	        toggleButton.setText("Switch to aromatic & aliphatic coloring");
        }
        motifImage.setPixelSize(
                (int) (IMAGE_WIDTH * 0.7),
                (int) (IMAGE_HEIGHT * 0.7));
        consensus.add(motifImage);
    }

	protected void setMotifClass(MotifClass motifClass) {
		currentMotifClass = motifClass;
		getMotifs(currentMotifClass);
		fireChanged(currentMotifClass);
	}

	private void getMotifs(MotifClass motifClass) {
		dispatch.execute(new LightWeightMotifRetrieverAction(motifClass,
				user == null ? "" : user.getSessionId()),
				new AsyncCallback<LightWeightMotifRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance()
								.fireEvent(
										new MessageEvent(
												MessageEventPriority.ERROR,
												"Fetching motifs failed", this
														.getClass().toString(),
												caught));
					}

					@Override
					public void onSuccess(LightWeightMotifRetrieverResult result) {
						hideMessage();
						setMotifs(result.getMotifs());
					}
				});
	}

	private void setMotifs(List<LightWeightMotif> motifs) {
		if (motifs != null && !motifs.isEmpty()) {
			motifListBox.clear();
			for (LightWeightMotif motif : motifs) {
				motifListBox.addItem(motif.getDisplayName(),
						motif.getShortName());
			}
			DomEvent.fireNativeEvent(Document.get().createChangeEvent(),
					motifListBox);
		} else {
			showWarningMessage("No motifs available");
		}
	}

	protected void fireChanged(MotifClass motifClass) {
		ValueChangeEvent.fire(this, motifClass);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<MotifClass> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public MotifInfoWidgetState getState() {
		return new MotifInfoWidgetState(motifClassWidget.getState(),
				motifListBox.getSelectedIndex());
	}

	@Override
	public void setState(final MotifInfoWidgetState state) {
		if (state != null) {
			motifClassWidget.setState(state.getMotifClassWidgetState());
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					motifListBox.setSelectedIndex(state
							.getMotifListBoxSelectedIndex());
				}
			});
		}
	}
}
