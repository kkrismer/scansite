package edu.mit.scansite.client.ui.view.features;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.widgets.features.DisplayGeneralPropertiesWidget;
import edu.mit.scansite.client.ui.widgets.features.OrthologScanResultTable;
import edu.mit.scansite.client.ui.widgets.features.SequenceAlignmentTable;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.dispatch.features.OrthologScanResult;
import edu.mit.scansite.shared.transferobjects.Parameter;
import edu.mit.scansite.shared.transferobjects.states.ScanOrthologsResultPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ScanOrthologsResultPageViewImpl extends ScanOrthologsResultPageView {
	interface ScanOrthologsResultPageViewImplUiBinder extends UiBinder<Widget, ScanOrthologsResultPageViewImpl> {
	}

	private static ScanOrthologsResultPageViewImplUiBinder uiBinder = GWT
			.create(ScanOrthologsResultPageViewImplUiBinder.class);

	private OrthologScanResult result = null;

	@UiField
	DisplayGeneralPropertiesWidget displayScanPropertiesWidget;

	@UiField
	DisplayGeneralPropertiesWidget displayScanResultPropertiesWidget;

	@UiField
	FlowPanel orthologousProteinsFlowPanel;

	@UiField
	FlowPanel sequenceAlignmentFlowPanel;

	public ScanOrthologsResultPageViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getPageTitle() {
		return "Scan for evolutionary conserved phosphorylation sites - Result";
	}

	@Override
	public boolean isMajorNavigationPage() {
		return false;
	}

	@Override
	public String getMajorPageId() {
		return NavigationEvent.PageId.FEATURE_SCAN_ORTHOLOGS.getId();
	}

	@Override
	public String getPageId() {
		return NavigationEvent.PageId.FEATURE_SCAN_ORTHOLOGS_RESULT.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FEATURE_SCAN_ORTHOLOGS_RESULT;
	}

	@Override
	public void setResult(final OrthologScanResult result) {
		this.result = result;
		runCommandOnLoad(new Command() {
			@Override
			public void execute() {
				initScanProperties(result);
				initScanResultProperties(result);
				initOrthologousProteinsTable(result);
				initSequenceAlignmentTable(result);
			}
		});
	}

	private void initScanProperties(OrthologScanResult result) {
		displayScanPropertiesWidget.setProperties(result.getInputParameters());
	}

	private void initScanResultProperties(OrthologScanResult result) {
		List<Parameter> scanResultProperties = new LinkedList<>();
		scanResultProperties.add(new Parameter("Number of orthologous proteins", result.getOrthologs().size()));
		scanResultProperties.add(new Parameter("Number of orthologous proteins with conserved phosphorylation site",
				result.getNrOfConservedPhosphoSites()));
		scanResultProperties
				.add(new Parameter("Evolutionary conservation of phosphorylation site", NumberFormat.getPercentFormat()
						.format((float) result.getNrOfConservedPhosphoSites() / (float) result.getOrthologs().size())));
		displayScanResultPropertiesWidget.setProperties(scanResultProperties);
	}

	private void initOrthologousProteinsTable(OrthologScanResult result) {
		orthologousProteinsFlowPanel.clear();
		OrthologScanResultTable resultTable = new OrthologScanResultTable(result);
		resultTable.setWidth("100%");
		orthologousProteinsFlowPanel.add(resultTable);
	}

	private void initSequenceAlignmentTable(OrthologScanResult result) {
		sequenceAlignmentFlowPanel.clear();
		if (result.getSequenceAlignment() == null) {
			HTML content = new HTML();
			content.setHTML(
					"<span style=\"color: orange\">Could not align sequences! Only a single sequence was available for the scan!</span>");
			sequenceAlignmentFlowPanel.add(content);
		} else {
			SequenceAlignmentTable sequenceAlignmentTable = new SequenceAlignmentTable(result);
			sequenceAlignmentTable.setWidth("100%");
			sequenceAlignmentFlowPanel.add(sequenceAlignmentTable);
		}

	}

	@Override
	public ScanOrthologsResultPageState getState() {
		return new ScanOrthologsResultPageState(result);
	}

	@Override
	public void setState(ScanOrthologsResultPageState state) {
		if (state != null) {
			setResult(state.getOrthologScanResult());
		}
	}
}
