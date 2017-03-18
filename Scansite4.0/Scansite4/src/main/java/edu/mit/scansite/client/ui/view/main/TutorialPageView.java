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
public class TutorialPageView extends PageView {
	interface TutorialPageViewUiBinder extends
			UiBinder<Widget, TutorialPageView> {
	}

	private static TutorialPageViewUiBinder uiBinder = GWT
			.create(TutorialPageViewUiBinder.class);

	public TutorialPageView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getPageTitle() {
		return "Tutorial";
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
		return NavigationEvent.PageId.TUTORIAL.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.TUTORIAL;
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}
