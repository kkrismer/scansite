package edu.mit.scansite.client.ui.presenter.footer;

import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.footer.FeedbackPageView;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class FeedbackPagePresenter extends Presenter {
	private FeedbackPageView view;

	public FeedbackPagePresenter(FeedbackPageView view) {
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
