package edu.mit.scansite.client.ui.view.main;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.widgets.motifs.MotifGroupInfoWidget;
import edu.mit.scansite.client.ui.widgets.motifs.MotifInfoWidget;
import edu.mit.scansite.shared.Breadcrumbs;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Konstantin Krismer
 */
public class MotifsPageViewImpl extends MotifsPageView {

	@UiTemplate("MotifsPageView.ui.xml")
	interface MotifsPageViewUiBinder extends
			UiBinder<Widget, MotifsPageViewImpl> {
	}

	private static MotifsPageViewUiBinder uiBinder = GWT
			.create(MotifsPageViewUiBinder.class);

	@UiField
	DivElement dataSourceInfo;
	@UiField(provided = true)
	MotifInfoWidget motifInfo;
	@UiField(provided = true)
	MotifGroupInfoWidget motifGroupInfo;

	public MotifsPageViewImpl(User user) {
		motifInfo = new MotifInfoWidget(user);
		motifGroupInfo = new MotifGroupInfoWidget(user);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getPageTitle() {
		return "Databases and Motifs";
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
		return NavigationEvent.PageId.MOTIFS.getId();
	}

	@Override
	public String getBreadcrumbs() {
		return Breadcrumbs.MOTIFS;
	}

	@Override
	public void setDataSourceInfo(Map<DataSource, Integer> dataSourceSizes) {
		SafeHtmlBuilder html = new SafeHtmlBuilder();
		html.appendHtmlConstant("<table>");
		html.appendHtmlConstant("<tr><th>Name</th><th>Data source type</th><th class=\"numeric\">Size (# of proteins)</th><th>Version description</th><th>Identifier type</th></tr>");

		List<DataSource> dataSources = new LinkedList<>(
				dataSourceSizes.keySet());
		Collections.sort(dataSources, new Comparator<DataSource>() {
			@Override
			public int compare(DataSource arg0, DataSource arg1) {
				return arg0.getDisplayName().compareTo(arg1.getDisplayName());
			}
		});

		for (DataSource dataSource : dataSources) {
			html.appendHtmlConstant("<tr>");
			html.appendHtmlConstant("<td>")
					.appendHtmlConstant(dataSource.getDisplayName())
					.appendHtmlConstant("</td>");

			html.appendHtmlConstant("<td>")
					.appendEscaped(dataSource.getType().getDisplayName())
					.appendHtmlConstant("</td>");
			html.appendHtmlConstant("<td class=\"numeric\">")
					.appendHtmlConstant(
							Integer.toString(dataSourceSizes.get(dataSource)))
					.appendHtmlConstant("</td>");
			html.appendHtmlConstant("<td>")
					.appendHtmlConstant(dataSource.getVersion())
					.appendHtmlConstant("</td>");
			html.appendHtmlConstant("<td>")
					.appendHtmlConstant(
							dataSource.getIdentifierType().getName())
					.appendHtmlConstant("</td>");
			html.appendHtmlConstant("</tr>");
		}
		html.appendHtmlConstant("</table>");
		dataSourceInfo.setInnerSafeHtml(html.toSafeHtml());
	}

	@Override
	public MotifInfoWidget getMotifInfoWidget() {
		return motifInfo;
	}

	@Override
	public MotifGroupInfoWidget getMotifGroupInfoWidget() {
		return motifGroupInfo;
	}
}
