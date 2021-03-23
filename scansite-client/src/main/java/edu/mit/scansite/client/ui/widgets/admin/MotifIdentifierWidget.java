package edu.mit.scansite.client.ui.widgets.admin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.shared.dispatch.features.IdentifierTypeRetrieverAction;
import edu.mit.scansite.shared.dispatch.features.IdentifierTypeRetrieverResult;
import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.IdentifierType;

/**
 * @author Konstantin Krismer
 */
public class MotifIdentifierWidget extends ScansiteWidget {

	private static MotifIdentifierWidgetUiBinder uiBinder = GWT
			.create(MotifIdentifierWidgetUiBinder.class);

	interface MotifIdentifierWidgetUiBinder extends
			UiBinder<Widget, MotifIdentifierWidget> {
	}

	protected final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private final Map<IdentifierType, TextBox> identifierTextBoxes = new HashMap<>();

	@UiField
	FlowPanel panel;

	public MotifIdentifierWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void init() {
		dispatch.execute(new IdentifierTypeRetrieverAction(),
				new AsyncCallback<IdentifierTypeRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										"Fetching identifier types from server failed", this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(IdentifierTypeRetrieverResult result) {
						hideMessage();
						initIdentifierTextBoxes(result.getTypes());
					}
				});
	}

	private void initIdentifierTextBoxes(
			final List<IdentifierType> identifierTypes) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				identifierTextBoxes.clear();
				Document doc = Document.get();
				for (IdentifierType type : identifierTypes) {
					LabelElement label = doc.createLabelElement();
					label.setInnerHTML(type.getName() + "*");
					TextBox textBox = new TextBox();
					final LIElement li = doc.createLIElement();

					li.appendChild(label);
					li.appendChild(textBox.getElement());

					panel.add(new Widget() {
						{
							setElement(li);
						}
					});
					identifierTextBoxes.put(type, textBox);
				}
				final SpanElement span = doc.createSpanElement();
				span.setInnerText("* optional (UniProt Entry Name is required for localization match)");
				span.setAttribute("style",
						"font-size: smaller; padding-left: 15px");
				panel.add(new Widget() {
					{
						setElement(span);
					}
				});
			}
		});
	}

	public List<Identifier> getIdentifiers() {
		if (!identifierTextBoxes.isEmpty()) {
			List<Identifier> identifiers = new LinkedList<>();
			for (Entry<IdentifierType, TextBox> identifier : identifierTextBoxes
					.entrySet()) {
				if (!identifier.getValue().getValue().trim().isEmpty()) {
					identifiers.add(new Identifier(identifier.getValue()
							.getValue().trim(), identifier.getKey()));
				}
			}
			return identifiers;
		}
		return null;
	}

	public void setIdentifiers(final List<Identifier> identifiers) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				for(TextBox textBox : identifierTextBoxes.values()) {
					textBox.setValue("");
				}
				if(identifiers != null) {
					for (Identifier identifier : identifiers) {
						identifierTextBoxes.get(identifier.getType()).setValue(
								identifier.getValue());
					}
				}
			}
		});
	}

}
