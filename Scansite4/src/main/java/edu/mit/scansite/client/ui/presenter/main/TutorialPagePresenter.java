package edu.mit.scansite.client.ui.presenter.main;

import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.main.TutorialPageView;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class TutorialPagePresenter extends Presenter {
	private TutorialPageView view;

	public TutorialPagePresenter(TutorialPageView view) {
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
