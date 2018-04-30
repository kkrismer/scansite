package edu.mit.scansite.client.ui.widgets.admin;

import java.util.HashMap;
import java.util.Map;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupRetrieverResult;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Konstantin Krismer
 */
public class MotifGroupSelector extends ScansiteWidget implements
		HasValueChangeHandlers<LightWeightMotifGroup> {

	private static MotifGroupSelectorUiBinder uiBinder = GWT
			.create(MotifGroupSelectorUiBinder.class);

	interface MotifGroupSelectorUiBinder extends
			UiBinder<Widget, MotifGroupSelector> {
	}

	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private Map<Integer, LightWeightMotifGroup> motifGroups = new HashMap<>();
	private LightWeightMotifGroup motifGroup;
	private User user;

	@UiField
	ListBox motifGroupListBox;

	public MotifGroupSelector() {
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				motifGroupListBox.getElement().setId("motifGroupListBoxId");
			}
		});
	}

	@UiHandler("motifGroupListBox")
	void onMotifGroupListBoxChange(ChangeEvent event) {
		fireChanged(motifGroups.get(Integer.parseInt(motifGroupListBox
				.getValue(motifGroupListBox.getSelectedIndex()))));
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void initMotifGroups(final MotifClass motifClass) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				motifGroupListBox.getElement().setId("motifGroupListBoxId");
				motifGroupListBox.setEnabled(false);
				dispatch.execute(new MotifGroupRetrieverAction(motifClass,
						user != null ? user.getSessionId() : ""),
						new AsyncCallback<MotifGroupRetrieverResult>() {
							@Override
							public void onFailure(Throwable caught) {
								EventBus.instance().fireEvent(
										new MessageEvent(MessageEventPriority.ERROR,
												caught.getMessage(), this.getClass()
														.toString(), caught));
							}

							@Override
							public void onSuccess(
									MotifGroupRetrieverResult result) {
								hideMessage();
								motifGroupListBox.clear();
								motifGroups.clear();
								if (result.getMotifGroups() != null) {
									for (LightWeightMotifGroup motifGroup : result
											.getMotifGroups()) {
										motifGroups.put(motifGroup.getId(),
												motifGroup);
										motifGroupListBox.addItem(motifGroup
												.getDisplayName(), Integer
												.toString(motifGroup.getId()));
									}
									motifGroupListBox.setSelectedIndex(0);
									fireChanged(motifGroups.get(Integer.parseInt(motifGroupListBox
											.getValue(motifGroupListBox
													.getSelectedIndex()))));
								}
								motifGroupListBox.setEnabled(true);
							}
						});
			}
		});
	}

	public void setMotifGroup(final LightWeightMotifGroup motifGroup) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				fireChanged(motifGroup);
			}
		});
	}

	protected void fireChanged(LightWeightMotifGroup motifGroup) {
		if (motifGroup != null) {
			this.motifGroup = motifGroup;
			if (motifGroup.getId() != -1) {
				for (int i = 0; i < motifGroupListBox.getItemCount(); ++i) {
					if (Integer.parseInt(motifGroupListBox.getValue(i)) == motifGroup
							.getId()) {
						motifGroupListBox.setSelectedIndex(i);
					}
				}
				motifGroupListBox.setEnabled(true);
			} else {
				motifGroupListBox.setEnabled(false);
			}
			ValueChangeEvent.fire(this, motifGroup);
		}
	}

	public LightWeightMotifGroup getMotifGroup() {
		return motifGroup;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<LightWeightMotifGroup> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public void setEnabled(final boolean enabled) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				motifGroupListBox.setEnabled(enabled);
			}
		});
	}
}
