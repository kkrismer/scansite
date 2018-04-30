package edu.mit.scansite.client.ui.presenter.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.admin.MotifGroupMgmtPageView;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifGroupRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifGroupRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupAddAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupDeleteAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupUpdateAction;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGroupMgmtPagePresenter extends Presenter implements
		MotifGroupMgmtPageView.Presenter {
	private MotifGroupMgmtPageView view;

	public MotifGroupMgmtPagePresenter(MotifGroupMgmtPageView view) {
		this.view = view;
	}

	@Override
	public void bind() {
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		retrieveMotifGroups();
	}

	private void retrieveMotifGroups() {
		dispatch.execute(new LightWeightMotifGroupRetrieverAction(),
				new AsyncCallback<LightWeightMotifGroupRetrieverResult>() {
					public void onFailure(Throwable e) {
						EventBus.instance()
								.fireEvent(
										new MessageEvent(
												MessageEventPriority.ERROR,
												"Unable to retrieve motif groups from database",
												this.getClass().toString(), e));
					}

					public void onSuccess(
							LightWeightMotifGroupRetrieverResult result) {
						view.displayMotifGroupList(result.getMotifGroups());
						view.hideMessage();
					}
				});
	}

	@Override
	public void onAddButtonClicked(LightWeightMotifGroup motifGroup) {
		dispatch.execute(new MotifGroupAddAction(motifGroup),
				new AsyncCallback<LightWeightMotifGroupRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance()
								.fireEvent(
										new MessageEvent(
												MessageEventPriority.ERROR,
												"Unable to add motif group to database",
												this.getClass().toString(),
												caught));
					}

					@Override
					public void onSuccess(
							LightWeightMotifGroupRetrieverResult result) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.INFO,
										"Motif group successfully added", this
												.getClass().toString(), null));
						view.resetInputFields();
						view.displayMotifGroupList(result.getMotifGroups());
					}
				});
	}

	@Override
	public void onUpdateButtonClicked(LightWeightMotifGroup motifGroup) {
		dispatch.execute(new MotifGroupUpdateAction(motifGroup),
				new AsyncCallback<LightWeightMotifGroupRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance()
								.fireEvent(
										new MessageEvent(
												MessageEventPriority.ERROR,
												"Unable to update motif group",
												this.getClass().toString(),
												caught));
					}

					@Override
					public void onSuccess(
							LightWeightMotifGroupRetrieverResult result) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.INFO,
										"Motif group successfully updated",
										this.getClass().toString(), null));
						view.resetInputFields();
						view.displayMotifGroupList(result.getMotifGroups());
					}
				});
	}

	@Override
	public void onDeleteButtonClicked(LightWeightMotifGroup motifGroup) {
		dispatch.execute(new MotifGroupDeleteAction(motifGroup.getId()),
				new AsyncCallback<LightWeightMotifGroupRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance()
								.fireEvent(
										new MessageEvent(
												MessageEventPriority.ERROR,
												"Unable to delete motif group - deletion of group is not possible if there are still motifs associated with it",
												this.getClass().toString(),
												caught));
					}

					@Override
					public void onSuccess(
							LightWeightMotifGroupRetrieverResult result) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.INFO,
										"Motif group successfully deleted",
										this.getClass().toString(), null));
						view.resetInputFields();
						view.displayMotifGroupList(result.getMotifGroups());
					}
				});
	}
}
