package edu.mit.scansite.client.ui.widgets.features;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.OListElement;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.transferobjects.Parameter;

/**
 * @author Konstantin Krismer
 */
public class DisplayGeneralPropertiesWidget extends ScansiteWidget {
	interface DisplayGeneralPropertiesWidgetUiBinder extends
			UiBinder<Widget, DisplayGeneralPropertiesWidget> {
	}

	private static DisplayGeneralPropertiesWidgetUiBinder uiBinder = GWT
			.create(DisplayGeneralPropertiesWidgetUiBinder.class);

	private String title = null;

	@UiField
	OListElement propertiesOl;

	public DisplayGeneralPropertiesWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setProperties(final List<Parameter> parameters) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				if (title != null && !title.isEmpty()) {
					builder.appendHtmlConstant("<li class=\"paramSectionTitle\">");
					builder.appendHtmlConstant(title);
					builder.appendHtmlConstant("</li>");
				}
				for (Parameter parameter : parameters) {
					if (parameter.isGroupBegin()) {
						builder.appendHtmlConstant("<li><ol><li style=\"font-weight: bold\">"
								+ parameter.getGroup() + "</li>");
					} else if (parameter.getGroup() != null
							&& !parameter.getGroup().isEmpty()) {
						builder.appendHtmlConstant("</ol></li>");
					} else {
						builder.appendHtmlConstant("<li><label>");
						builder.appendHtmlConstant(parameter.getName().trim());
						if (parameter.isLongParameter()) {
							builder.appendHtmlConstant("</label><br />");
						} else {
							builder.appendHtmlConstant("</label> ");
						}
						if (parameter.isInactiveParameter()) {
							builder.appendHtmlConstant("<span class=\"default\">");
						}
						builder.appendHtmlConstant(parameter.getValue().trim());
						if (parameter.isInactiveParameter()) {
							builder.appendHtmlConstant("</span>");
						}
					}
				}
				propertiesOl.setInnerSafeHtml(builder.toSafeHtml());
			}
		});
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
