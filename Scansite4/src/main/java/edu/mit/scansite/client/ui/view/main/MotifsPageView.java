package edu.mit.scansite.client.ui.view.main;

import java.util.Map;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.motifs.MotifGroupInfoWidget;
import edu.mit.scansite.client.ui.widgets.motifs.MotifInfoWidget;
import edu.mit.scansite.shared.transferobjects.DataSource;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public abstract class MotifsPageView extends PageView {

	public abstract void setDataSourceInfo(Map<DataSource, Integer> dataSourceSizes);

	public abstract MotifInfoWidget getMotifInfoWidget();

	public abstract MotifGroupInfoWidget getMotifGroupInfoWidget();
}
