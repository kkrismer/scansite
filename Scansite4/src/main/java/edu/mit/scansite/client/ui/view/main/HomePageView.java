package edu.mit.scansite.client.ui.view.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.shared.Breadcrumbs;

/**
 * @author Konstantin Krismer
 */
public class HomePageView extends PageView {
	interface uiBinder extends UiBinder<Widget, HomePageView> {
	}

	private static uiBinder uiBinder = GWT.create(uiBinder.class);

	public HomePageView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getPageTitle() {
		return "Home";
	}

	@Override
	public boolean isMajorNavigationPage() {
		return true;
	}

	@Override
	public String getMajorPageId() {
		return getPageId();
	}

	@Override
	public String getPageId() {
		return NavigationEvent.PageId.HOME.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.HOME;
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}
