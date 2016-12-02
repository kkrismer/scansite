package edu.mit.scansite.client.ui.presenter.footer;

import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.footer.AdminLoginPageView;

/**
 * @author Konstantin Krismer
 */
public class AdminLoginPagePresenter extends Presenter {
	private AdminLoginPageView view;

	public AdminLoginPagePresenter(AdminLoginPageView view) {
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
