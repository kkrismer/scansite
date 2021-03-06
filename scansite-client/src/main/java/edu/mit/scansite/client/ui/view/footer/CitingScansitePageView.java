package edu.mit.scansite.client.ui.view.footer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.event.NavigationEvent;

/**
 * @author Konstantin Krismer
 */
public class CitingScansitePageView extends PageView {
	interface CitingScansitePageViewUiBinder extends UiBinder<Widget, CitingScansitePageView> {
	}

	private static CitingScansitePageViewUiBinder uiBinder = GWT
			.create(CitingScansitePageViewUiBinder.class);

	public CitingScansitePageView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getPageTitle() {
		return "Citing Scansite";
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
		return NavigationEvent.PageId.CITE.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.CITE;
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}
