package edu.mit.scansite.client.ui.view.features;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.widgets.features.DisplayGeneralPropertiesWidget;
import edu.mit.scansite.client.ui.widgets.features.DisplayMotifSelectionWidget;
import edu.mit.scansite.client.ui.widgets.features.DomainInformationWidget;
import edu.mit.scansite.client.ui.widgets.features.ProteinScanResultSiteTable;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.URIs;
import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;
import edu.mit.scansite.shared.transferobjects.*;
import edu.mit.scansite.shared.transferobjects.states.ScanProteinResultPageState;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ScanProteinResultPageViewImpl extends ScanProteinResultPageView {
    interface ScanProteinResultPageViewImplUiBinder extends
            UiBinder<Widget, ScanProteinResultPageViewImpl> {
    }

    private static ScanProteinResultPageViewImplUiBinder uiBinder = GWT
            .create(ScanProteinResultPageViewImplUiBinder.class);

    private ProteinScanResult result = null;
    private User user;

    @UiField
    DisplayGeneralPropertiesWidget displayProteinPropertiesWidget;

    @UiField
    DisplayMotifSelectionWidget displayMotifSelectionWidget;

    @UiField
    DisplayGeneralPropertiesWidget displayScanPropertiesWidget;

    @UiField
    FlowPanel motifSitesTable;

    @UiField
    FlowPanel domainPanel;

    @UiField
    InputElement disphosSeqHidden;

    @UiField
    Anchor downloadResultAnchor;

    public ScanProteinResultPageViewImpl(User user) {
        this.user = user;
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public String getPageTitle() {
        return "Scan Protein for Motifs - Result";
    }

    @Override
    public boolean isMajorNavigationPage() {
        return false;
    }

    @Override
    public String getMajorPageId() {
        return NavigationEvent.PageId.FEATURE_SCAN_PROTEIN.getId();
    }

    @Override
    public String getPageId() {
        return NavigationEvent.PageId.FEATURE_SCAN_PROTEIN_RESULT.getId();
    }

    @Override
    public String getBreadcrumbs() {
        return Breadcrumbs.FEATURE_SCAN_PROTEIN_RESULT;
    }

    @Override
    public void setResult(final ProteinScanResult result) {
        this.result = result;
        Command setResult = new Command() {
            @Override
            public void execute() {
                if (result.isSuccess()) {
                    setProteinProperties(result.getResults().getProtein(),
                            result.getResults().getProteinLocalization());
                    setScanProperties(result.getResults());
                    setProteinPlot(result.getResults());
                    setMotifSitesTable(result.getResults());
                    disphosSeqHidden.setValue(result.getResults().getProtein().getSequence());
                    downloadResultAnchor.setHref(result.getResults().getResultFilePath());
                } else {
                    showErrorMessage(result.getFailureMessage());
                }
            }
        };
        runCommandOnLoad(setResult);
    }

    private void setProteinProperties(LightWeightProtein protein,
                                      LightWeightLocalization localization) {
        String dbLink = URIs.getDirectIdentifierInfoLink(protein);
        String phosphosite = "";
        HashMap<String, Set<String>> annotations = null;

        if (protein.getDataSource() != null
                && protein.getDataSource().getShortName()
                .equalsIgnoreCase("swissprot")) {
            if (protein instanceof Protein) {
                annotations = ((Protein) protein).getAnnotations();
            }
            Set<String> anns = annotations.get("accession");
            if (anns != null && !anns.isEmpty()) {
                phosphosite = ", see <a href='"
                        + URIs.directPhosphositeProteinLink(anns.iterator()
                        .next()) + "' target='_blank'>PhosphoSite</a>";
            }
        }

        List<Parameter> parameters = new LinkedList<>();
        parameters.add(new Parameter("Identifier", protein.getIdentifier()
                + (dbLink == null ? "" : " (see <a target='_blank' href=\""
                + dbLink + "\">"
                + protein.getDataSource().getDisplayName() + "</a>"
                + phosphosite + ")")));

        if (annotations != null && !annotations.isEmpty()) {
            for (String type : annotations.keySet()) {
                String txt = "";
                for (String ann : annotations.get(type)) {
                    if (ann != null && !ann.isEmpty()) {
                        if (!txt.isEmpty()) {
                            txt += ", ";
                        }
                        txt += ann;
                    }
                }

                if (type.equalsIgnoreCase("Keyword")) {
                    parameters.add(new Parameter(type.substring(0, 1)
                            .toUpperCase() + type.substring(1) + "s", txt,
                            true, false));
                } else {
                    parameters.add(new Parameter(type.substring(0, 1)
                            .toUpperCase() + type.substring(1) + "s", txt));
                }
            }
        }

        if (protein instanceof Protein) {
            Double mw = ((Protein) protein).getMolecularWeight() / 1000;
            parameters.add(new Parameter("Molecular weight",
                    String.valueOf(NumberFormat.getFormat("0.00").format(mw)) + " kDa"));
            parameters.add(new Parameter("Isoelectric point",
                    ((Protein) protein).getpI()));
        }
        if (localization != null) {
            parameters.add(new Parameter("Subcellular localization",
                    localization.getType().getName()));
        } else {
            parameters
                    .add(new Parameter("Subcellular localization", "Unknown", false, true));
        }
        displayProteinPropertiesWidget.setProperties(parameters);
    }

    private void setScanProperties(ScanResults result) {
        displayMotifSelectionWidget.setMotifSelection(result
                .getMotifSelection());
        List<Parameter> parameters = new LinkedList<>();
        parameters.add(new Parameter("Domains requested", result
                .isShowDomains() ? "Yes" : "No"));
        parameters.add(new Parameter("Stringency", result
                .getHistogramThreshold().getName()));
        parameters.add(new Parameter("Reference histogram", result
                .getHistogramTaxonName()
                + " ("
                + result.getHistogramDataSourceSelection().getDisplayName()
                + ")"));
        parameters.add(new Parameter("Number of predicted motif sites", result
                .getHits().size()));
        parameters.add(new Parameter("Protein data source", result.getProtein()
                .getDataSource().getDisplayName()));
        if (result.getLocalizationDataSource() != null) {
            parameters.add(new Parameter("Localization data source", result
                    .getLocalizationDataSource().getDisplayName()));
        } else {
            parameters.add(new Parameter("Localization data source",
                    "Not available for specified protein identifier type ("
                            + result.getProtein().getDataSource()
                            .getIdentifierType().getName() + ")", false, true));
        }

        displayScanPropertiesWidget.setProperties(parameters);
    }

    private void setProteinPlot(ScanResults result) {
        String domainDataTable = "";
        if (result.isShowDomains() && result.getDomainPositions() != null
                && !result.getDomainPositions().isEmpty()) {
            domainPanel.add(new DomainInformationWidget(result.getProtein(),
                    result.getDomainPositions()));


//        <table>
//            <tr>
//                <td><strong>Full Domain Name</strong></td>
//                <td><strong>IPR Code</strong></td>
//                <td><strong>Alternative Name</strong></td>
//            </tr>
//            <tr>
//                <td></td>
//                <td><a href="http://www.ebi.ac.uk/interpro/entry/"></a></td>
//                <td></td>
//            </tr>
//        </table>

            List<DomainPosition> domainPositions = result.getDomainPositions();
            domainDataTable += "<br/><br/>";
            domainDataTable += "<table><tr>";
            domainDataTable += "<td><strong>Full Domain Name</strong></td>";
            domainDataTable += "<td><strong>IPR Code</strong></td>";
            domainDataTable += "<td><strong>Alternative Name</strong></td>";

            for (DomainPosition domainPosition : domainPositions) {
                domainDataTable += "</tr><tr><td>" + domainPosition.getName() + "</td>";
                domainDataTable += "<td><a target=\"_blank\" href=\"http://www.ebi.ac.uk/interpro/entry/"
                        + domainPosition.getIPRCode() + "\">"
                        + domainPosition.getIPRCode() + "</a></td>";
                domainDataTable += "<td>" + domainPosition.getAlternativeName() + "</td>";
            }

            domainDataTable += "</tr></table><br/>";
        }

        if (result.getImagePath() != null) {
            DOM.getElementById("proteinPlot").setInnerHTML(
                    "<img alt=\"protein plot\" src=\"" + result.getImagePath() + "\"/>" + domainDataTable);
        }
    }

    private void setMotifSitesTable(ScanResults result) {
        ProteinScanResultSiteTable resultTable = new ProteinScanResultSiteTable(
                result, user);
        resultTable.setWidth("100%");
        motifSitesTable.clear();
        motifSitesTable.add(resultTable);
    }

    @Override
    public ScanProteinResultPageState getState() {
        return new ScanProteinResultPageState(result);
    }

    @Override
    public void setState(ScanProteinResultPageState state) {
        if (state != null) {
            setResult(state.getProteinScanResult());
        }
    }
}
