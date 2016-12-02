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
import edu.mit.scansite.client.ui.widgets.features.DatabaseScanResultSiteTable;
import edu.mit.scansite.client.ui.widgets.features.DisplayDbRestrictionWidget;
import edu.mit.scansite.client.ui.widgets.features.DisplayGeneralPropertiesWidget;
import edu.mit.scansite.client.ui.widgets.features.DisplayMotifSelectionWidget;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;
import edu.mit.scansite.shared.transferobjects.Parameter;
import edu.mit.scansite.shared.transferobjects.states.ScanDatabaseResultPageState;

/**
 * @author Konstantin Krismer
 */
public class ScanDatabaseResultPageViewImpl extends ScanDatabaseResultPageView {
	interface ScanDatabaseResultPageViewImplUiBinder extends
			UiBinder<Widget, ScanDatabaseResultPageViewImpl> {
	}

	private static ScanDatabaseResultPageViewImplUiBinder uiBinder = GWT
			.create(ScanDatabaseResultPageViewImplUiBinder.class);

	private DatabaseScanResult result = null;

	@UiField
	DisplayMotifSelectionWidget displayMotifSelectionWidget;

	@UiField
	SpanElement database;

	@UiField
	DisplayDbRestrictionWidget displayDbRestrictionWidget;

	@UiField
	DisplayGeneralPropertiesWidget displayScanResultPropertiesWidget;

	@UiField
	FlowPanel motifSitesTable;
	
	@UiField
	Anchor downloadResultAnchor;

	public ScanDatabaseResultPageViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getPageTitle() {
		return "Search a Sequence Database for Motifs - Result";
	}

	@Override
	public boolean isMajorNavigationPage() {
		return false;
	}

	@Override
	public String getMajorPageId() {
		return NavigationEvent.PageId.FEATURE_SCAN_DB.getId();
	}

	@Override
	public String getPageId() {
		return NavigationEvent.PageId.FEATURE_SCAN_DB_RESULT.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FEATURE_SCAN_DB_RESULT;
	}

	@Override
	public void setResult(final DatabaseScanResult result) {
		this.result = result;
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				setScanProperties(result);
				setScanResultProperties(result);
				setMotifSitesTable(result);
				downloadResultAnchor.setHref(result.getResultFilePath());
			}
		});
	}

	private void setScanProperties(DatabaseScanResult result) {
		displayMotifSelectionWidget.setMotifSelection(result
				.getMotifSelection());
		database.setInnerText(result.getDataSource().getDisplayName());
		displayDbRestrictionWidget.setRestrictionProperties(result
				.getRestrictionProperties());
	}

	private void setScanResultProperties(DatabaseScanResult result) {
		List<Parameter> parameters = new LinkedList<>();

		parameters.add(new Parameter("Total number of proteins in database",
				result.getTotalNrOfProteinsInDb()));
		parameters.add(new Parameter(
				"Number of proteins matching restrictions", result
						.getNrOfProteinsFound()));
		parameters.add(new Parameter("Number of predicted sites found", result
				.getTotalNrOfSites()));
		parameters.add(new Parameter("Median of scores", result.getMedian()));
		parameters.add(new Parameter("Median Absolute Deviation of Scores",
				result.getMedianAbsDev()));

		displayScanResultPropertiesWidget.setProperties(parameters);
	}

	private void setMotifSitesTable(DatabaseScanResult result) {
		DatabaseScanResultSiteTable resultTable = new DatabaseScanResultSiteTable(
				result);
		resultTable.setWidth("100%");
		motifSitesTable.clear();
		motifSitesTable.add(resultTable);
	}

	@Override
	public ScanDatabaseResultPageState getState() {
		return new ScanDatabaseResultPageState(result);
	}

	@Override
	public void setState(ScanDatabaseResultPageState state) {
		if (state != null) {
			setResult(state.getDatabaseScanResult());
		}
	}
}
