package edu.mit.scansite.client.ui.view.features;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.widgets.features.DisplayDbRestrictionWidget;
import edu.mit.scansite.client.ui.widgets.features.DisplayGeneralPropertiesWidget;
import edu.mit.scansite.client.ui.widgets.features.SequenceMatchResultTable;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.dispatch.features.SequenceMatchResult;
import edu.mit.scansite.shared.transferobjects.Parameter;
import edu.mit.scansite.shared.transferobjects.SequencePattern;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.states.ScanSeqResultPageState;

/**
 * @author Konstantin Krismer
 */
public class ScanSeqResultPageViewImpl extends ScanSeqResultPageView {
	interface ScanSeqResultPageViewImplUiBinder extends
			UiBinder<Widget, ScanSeqResultPageViewImpl> {
	}

	private static ScanSeqResultPageViewImplUiBinder uiBinder = GWT
			.create(ScanSeqResultPageViewImplUiBinder.class);

	private SequenceMatchResult result = null;
	private User user;

	@UiField
	SpanElement sequencePatternSpan;

	@UiField
	SpanElement databaseSpan;

	@UiField
	DisplayDbRestrictionWidget displayDbRestrictionWidget;

	@UiField
	DisplayGeneralPropertiesWidget displayScanResultPropertiesWidget;

	@UiField
	FlowPanel resultFlowPanel;
	
	@UiField
	Anchor downloadResultAnchor;

	public ScanSeqResultPageViewImpl(User user) {
		this.user = user;
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getPageTitle() {
		return "Find Sequence Match - Result";
	}

	@Override
	public boolean isMajorNavigationPage() {
		return false;
	}

	@Override
	public String getMajorPageId() {
		return NavigationEvent.PageId.FEATURE_SCAN_SEQ.getId();
	}

	@Override
	public String getPageId() {
		return NavigationEvent.PageId.FEATURE_SCAN_SEQ_RESULT.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FEATURE_SCAN_SEQ_RESULT;
	}

	@Override
	public void setResult(final SequenceMatchResult result) {
		this.result = result;
		Command setResult = new Command() {
			@Override
			public void execute() {
				setScanProperties(result);
				setScanResultProperties(result);
				setResultTable(result);
				downloadResultAnchor.setHref(result.getResultFilePath());
			}
		};
		runCommandOnLoad(setResult);
	}

	private void setScanProperties(SequenceMatchResult result) {
		String sequencePattern = "";
		boolean first = true;
		for (SequencePattern pattern : result.getSequencePatterns()) {
			if (first) {
				first = false;
			} else {
				sequencePattern += ", ";
			}
			sequencePattern += pattern.getHtmlFormattedRegEx();
		}
		sequencePatternSpan.setInnerHTML(sequencePattern);
		databaseSpan.setInnerText(result.getDataSource().getDisplayName());
		displayDbRestrictionWidget.setRestrictionProperties(result
				.getRestrictionProperties());
	}

	private void setScanResultProperties(SequenceMatchResult result) {
		List<Parameter> parameters = new LinkedList<>();

		parameters.add(new Parameter("Total number of proteins in database",
				Integer.toString(result.getProteinsInDbCount())));
		parameters.add(new Parameter(
				"Number of proteins matching restrictions", Integer
						.toString(result.getRestrictedProteinsInDbCount())));
		parameters.add(new Parameter("Number of sequence pattern matches",
				Integer.toString(result.getSequencePatternMatchCount())));
		parameters.add(new Parameter("Number of matched proteins", (result
				.isMoreMatchesThanMaxAllowed() ? "more than " : "")
				+ result.getMatches().size()));

		displayScanResultPropertiesWidget.setProperties(parameters);
	}

	private void setResultTable(SequenceMatchResult result) {
		SequenceMatchResultTable resultTable = new SequenceMatchResultTable(
				result, user);
		resultTable.setWidth("100%");
		resultFlowPanel.clear();
		resultFlowPanel.add(resultTable);
	}

	@Override
	public ScanSeqResultPageState getState() {
		return new ScanSeqResultPageState(result);
	}

	@Override
	public void setState(ScanSeqResultPageState state) {
		if (state != null) {
			setResult(state.getSequenceMatchResult());
		}
	}
}
