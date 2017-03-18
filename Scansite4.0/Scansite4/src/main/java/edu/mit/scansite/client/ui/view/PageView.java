package edu.mit.scansite.client.ui.view;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;

/**
 * @author Konstantin Krismer
 */
public abstract class PageView extends ScansiteWidget implements View {
	public abstract String getPageTitle();

	public abstract boolean isMajorNavigationPage();

	public abstract String getMajorPageId();

	public abstract String getPageId();

	public abstract String getBreadcrumbs();
}
