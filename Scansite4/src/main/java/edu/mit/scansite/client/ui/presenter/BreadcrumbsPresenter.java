package edu.mit.scansite.client.ui.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.view.BreadcrumbsView;

/**
 * @author Konstantin Krismer
 */
public class BreadcrumbsPresenter extends Presenter {

	private final BreadcrumbsView view;

	public BreadcrumbsPresenter(BreadcrumbsView view) {
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

	public void set(String breadcrumbsHTML) {
		view.set(breadcrumbsHTML);
	}
}