package edu.mit.scansite.client.ui.widgets.features;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;

/**
 * @author Konstantin Krismer
 */
public class SpecialSafeHtmlRenderer implements SafeHtmlRenderer<SafeHtml> {

	private static SpecialSafeHtmlRenderer instance;

	public static SpecialSafeHtmlRenderer getInstance() {
		if (instance == null) {
			instance = new SpecialSafeHtmlRenderer();
		}
		return instance;
	}

	private SpecialSafeHtmlRenderer() {
	}

	@Override
	public SafeHtml render(SafeHtml object) {
		return (object == null) ? SafeHtmlUtils.EMPTY_SAFE_HTML : object;
	}

	public void render(SafeHtml object, SafeHtmlBuilder appendable) {
		appendable.append(object);
	}
}
