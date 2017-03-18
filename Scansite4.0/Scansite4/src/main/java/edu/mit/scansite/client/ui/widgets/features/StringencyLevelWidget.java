package edu.mit.scansite.client.ui.widgets.features;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.widgets.ScansiteWidget;
import edu.mit.scansite.client.ui.widgets.Stateful;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.states.StringencyLevelWidgetState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class StringencyLevelWidget extends ScansiteWidget implements
		Stateful<StringencyLevelWidgetState> {
	interface StringencyLevelWidgetUiBinder extends
			UiBinder<Widget, StringencyLevelWidget> {
	}

	private static StringencyLevelWidgetUiBinder uiBinder = GWT
			.create(StringencyLevelWidgetUiBinder.class);

	private List<HistogramStringency> stringencies = new LinkedList<HistogramStringency>();

	@UiField
	ListBox stringencyListBox;

	public StringencyLevelWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		for (HistogramStringency hs : HistogramStringency.values()) {
			stringencies.add(hs);
		}
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				for (HistogramStringency stringency : stringencies) {
					stringencyListBox.addItem(stringency.getName());
				}
				stringencyListBox.getElement().setId("stringencyListBoxId");
			}
		});
	}

	public StringencyLevelWidget(HistogramStringency toExclude) {
		this();
		if (toExclude != null) {
			stringencies.remove(toExclude);
		}
	}

	public HistogramStringency getStringency() {
		return stringencies.get(stringencyListBox.getSelectedIndex());
	}

	public void select(final HistogramStringency stringency) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				for (int i = 0; i < stringencies.size(); ++i) {
					if (stringencies.get(i).equals(stringency)) {
						stringencyListBox.setSelectedIndex(i);
					}
				}
			}
		});
	}

	public void setEnabled(final boolean enabled) {
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				stringencyListBox.setEnabled(enabled);
			}
		});
	}

	@Override
	public StringencyLevelWidgetState getState() {
		return new StringencyLevelWidgetState(getStringency());
	}

	@Override
	public void setState(StringencyLevelWidgetState state) {
		if (state != null) {
			select(state.getStringency());
		}
	}
}
