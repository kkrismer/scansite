package edu.mit.scansite.client.ui.view.footer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.event.NavigationEvent;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class AboutPageView extends PageView {
	interface AboutViewUiBinder extends UiBinder<Widget, AboutPageView> {
	}

	private static AboutViewUiBinder uiBinder = GWT
			.create(AboutViewUiBinder.class);

	public AboutPageView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getPageTitle() {
		return "About";
	}

	@Override
	public boolean isMajorNavigationPage() {
		return false;
	}

	@Override
	public String getMajorPageId() {
		return getPageId();
	}

	@Override
	public String getPageId() {
		return NavigationEvent.PageId.ABOUT.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.ABOUT;
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}
