package edu.mit.scansite.client.ui.widgets.features;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.client.ui.widgets.motifs.MotifClassWidget;
import edu.mit.scansite.shared.transferobjects.MotifSelection;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.states.MotifClassWidgetState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ChooseAllMotifsWidget extends ChooseMotifWidget implements
		Stateful<MotifClassWidgetState> {
	private static ChooseAllMotifsWidgetUiBinder uiBinder = GWT
			.create(ChooseAllMotifsWidgetUiBinder.class);

    interface ChooseAllMotifsWidgetUiBinder extends
			UiBinder<Widget, ChooseAllMotifsWidget> {
	}

	private User user;

	@UiField
	MotifClassWidget motifClassWidget;

	public ChooseAllMotifsWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		motifClassWidget.setUser(user);
	}

    public void setUser(User user) {
        this.user = user;
        motifClassWidget.setUser(user);
    }

    @Override
	public MotifSelection getMotifSelection() {
		return new MotifSelection(motifClassWidget.getMotifClass());
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
