package edu.mit.scansite.client.ui.widgets.motifs;

import java.util.Collections;
import java.util.List;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupRetrieverResult;
import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.MotifGroup;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.states.MotifClassWidgetState;

/**
 * @author Konstantin Krismer
 */
public class MotifGroupInfoWidget extends ScansiteWidget implements
		Stateful<MotifClassWidgetState>, HasValueChangeHandlers<MotifClass> {
	interface MotifGroupInfoWidgetUiBinder extends
			UiBinder<Widget, MotifGroupInfoWidget> {
	}

	private static MotifGroupInfoWidgetUiBinder uiBinder = GWT
			.create(MotifGroupInfoWidgetUiBinder.class);
	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private MotifClass currentMotifClass = MotifClass.MAMMALIAN;
	private final User user;

	@UiField
	MotifClassWidget motifClassWidget;
	@UiField
	DivElement motifGroups;

	public MotifGroupInfoWidget(final User user) {
		this.user = user;
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				motifClassWidget.setUser(user);
				getMotifGroups(currentMotifClass);
			}
		});
	}

	@UiHandler("motifClassWidget")
	public void onMotifClassWidgetValueChange(ValueChangeEvent<MotifClass> event) {
		setMotifClass(event.getValue());
	}

	protected void setMotifClass(MotifClass motifClass) {
		currentMotifClass = motifClass;
		getMotifGroups(currentMotifClass);
		fireChanged(currentMotifClass);
	}

	private void getMotifGroups(MotifClass motifClass) {
		dispatch.execute(new MotifGroupRetrieverAction(motifClass,
				user != null ? user.getSessionId() : ""),
				new AsyncCallback<MotifGroupRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance()
								.fireEvent(
										new MessageEvent(
												MessageEventPriority.ERROR,
												"Fetching motif groups failed",
												this.getClass().toString(),
												caught));
					}

					@Override
					public void onSuccess(MotifGroupRetrieverResult result) {
						hideMessage();
						setMotifGroups(result.getMotifGroups());
					}
				});
	}

	public void setMotifGroups(final List<MotifGroup> groups) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				Collections.sort(groups);
				if (groups != null && !groups.isEmpty()) {
					hideMessage();
					SafeHtmlBuilder safeHtml = new SafeHtmlBuilder();
					safeHtml.appendHtmlConstant("<ul>");
					for (MotifGroup group : groups) {
						safeHtml.appendHtmlConstant("<li>");
						safeHtml.appendEscaped(group.getDisplayName());
						safeHtml.appendHtmlConstant("<ul>");
						for (LightWeightMotif motif : group.getMotifs()) {
							safeHtml.appendHtmlConstant("<li>");
							safeHtml.appendEscaped(motif.getDisplayName());
							safeHtml.appendHtmlConstant("</li>");
						}
						safeHtml.appendHtmlConstant("</ul>");
						safeHtml.appendHtmlConstant("</li>");
					}
					safeHtml.appendHtmlConstant("</ul>");
					motifGroups.setInnerSafeHtml(safeHtml.toSafeHtml());
				} else {
					showWarningMessage("No motifs available.");
				}
			}
		});
	}

	protected void fireChanged(MotifClass motifClass) {
		ValueChangeEvent.fire(this, motifClass);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<MotifClass> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public MotifClassWidgetState getState() {
		return motifClassWidget.getState();
	}

	@Override
	public void setState(MotifClassWidgetState state) {
		motifClassWidget.setState(state);
	}
}
