package edu.mit.scansite.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Konstantin Krismer
 */
public class BreadcrumbsView extends Composite implements View {
	interface BreadcrumbsViewUiBinder extends UiBinder<Widget, BreadcrumbsView> {
	}

	private static BreadcrumbsViewUiBinder uiBinder = GWT
			.create(BreadcrumbsViewUiBinder.class);

	public BreadcrumbsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	SpanElement breadcrumbSpan;

	public void set(String breadcrumbsHTML) {
		breadcrumbSpan.setInnerHTML(breadcrumbsHTML);
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}
