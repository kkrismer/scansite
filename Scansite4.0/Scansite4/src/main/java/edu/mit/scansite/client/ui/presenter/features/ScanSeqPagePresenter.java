package edu.mit.scansite.client.ui.presenter.features;

import java.util.List;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.event.SequenceMatchEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.features.ScanSeqPageView;
import edu.mit.scansite.shared.dispatch.features.SequenceMatchAction;
import edu.mit.scansite.shared.dispatch.features.SequenceMatchResult;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.RestrictionProperties;
import edu.mit.scansite.shared.transferobjects.SequencePattern;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.states.ScanSeqPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ScanSeqPagePresenter extends Presenter implements
		ScanSeqPageView.Presenter {
	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());
	private ScanSeqPageView view;
	private User user;

	public ScanSeqPagePresenter(ScanSeqPageView view, ScanSeqPageState state,
			User user) {
		this.view = view;
		this.user = user;
		view.setState(state);
	}

	@Override
	public void bind() {
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void onSubmitButtonClicked(List<SequencePattern> sequencePatterns,
			DataSource dataSource, RestrictionProperties properties,
			boolean limitResultsToPhosphorylatedProteins) {
		view.showWaitSymbol();
		fireHistoryEvent(view.getState(),
				NavigationEvent.PageId.FEATURE_SCAN_SEQ.getId());

		SequenceMatchAction action = new SequenceMatchAction(sequencePatterns,
				dataSource, properties, limitResultsToPhosphorylatedProteins,
				user != null ? user.getSessionId() : "");
		dispatch.execute(action, new AsyncCallback<SequenceMatchResult>() {
			@Override
			public void onFailure(Throwable caught) {
				view.setSubmitButtonEnabled(true);
				view.hideWaitImage();
				EventBus.instance().fireEvent(
						new MessageEvent(MessageEventPriority.ERROR,
								"Server-side error", this.getClass()
										.toString(), caught));
			}

			@Override
			public void onSuccess(SequenceMatchResult result) {
				view.setSubmitButtonEnabled(true);
				view.hideWaitImage();
				view.hideMessage();
				EventBus.instance().fireEvent(new SequenceMatchEvent(result));
			}
		});
	}
}
