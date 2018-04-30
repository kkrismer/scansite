package edu.mit.scansite.client.ui.widgets.features;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.KEYDOWN;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;

/**
 * A {@link Cell} used to render {@link SafeHtml}. Clicking on the cell causes
 * its {@link ValueUpdater} to be called.

 * @author Konstantin Krismer
 */
public class ClickableSafeHtmlCell extends AbstractSafeHtmlCell<SafeHtml> {

	/**
	 * Construct a new ClickableTextCell that will use a
	 * {@link SpecialSafeHtmlRenderer}.
	 */
	public ClickableSafeHtmlCell() {
		this(SpecialSafeHtmlRenderer.getInstance());
	}

	/**
	 * Construct a new ClickableTextCell that will use a given
	 * {@link SafeHtmlRenderer}.
	 * 
	 * @param renderer
	 *            a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
	 */
	public ClickableSafeHtmlCell(SafeHtmlRenderer<SafeHtml> renderer) {
		super(renderer, CLICK, KEYDOWN);
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, SafeHtml value,
			NativeEvent event, ValueUpdater<SafeHtml> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		if (CLICK.equals(event.getType())) {
			onEnterKeyDown(context, parent, value, event, valueUpdater);
		}
	}

	@Override
	protected void onEnterKeyDown(Context context, Element parent,
			SafeHtml value, NativeEvent event,
			ValueUpdater<SafeHtml> valueUpdater) {
		if (valueUpdater != null) {
			valueUpdater.update(value);
		}
	}

	@Override
	public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(value);
		}
	}
}
