package edu.mit.scansite.client.ui.presenter.main;

import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.main.HomePageView;

/**
 * @author Konstantin Krismer
 */
public class HomePagePresenter extends Presenter {

	private HomePageView view;

	public HomePagePresenter(HomePageView view) {
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
