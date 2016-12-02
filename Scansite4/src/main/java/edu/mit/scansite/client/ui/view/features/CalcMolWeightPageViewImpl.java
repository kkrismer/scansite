package edu.mit.scansite.client.ui.view.features;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.widgets.features.ChooseProteinWidget;
import edu.mit.scansite.client.ui.widgets.features.SiteInSequenceWidget;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.dispatch.features.UtilitiesMwAndPiResult;
import edu.mit.scansite.shared.transferobjects.states.CalcMolWeightPageState;

/**
 * @author Konstantin Krismer
 */
public class CalcMolWeightPageViewImpl extends CalcMolWeightPageView {
	interface CalcMolWeightPageViewUiBinder extends
			UiBinder<Widget, CalcMolWeightPageViewImpl> {
	}

	private static CalcMolWeightPageViewUiBinder uiBinder = GWT
			.create(CalcMolWeightPageViewUiBinder.class);

	private Presenter presenter;

	@UiField
	ChooseProteinWidget chooseProteinWidget;

	@UiField(provided = true)
	TextBox maxSites = new TextBox();

	@UiField
	FlowPanel result;

	public CalcMolWeightPageViewImpl() {
		maxSites.getElement().setId("maxSitesId");
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@UiHandler("maxSites")
	public void onMaxSitesTextBoxKeyUp(KeyUpEvent event) {
		if (maxSites.getValue().length() > 0) {
			Integer maxSitesValue = 5;
			try {
				maxSitesValue = Integer.parseInt(maxSites.getValue());
				if (maxSitesValue > 1000) {
					maxSitesValue = 1000;
				}
			} catch (Exception e) {
				maxSitesValue = 5;
			} finally {
				maxSites.setValue(maxSitesValue.toString());
			}
		}
	}

	@UiHandler("submitButton")
	public void onSubmitButtonClick(ClickEvent event) {
		if (chooseProteinWidget.inputValidation()) {
			hideMessage();
			int maxSitesValue = maxSites.getValue().length() == 0 ? 0 : Integer
					.parseInt(maxSites.getValue());
			presenter.onSubmitButtonClicked(chooseProteinWidget.getProtein(),
					maxSitesValue);
		} else {
			showWarningMessage("Input validation failed");
		}
	}

	@Override
	public String getPageTitle() {
		return "Calculate molecular weight and isoelectric point";
	}

	@Override
	public boolean isMajorNavigationPage() {
		return true;
	}

	@Override
	public String getMajorPageId() {
		return getPageId();
	}

	@Override
	public String getPageId() {
		return NavigationEvent.PageId.FEATURE_CALC_MOLWEIGHT.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.FEATURE_CALC_MOLWEIGHT;
	}

	@Override
	public void setResult(UtilitiesMwAndPiResult result) {
		this.result.clear();
		if (result.isSuccess()) {
			HTML title = new HTML("<h3>Result</h3>");
			this.result.add(title);
			StringBuilder s = new StringBuilder();
			hideMessage();
			s.append(
					"<h4>Molecular weights and isoelectric points for protein ")
					.append(result.getProtein().getIdentifier())
					.append(" with multiple phosphorylation sites")
					.append("</h4>");
			s.append("<table><tr><th class=\"numeric\">Phosphorylations</th><th class=\"numeric\">Molecular Weight</th><th class=\"numeric\">Isoelectric Point</th></tr>");
			for (int i = 0; i <= result.getMaxSites(); ++i) {
				s.append("<tr>");
				s.append("<td class=\"numeric\">").append(i).append("</td>");
				s.append("<td class=\"numeric\">")
						.append(NumberFormat.getFormat("0.000").format(
								result.getMolecularWeights().get(i)))
						.append("</td>");
				s.append("<td class=\"numeric\">")
						.append(NumberFormat.getFormat("0.00").format(
								result.getIsoelectricPoints().get(i)))
						.append("</td>");
				s.append("</tr>");
			}
			s.append("</table>");
			this.result.add(new SiteInSequenceWidget(result.getProtein()));
			this.result.add(new HTML(s.toString()));
		} else {
			showErrorMessage(result.getErrorMessage());
		}
	}

	@Override
	public CalcMolWeightPageState getState() {
		try {
			return new CalcMolWeightPageState(chooseProteinWidget.getState(),
					Integer.parseInt(maxSites.getValue()));
		} catch (NumberFormatException ex) {
			return new CalcMolWeightPageState(chooseProteinWidget.getState(), 5);
		}
	}

	@Override
	public void setState(final CalcMolWeightPageState state) {
		if (state != null) {
			chooseProteinWidget.setState(state.getChooseProteinWidgetState());
			runCommandOnLoad(new Command() {
				@Override
				public void execute() {
					maxSites.setValue(Integer.toString(state
							.getMaxNumberPhosphorylationSites()));
				}
			});
		}
	}
}
