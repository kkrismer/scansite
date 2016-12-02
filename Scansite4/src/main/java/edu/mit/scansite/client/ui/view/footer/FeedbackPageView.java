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
public class FeedbackPageView extends PageView {
	interface FeedbackViewUiBinder extends UiBinder<Widget, FeedbackPageView> {
	}

	private static FeedbackViewUiBinder uiBinder = GWT
			.create(FeedbackViewUiBinder.class);

	public FeedbackPageView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getPageTitle() {
		return "Send Feedback";
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
		return NavigationEvent.PageId.FEEDBACK.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FEEDBACK;
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}
