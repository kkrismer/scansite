package edu.mit.scansite.client.ui.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.view.NotFoundPageView;

/**
 * @author Konstantin Krismer
 */
public class NotFoundPagePresenter extends Presenter {
	private NotFoundPageView view;

	public NotFoundPagePresenter(NotFoundPageView view) {
		this.view = view;
	}

	@Override
	public void bind() {
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}
}
