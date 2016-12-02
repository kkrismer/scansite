package edu.mit.scansite.client.ui.widgets.features;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.client.ui.widgets.motifs.MotifClassWidget;
import edu.mit.scansite.shared.ImagePaths;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupRetrieverResult;
import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.MotifGroup;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.states.ChooseSelectedMotifsWidgetState;

/**
 * Database motif widget, that allows multiple motif and group selection.
 * 
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ChooseSelectedMotifsWidget extends ChooseMotifWidget implements
		Stateful<ChooseSelectedMotifsWidgetState> {
	interface ChooseSelectedMotifsWidgetUiBinder extends
			UiBinder<Widget, ChooseSelectedMotifsWidget> {
	}

	private static ChooseSelectedMotifsWidgetUiBinder uiBinder = GWT
			.create(ChooseSelectedMotifsWidgetUiBinder.class);

	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private List<LightWeightMotif> motifs;
	private List<MotifGroup> motifGroups;

	private boolean allowMultipleSelection = true;
	private boolean showGroups = true;
	private String legend = "Selected motifs and groups";
	private User user;
	private String motifClassWidgetRadioButtonGroupName = "selectedMotifsMotifClass";

	@UiField
	SpanElement legendSpan;

	@UiField
	MotifClassWidget motifClassWidget;

	@UiField
	ListBox motifsListBox;

	@UiField
	ListBox motifGroupsListBox;

	@UiField
	LIElement motifGroupsLI;

	public ChooseSelectedMotifsWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				motifsListBox.getElement().setId("motifsListBoxId");
				motifGroupsListBox.getElement().setId("motifGroupsListBoxId");
				getMotifs(MotifClass.MAMMALIAN);
				getGroups(MotifClass.MAMMALIAN);
			}
		});
	}

	@UiHandler("motifClassWidget")
	void onMotifClassWidgetValueChange(ValueChangeEvent<MotifClass> event) {
		getMotifs(event.getValue());
		getGroups(event.getValue());
	}

	public boolean isAllowMultipleSelection() {
		return allowMultipleSelection;
	}

	public void setAllowMultipleSelection(final boolean allowMultipleSelection) {
		this.allowMultipleSelection = allowMultipleSelection;
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				motifsListBox.setMultipleSelect(allowMultipleSelection);
				motifGroupsListBox.setMultipleSelect(allowMultipleSelection);
			}
		});
	}

	public boolean isShowGroups() {
		return showGroups;
	}

	public void setShowGroups(final boolean showGroups) {
		this.showGroups = showGroups;
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				if (showGroups) {
					motifGroupsLI.removeAttribute("style");
				} else {
					motifGroupsLI.setAttribute("style", "display: none");
				}
			}
		});
	}

	public String getLegend() {
		return legend;
	}

	public void setLegend(final String legend) {
		this.legend = legend;
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				legendSpan.setInnerText(legend);
			}
		});
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMotifClassWidgetRadioButtonGroupName() {
		return motifClassWidgetRadioButtonGroupName;
	}

	public void setMotifClassWidgetRadioButtonGroupName(
			final String motifClassWidgetRadioButtonGroupName) {
		this.motifClassWidgetRadioButtonGroupName = motifClassWidgetRadioButtonGroupName;

		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				motifClassWidget
						.setRadioButtonGroupName(motifClassWidgetRadioButtonGroupName);
			}
		});
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

	private void getMotifs(MotifClass motifClass) {
		motifsListBox.clear();
		showSymbol(ImagePaths.getStaticImagePath(ImagePaths.WAIT_SMALL));
		dispatch.execute(new LightWeightMotifRetrieverAction(motifClass,
				user != null ? user.getSessionId() : ""),
				new AsyncCallback<LightWeightMotifRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						hideSymbol();
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										"Fetching motifs from server failed", this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(LightWeightMotifRetrieverResult result) {
						hideSymbol();
						hideMessage();
						setMotifs(result.getMotifs());
					}
				});
	}

	private void setMotifs(List<LightWeightMotif> motifs) {
		motifsListBox.clear();
		if (motifs != null && !motifs.isEmpty()) {
			this.motifs = motifs;
			Collections.sort(motifs);
			for (LightWeightMotif motif : motifs) {
				motifsListBox.addItem(motif.getDisplayName(),
						motif.getShortName());
			}
		} else {
			EventBus.instance().fireEvent(
					new MessageEvent(MessageEventPriority.ERROR,
							"Fetching motifs from server failed", this.getClass()
									.toString(), null));
		}
	}

	private void getGroups(MotifClass motifClass) {
		motifGroupsListBox.clear();
		showSymbol(ImagePaths.getStaticImagePath(ImagePaths.WAIT_SMALL));
		dispatch.execute(new MotifGroupRetrieverAction(motifClass,
				user != null ? user.getSessionId() : ""),
				new AsyncCallback<MotifGroupRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						hideSymbol();
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										"Fetching motif groups from server failed", this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(MotifGroupRetrieverResult result) {
						hideSymbol();
						hideMessage();
						setGroups(result.getMotifGroups());
					}
				});
	}

	private void setGroups(List<MotifGroup> motifGroups) {
		motifGroupsListBox.clear();
		if (motifGroups != null && !motifGroups.isEmpty()) {
			this.motifGroups = motifGroups;
			Collections.sort(motifGroups);
			for (LightWeightMotifGroup motifGroup : motifGroups) {
				motifGroupsListBox.addItem(motifGroup.getDisplayName(),
						motifGroup.getShortName());
			}
		} else {
			EventBus.instance().fireEvent(
					new MessageEvent(MessageEventPriority.ERROR,
							"Fetching motif groups from server failed", this.getClass()
									.toString(), null));
		}
	}

	@Override
	public MotifSelection getMotifSelection() {
		Set<String> motifShortNames = new HashSet<>();
		if (allowMultipleSelection) {
			for (int i = 0; i < motifs.size(); ++i) {
				if (motifsListBox.isItemSelected(i)) {
					motifShortNames.add(motifs.get(i).getShortName());
				}
			}
			if (showGroups) {
				for (int i = 0; i < motifGroups.size(); ++i) {
					if (motifGroupsListBox.isItemSelected(i)) {
						for (LightWeightMotif motif : motifGroups.get(i)
								.getMotifs()) {
							motifShortNames.add(motif.getShortName());
						}
					}
				}
			}
		} else {
			motifShortNames.add(motifsListBox.getValue(motifsListBox
					.getSelectedIndex()));
			if (showGroups) {
				for (LightWeightMotif motif : motifGroups.get(
						motifGroupsListBox.getSelectedIndex()).getMotifs()) {
					motifShortNames.add(motif.getShortName());
				}
			}
		}

		return new MotifSelection(motifClassWidget.getMotifClass(),
				motifShortNames);
	}

	@Override
	public ChooseSelectedMotifsWidgetState getState() {
		List<Integer> selectedMotifIndices = new LinkedList<>();
		List<Integer> selectedMotifGroupIndices = new LinkedList<>();

		if (motifs != null) {
			for (int i = 0; i < motifs.size(); ++i) {
				if (motifsListBox.isItemSelected(i)) {
					selectedMotifIndices.add(i);
				}
			}
		}

		if (motifGroups != null) {
			for (int i = 0; i < motifGroups.size(); ++i) {
				if (motifGroupsListBox.isItemSelected(i)) {
					selectedMotifGroupIndices.add(i);
				}
			}
		}

		return new ChooseSelectedMotifsWidgetState(motifClassWidget.getState(),
				selectedMotifIndices, selectedMotifGroupIndices);
	}

	@Override
	public void setState(final ChooseSelectedMotifsWidgetState state) {
		if (state != null) {
			motifClassWidget.setState(state.getMotifClassWidgetState());
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					for (Integer index : state.getSelectedMotifIndices()) {
						if (index < motifsListBox.getItemCount()) {
							motifsListBox.setItemSelected(index, true);
						}
					}
					for (Integer index : state.getSelectedMotifIndices()) {
						if (index < motifGroupsListBox.getItemCount()) {
							motifGroupsListBox.setItemSelected(index, true);
						}
					}
				}
			});
		}
	}
}
