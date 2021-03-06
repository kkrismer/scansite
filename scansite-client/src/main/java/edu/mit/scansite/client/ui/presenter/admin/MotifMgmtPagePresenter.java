package edu.mit.scansite.client.ui.presenter.admin;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.admin.MotifMgmtPageView;
import edu.mit.scansite.client.ui.widgets.admin.HistogramEditWidget;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverAction;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.HistogramCreateAction;
import edu.mit.scansite.shared.dispatch.motif.HistogramRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.HistogramUpdateAction;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.MotifAddAction;
import edu.mit.scansite.shared.dispatch.motif.MotifDeleteAction;
import edu.mit.scansite.shared.dispatch.motif.MotifRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifRetrieverResult;
import edu.mit.scansite.shared.dispatch.motif.MotifUpdateAction;
import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.HistogramEditChangeEvent;
import edu.mit.scansite.shared.event.HistogramEditChangeEventHandler;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifMgmtPagePresenter extends Presenter implements MotifMgmtPageView.Presenter {
	private MotifMgmtPageView view;
	private Map<String, DataSource> dataSources = new HashMap<>();
	private final User user;

	public MotifMgmtPagePresenter(MotifMgmtPageView view, User user) {
		this.view = view;
		this.user = user;
	}

	@Override
	public void bind() {
		EventBus.instance().addHandler(HistogramEditChangeEvent.TYPE, new HistogramEditChangeEventHandler() {
			@Override
			public void onHistogramEditChangeEvent(HistogramEditChangeEvent event) {
				HistogramUpdateAction action = new HistogramUpdateAction();
				action.setHistogramNr(event.getHistogramNr());
				action.setHistogram(event.getHistogram());
				view.getHistogramEditWidget(event.getHistogramNr()).setApplyButtonEnabled(false);
				view.getHistogramEditWidget(event.getHistogramNr()).initImagePanel();
				dispatch.execute(action, new AsyncCallback<HistogramRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR, "Server-side error",
								this.getClass().toString(), caught, 2));
					}

					@Override
					public void onSuccess(HistogramRetrieverResult result) {
						view.getHistogramEditWidget(result.getHistogramNr()).setApplyButtonEnabled(true);
						view.getHistogramEditWidget(result.getHistogramNr()).setHistogram(result.getHistogram());
					}
				});
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		retrieveMotifs(MotifClass.MAMMALIAN);
		retrieveDataSources();
	}

	private void retrieveMotifs(MotifClass motifClass) {
		boolean isAdmin = user == null ? false : user.isAdmin();
		dispatch.execute(
				new LightWeightMotifRetrieverAction(motifClass, user == null ? "" : user.getSessionId(), !isAdmin),
				new AsyncCallback<LightWeightMotifRetrieverResult>() {
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR,
								"Unable to retrieve motifs from database", this.getClass().toString(), caught, 2));
					}

					public void onSuccess(LightWeightMotifRetrieverResult result) {
						view.displayMotifList(result.getMotifs());
					}
				});
	}

	private void retrieveDataSources() {
		dispatch.execute(new DataSourcesRetrieverAction(false), new AsyncCallback<DataSourcesRetrieverResult>() {
			@Override
			public void onFailure(Throwable caught) {
				EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR,
						"Error fetching databases from server", this.getClass().toString(), caught));
			}

			@Override
			public void onSuccess(DataSourcesRetrieverResult result) {
				if (result.getDataSources() != null && !result.getDataSources().isEmpty()) {
					view.hideMessage();
					for (DataSource dataSource : result.getDataSources()) {
						dataSources.put(dataSource.getShortName(), dataSource);
					}
				} else {
					EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR,
							"No data source available", this.getClass().toString(), null));
				}
			}
		});
	}

	@Override
	public void onConfirmButtonClicked(Motif motif) {
		view.setHistogramsVisible(true);
		for (int i = 0; i < view.getHistogramWidgets().size(); ++i) {
			String taxonName = ScansiteConstants.HIST_DEFAULT_TAXON_NAMES[i];
			String dataSourceShortName = ScansiteConstants.HIST_DEFAULT_DATASOURCE_SHORTS[i];

			view.getHistogramEditWidget(i).setApplyButtonEnabled(false);
			view.getHistogramEditWidget(i).init(i, dataSourceShortName, taxonName, motif.getShortName());

			HistogramCreateAction action = new HistogramCreateAction();
			action.setHistogramNr(i);
			action.setMotif(motif);
			action.setTaxonName(taxonName);
			action.setDataSource(dataSources.get(dataSourceShortName));
			dispatch.execute(action, new AsyncCallback<HistogramRetrieverResult>() {
				@Override
				public void onFailure(Throwable caught) {
					EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR, caught.getMessage(),
							this.getClass().toString(), caught));
				}

				@Override
				public void onSuccess(HistogramRetrieverResult result) {
					EventBus.instance()
							.fireEvent(new MessageEvent(MessageEventPriority.INFO,
									"Please adapt the stringency levels to your preferences",
									this.getClass().toString(), null, 2));
					view.getHistogramEditWidget(result.getHistogramNr()).setApplyButtonEnabled(true);
					view.getHistogramEditWidget(result.getHistogramNr()).setHistogram(result.getHistogram());
				}
			});
		}
	}

	@Override
	public void onAddButtonClicked(Motif motif) {
		boolean doContinue = true;
		for (HistogramEditWidget widget : view.getHistogramWidgets()) {
			if (widget.getHistogram() == null) {
				doContinue = false;
			}
		}
		if (doContinue) {
			MotifAddAction action = new MotifAddAction(motif, user == null ? "" : user.getSessionId());
			for (HistogramEditWidget hew : view.getHistogramWidgets()) {
				action.addHistogram(hew.getHistogram());
			}
			dispatch.execute(action, new AsyncCallback<LightWeightMotifRetrieverResult>() {
				@Override
				public void onFailure(Throwable caught) {
					view.setAddButtonEnabled(true);
					view.hideWaitSymbol();
					EventBus.instance()
							.fireEvent(new MessageEvent(MessageEventPriority.ERROR,
									"Could not save motif and corresponding histograms", this.getClass().toString(),
									caught, 2));
				}

				@Override
				public void onSuccess(LightWeightMotifRetrieverResult result) {
					view.displayMotifList(result.getMotifs());
					view.disableEditInputFields();
					view.clearAddInputFields();
					view.setHistogramsVisible(false);
					view.setAddButtonEnabled(true);
					view.hideWaitSymbol();
					EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.INFO,
							"Motif and histograms successfully saved", this.getClass().toString(), null, 2));
				}
			});
		}
	}

	@Override
	public void onUpdateButtonClicked(Motif motif) {
		dispatch.execute(new MotifUpdateAction(motif, user == null ? "" : user.getSessionId()),
				new AsyncCallback<LightWeightMotifRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR, caught.getMessage(),
								this.getClass().toString(), caught, 1));
					}

					@Override
					public void onSuccess(LightWeightMotifRetrieverResult result) {
						view.displayMotifList(result.getMotifs());
						view.disableEditInputFields();
						EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.INFO,
								"Motif successfully updated", this.getClass().toString(), null, 1));
					}
				});
	}

	@Override
	public void onDeleteButtonClicked(LightWeightMotif motif) {
		dispatch.execute(
				new MotifDeleteAction(motif.getId(), motif.getMotifClass(), user == null ? "" : user.getSessionId()),
				new AsyncCallback<LightWeightMotifRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR, caught.getMessage(),
								this.getClass().toString(), caught, 1));
					}

					@Override
					public void onSuccess(LightWeightMotifRetrieverResult result) {
						view.displayMotifList(result.getMotifs());
						view.disableEditInputFields();
						EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.INFO,
								"Motif successfully deleted", this.getClass().toString(), null, 1));
					}
				});
	}

	@Override
	public void onMotifCellListSelectionChange(LightWeightMotif motif) {
		dispatch.execute(new MotifRetrieverAction(motif.getShortName(), motif.getMotifClass(),
				user == null ? "" : user.getSessionId()), new AsyncCallback<MotifRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(new MessageEvent(MessageEventPriority.ERROR, caught.getMessage(),
								this.getClass().toString(), caught, 1));
					}

					@Override
					public void onSuccess(MotifRetrieverResult result) {
						view.displayMotif(result.getMotifs().get(0));
					}
				});
	}

	@Override
	public void onMotifClassSelectionChange(MotifClass motifClass) {
		retrieveMotifs(motifClass);
	}
}
