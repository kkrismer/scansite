package edu.mit.scansite.client.ui.presenter;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.event.NavigationEvent.PageId;
import edu.mit.scansite.client.ui.event.NavigationEventHandler;
import edu.mit.scansite.client.ui.view.NavigationBarView;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NavigationBarPresenter extends Presenter {
	private final NavigationBarView view;

	public NavigationBarPresenter(NavigationBarView navigationBarView) {
		this.view = navigationBarView;
	}

	@Override
	public void bind() {
		EventBus.instance().addHandler(NavigationEvent.TYPE,
				new NavigationEventHandler() {
					@Override
					public void onNavigationEvent(NavigationEvent event) {
						PageId target = event.getNavigationTargetId();
						History.newItem(target.getId(), true);
					}
				});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	public void select(String majorPageId) {
		view.select(majorPageId);
	}
}
