package edu.mit.scansite.client.ui.view.footer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.shared.Breadcrumbs;

/**
 * @author Konstantin Krismer
 */
public class FAQPageView extends PageView {
	interface FAQViewUiBinder extends UiBinder<Widget, FAQPageView> {
	}

	private static FAQViewUiBinder uiBinder = GWT.create(FAQViewUiBinder.class);

	public FAQPageView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getPageTitle() {
		return "FAQ";
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
		return NavigationEvent.PageId.FAQ.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FAQ;
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}
