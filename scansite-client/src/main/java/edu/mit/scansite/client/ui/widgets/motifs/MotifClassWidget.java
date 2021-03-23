package edu.mit.scansite.client.ui.widgets.motifs;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.dispatch.motif.MotifNumbersRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifNumbersRetrieverResult;
import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.states.MotifClassWidgetState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifClassWidget extends ScansiteWidget implements
		HasValueChangeHandlers<MotifClass>, Stateful<MotifClassWidgetState> {
	private static MotifClassWidgetUiBinder uiBinder = GWT
			.create(MotifClassWidgetUiBinder.class);

	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	interface MotifClassWidgetUiBinder extends
			UiBinder<Widget, MotifClassWidget> {
	}

	private User user;
	private String radioButtonGroupName;

	@UiField
	public RadioButton mammalian;

	@UiField
	public RadioButton yeast;

	@UiField
	public RadioButton other;

	public MotifClassWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				mammalian.setText(MotifClass.MAMMALIAN.getName());
				yeast.setText(MotifClass.YEAST.getName());
				other.setText(MotifClass.OTHER.getName());
				retrieveMotifCounts();
				mammalian.setValue(true);
			}
		});
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		retrieveMotifCounts();
	}

	public String getRadioButtonGroupName() {
		return radioButtonGroupName;
	}

	public void setRadioButtonGroupName(final String radioButtonGroupName) {
		this.radioButtonGroupName = radioButtonGroupName;
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				if (radioButtonGroupName != null) {
					mammalian.setName(radioButtonGroupName);
					yeast.setName(radioButtonGroupName);
					other.setName(radioButtonGroupName);
				}
			}
		});
	}

	private void retrieveMotifCounts() {
		dispatch.execute(
				new MotifNumbersRetrieverAction(user != null ? user
						.getSessionId() : ""),
				new AsyncCallback<MotifNumbersRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance()
						.fireEvent(
								new MessageEvent(
										MessageEventPriority.ERROR,
										"Server-side error",
										this.getClass()
												.toString(),
										caught));
					}

					@Override
					public void onSuccess(MotifNumbersRetrieverResult result) {
						hideMessage();
						init(result.getnMammalMotifs(),
								result.getnYeastMotifs(),
								result.getnOtherMotifs());
					}
				});
	}

	private void init(int nMammalMotifs, int nYeastMotifs, int nOtherMotifs) {
		if (nMammalMotifs > 0) {
			mammalian.setText(MotifClass.MAMMALIAN.getName() + " ("
					+ String.valueOf(nMammalMotifs) + " kinase"
					+ (nMammalMotifs > 1 ? "s" : "") + " / domain"
					+ (nMammalMotifs > 1 ? "s" : "") + ")");
			mammalian.setEnabled(true);
		} else {
			mammalian.setEnabled(false);
		}
		if (nYeastMotifs > 0) {
			yeast.setText(MotifClass.YEAST.getName() + " ("
					+ String.valueOf(nYeastMotifs) + " kinase"
					+ (nYeastMotifs > 1 ? "s" : "") + " / domain"
					+ (nYeastMotifs > 1 ? "s" : "") + ")");
			yeast.setEnabled(true);
		} else {
			yeast.setEnabled(false);
		}
		if (nOtherMotifs > 0) {
			other.setText(MotifClass.OTHER.getName() + " ("
					+ String.valueOf(nOtherMotifs) + " kinase"
					+ (nOtherMotifs > 1 ? "s" : "") + " / domain"
					+ (nOtherMotifs > 1 ? "s" : "") + ")");
			other.setEnabled(true);
		} else {
			other.setEnabled(false);
		}
	}

	protected void fireChanged(MotifClass mClass) {
		ValueChangeEvent.fire(this, mClass);
	}

	@UiHandler("mammalian")
	public void onMammalianRadioButtonValueChange(
			ValueChangeEvent<Boolean> event) {
		if (event.getValue()) {
			fireChanged(MotifClass.MAMMALIAN);
		}
	}

	@UiHandler("yeast")
	public void onYeastRadioButtonValueChange(ValueChangeEvent<Boolean> event) {
		if (event.getValue()) {
			fireChanged(MotifClass.YEAST);
		}
	}

	@UiHandler("other")
	public void onOtherRadioButtonValueChange(ValueChangeEvent<Boolean> event) {
		if (event.getValue()) {
			fireChanged(MotifClass.OTHER);
		}
	}

	public MotifClass getMotifClass() {
		if (mammalian.getValue()) {
			return MotifClass.MAMMALIAN;
		} else if (yeast.getValue()) {
			return MotifClass.YEAST;
		} else { // (classOtherRButton.getValue()) {
			return MotifClass.OTHER;
		}
	}

	public void setMotifClass(final MotifClass motifClass) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				mammalian.setValue(MotifClass.MAMMALIAN.equals(motifClass),
						true);
				yeast.setValue(MotifClass.YEAST.equals(motifClass), true);
				other.setValue(MotifClass.OTHER.equals(motifClass), true);
			}
		});
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<MotifClass> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public MotifClassWidgetState getState() {
		return new MotifClassWidgetState(getMotifClass());
	}

	@Override
	public void setState(MotifClassWidgetState state) {
		if (state != null && state.getMotifClass() != null) {
			setMotifClass(state.getMotifClass());
		}
	}
}
