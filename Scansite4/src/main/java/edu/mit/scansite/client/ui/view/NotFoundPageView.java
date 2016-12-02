package edu.mit.scansite.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.shared.Breadcrumbs;

/**
 * @author Konstantin Krismer
 */
public class NotFoundPageView extends PageView {
	interface NotFoundViewUiBinder extends UiBinder<Widget, NotFoundPageView> {
	}

	private static NotFoundViewUiBinder uiBinder = GWT
			.create(NotFoundViewUiBinder.class);

	public NotFoundPageView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getPageTitle() {
		return "404";
	}

	@Override
	public boolean isMajorNavigationPage() {
		return false;
	}

	@Override
	public String getMajorPageId() {
		return "fileNotFound";
	}

	@Override
	public String getPageId() {
		return getMajorPageId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FILE_NOT_FOUND;
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}
