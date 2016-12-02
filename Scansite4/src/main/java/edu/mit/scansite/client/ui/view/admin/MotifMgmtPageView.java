package edu.mit.scansite.client.ui.view.admin;

import java.util.List;

import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.widgets.admin.HistogramEditWidget;
import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;

/**
 * @author Konstantin Krismer
 */
public abstract class MotifMgmtPageView extends PageView {

	public interface Presenter {
		public void onAddButtonClicked(Motif motif);

		public void onConfirmButtonClicked(Motif motif);

		public void onUpdateButtonClicked(Motif motif);

		public void onDeleteButtonClicked(LightWeightMotif motif);

		public void onMotifCellListSelectionChange(LightWeightMotif motif);

		public void onMotifClassSelectionChange(MotifClass motifClass);
	}

	public abstract void setPresenter(Presenter presenter);

	public abstract void displayMotifList(List<LightWeightMotif> motifs);

	public abstract void disableEditInputFields();

	public abstract void displayMotif(Motif motif);

	public abstract HistogramEditWidget getHistogramEditWidget(int id);

	public abstract List<HistogramEditWidget> getHistogramWidgets();
	
	public abstract void setHistogramsVisible(boolean visible);
}
