package edu.mit.scansite.client.ui.presenter;

import com.google.gwt.dom.client.Document;

import edu.mit.scansite.client.HistoryToken;
import edu.mit.scansite.client.ui.presenter.admin.MotifGroupMgmtPagePresenter;
import edu.mit.scansite.client.ui.presenter.admin.MotifMgmtPagePresenter;
import edu.mit.scansite.client.ui.presenter.admin.NewsMgmtPagePresenter;
import edu.mit.scansite.client.ui.presenter.admin.UserMgmtPagePresenter;
import edu.mit.scansite.client.ui.presenter.features.CalcCompositionPagePresenter;
import edu.mit.scansite.client.ui.presenter.features.CalcMolWeightPagePresenter;
import edu.mit.scansite.client.ui.presenter.features.PredictLocalizationPagePresenter;
import edu.mit.scansite.client.ui.presenter.features.PredictLocalizationResultPagePresenter;
import edu.mit.scansite.client.ui.presenter.features.ScanDatabasePagePresenter;
import edu.mit.scansite.client.ui.presenter.features.ScanDatabaseResultPagePresenter;
import edu.mit.scansite.client.ui.presenter.features.ScanOrthologsPagePresenter;
import edu.mit.scansite.client.ui.presenter.features.ScanOrthologsResultPagePresenter;
import edu.mit.scansite.client.ui.presenter.features.ScanProteinPagePresenter;
import edu.mit.scansite.client.ui.presenter.features.ScanProteinResultPagePresenter;
import edu.mit.scansite.client.ui.presenter.features.ScanSeqPagePresenter;
import edu.mit.scansite.client.ui.presenter.features.ScanSeqResultPagePresenter;
import edu.mit.scansite.client.ui.presenter.footer.AboutPagePresenter;
import edu.mit.scansite.client.ui.presenter.footer.AdminLoginPagePresenter;
import edu.mit.scansite.client.ui.presenter.footer.AdminPagePresenter;
import edu.mit.scansite.client.ui.presenter.footer.CitingScansitePagePresenter;
import edu.mit.scansite.client.ui.presenter.footer.FAQPagePresenter;
import edu.mit.scansite.client.ui.presenter.footer.FeedbackPagePresenter;
import edu.mit.scansite.client.ui.presenter.main.HomePagePresenter;
import edu.mit.scansite.client.ui.presenter.main.MotifsPagePresenter;
import edu.mit.scansite.client.ui.presenter.main.NewsPagePresenter;
import edu.mit.scansite.client.ui.presenter.tutorial.ScanProteinTutorialPagePresenter;
import edu.mit.scansite.client.ui.presenter.tutorial.SearchDbTutorialPagePresenter;
import edu.mit.scansite.client.ui.presenter.tutorial.SequenceMatchTutorialPagePresenter;
import edu.mit.scansite.client.ui.presenter.tutorial.TutorialPagePresenter;
import edu.mit.scansite.client.ui.view.NotFoundPageView;
import edu.mit.scansite.client.ui.view.PageView;
import edu.mit.scansite.client.ui.view.admin.MotifGroupMgmtPageView;
import edu.mit.scansite.client.ui.view.admin.MotifGroupMgmtPageViewImpl;
import edu.mit.scansite.client.ui.view.admin.MotifMgmtPageView;
import edu.mit.scansite.client.ui.view.admin.MotifMgmtPageViewImpl;
import edu.mit.scansite.client.ui.view.admin.NewsMgmtPageView;
import edu.mit.scansite.client.ui.view.admin.NewsMgmtPageViewImpl;
import edu.mit.scansite.client.ui.view.admin.UserMgmtPageView;
import edu.mit.scansite.client.ui.view.admin.UserMgmtPageViewImpl;
import edu.mit.scansite.client.ui.view.features.CalcCompositionPageView;
import edu.mit.scansite.client.ui.view.features.CalcCompositionPageViewImpl;
import edu.mit.scansite.client.ui.view.features.CalcMolWeightPageView;
import edu.mit.scansite.client.ui.view.features.CalcMolWeightPageViewImpl;
import edu.mit.scansite.client.ui.view.features.PredictLocalizationPageView;
import edu.mit.scansite.client.ui.view.features.PredictLocalizationPageViewImpl;
import edu.mit.scansite.client.ui.view.features.PredictLocalizationResultPageView;
import edu.mit.scansite.client.ui.view.features.PredictLocalizationResultPageViewImpl;
import edu.mit.scansite.client.ui.view.features.ScanDatabasePageView;
import edu.mit.scansite.client.ui.view.features.ScanDatabasePageViewImpl;
import edu.mit.scansite.client.ui.view.features.ScanDatabaseResultPageView;
import edu.mit.scansite.client.ui.view.features.ScanDatabaseResultPageViewImpl;
import edu.mit.scansite.client.ui.view.features.ScanOrthologsPageView;
import edu.mit.scansite.client.ui.view.features.ScanOrthologsPageViewImpl;
import edu.mit.scansite.client.ui.view.features.ScanOrthologsResultPageView;
import edu.mit.scansite.client.ui.view.features.ScanOrthologsResultPageViewImpl;
import edu.mit.scansite.client.ui.view.features.ScanProteinPageView;
import edu.mit.scansite.client.ui.view.features.ScanProteinPageViewImpl;
import edu.mit.scansite.client.ui.view.features.ScanProteinResultPageView;
import edu.mit.scansite.client.ui.view.features.ScanProteinResultPageViewImpl;
import edu.mit.scansite.client.ui.view.features.ScanSeqPageView;
import edu.mit.scansite.client.ui.view.features.ScanSeqPageViewImpl;
import edu.mit.scansite.client.ui.view.features.ScanSeqResultPageView;
import edu.mit.scansite.client.ui.view.features.ScanSeqResultPageViewImpl;
import edu.mit.scansite.client.ui.view.footer.AboutPageView;
import edu.mit.scansite.client.ui.view.footer.AdminLoginPageView;
import edu.mit.scansite.client.ui.view.footer.AdminPageView;
import edu.mit.scansite.client.ui.view.footer.CitingScansitePageView;
import edu.mit.scansite.client.ui.view.footer.FAQPageView;
import edu.mit.scansite.client.ui.view.footer.FeedbackPageView;
import edu.mit.scansite.client.ui.view.main.HomePageView;
import edu.mit.scansite.client.ui.view.main.MotifsPageView;
import edu.mit.scansite.client.ui.view.main.MotifsPageViewImpl;
import edu.mit.scansite.client.ui.view.main.NewsPageView;
import edu.mit.scansite.client.ui.view.tutorial.ScanProteinTutorialPageView;
import edu.mit.scansite.client.ui.view.tutorial.SearchDbTutorialPageView;
import edu.mit.scansite.client.ui.view.tutorial.SequenceMatchTutorialPageView;
import edu.mit.scansite.client.ui.view.tutorial.TutorialPageView;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;
import edu.mit.scansite.shared.dispatch.features.OrthologScanResult;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;
import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;
import edu.mit.scansite.shared.dispatch.features.SequenceMatchResult;
import edu.mit.scansite.shared.event.NavigationEvent;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.states.CalcMolWeightPageState;
import edu.mit.scansite.shared.transferobjects.states.ChooseProteinWidgetState;
import edu.mit.scansite.shared.transferobjects.states.PredictLocalizationPageState;
import edu.mit.scansite.shared.transferobjects.states.PredictLocalizationResultPageState;
import edu.mit.scansite.shared.transferobjects.states.ScanDatabasePageState;
import edu.mit.scansite.shared.transferobjects.states.ScanDatabaseResultPageState;
import edu.mit.scansite.shared.transferobjects.states.ScanOrthologsPageState;
import edu.mit.scansite.shared.transferobjects.states.ScanOrthologsResultPageState;
import edu.mit.scansite.shared.transferobjects.states.ScanProteinPageState;
import edu.mit.scansite.shared.transferobjects.states.ScanProteinResultPageState;
import edu.mit.scansite.shared.transferobjects.states.ScanSeqPageState;
import edu.mit.scansite.shared.transferobjects.states.ScanSeqResultPageState;
import edu.mit.scansite.shared.transferobjects.states.State;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class PresenterFactory {
	private static PresenterFactory instance;
	private static final String titleSuffix = " - Scansite 4.0";

	private PresenterFactory() {
	}

	public static PresenterFactory instance() {
		if (instance == null) {
			instance = new PresenterFactory();
		}
		return instance;
	}

	public Presenter getPresenter(HistoryToken histToken,
			NavigationBarPresenter navigationBarPresenter,
			BreadcrumbsPresenter breadcrumbsPresenter, User user, State state) {
		String token = histToken.getCurrentToken();
		Presenter presenter = null;
		PageView view = null;
		if (token.equalsIgnoreCase(NavigationEvent.PageId.HOME.getId())) {
			view = new HomePageView();
			presenter = new HomePagePresenter((HomePageView) view);
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.NEWS.getId())) {
			view = new NewsPageView();
			presenter = new NewsPagePresenter((NewsPageView) view);
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.FEATURE_SCAN_PROTEIN
						.getId())) {
			view = new ScanProteinPageViewImpl(user);
			ScanProteinPageView scanProteinPageView = (ScanProteinPageView) view;
			if (state instanceof ScanProteinPageState) {
				presenter = new ScanProteinPagePresenter(scanProteinPageView,
						(ScanProteinPageState) state, user);
			} else {
				presenter = new ScanProteinPagePresenter(scanProteinPageView,
						null, user);
			}
			scanProteinPageView
					.setPresenter((ScanProteinPagePresenter) presenter);
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.FEATURE_SCAN_PROTEIN_RESULT
						.getId())) {
			view = new ScanProteinResultPageViewImpl(user);
			if (state instanceof ScanProteinResultPageState) {
				presenter = new ScanProteinResultPagePresenter(
						(ScanProteinResultPageView) view,
						(ScanProteinResultPageState) state);
			} else {
				presenter = new ScanProteinResultPagePresenter(
						(ScanProteinResultPageView) view);
			}
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.FEATURE_SCAN_DB
						.getId())) {
			view = new ScanDatabasePageViewImpl(user);
			ScanDatabasePageView scanDatabasePageView = (ScanDatabasePageView) view;
			if (state instanceof ScanDatabasePageState) {
				presenter = new ScanDatabasePagePresenter(scanDatabasePageView,
						(ScanDatabasePageState) state, user); //todo check
			} else {
				presenter = new ScanDatabasePagePresenter(scanDatabasePageView,
						null, user);
			}
			scanDatabasePageView
					.setPresenter((ScanDatabasePagePresenter) presenter);
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.FEATURE_SCAN_DB_RESULT
						.getId())) {
			view = new ScanDatabaseResultPageViewImpl(user);
			if (state instanceof ScanDatabaseResultPageState) {
				presenter = new ScanDatabaseResultPagePresenter(
						(ScanDatabaseResultPageView) view,
						(ScanDatabaseResultPageState) state);
			} else {
				presenter = new ScanDatabaseResultPagePresenter(
						(ScanDatabaseResultPageView) view);
			}
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.FEATURE_SCAN_SEQ
						.getId())) {
			view = new ScanSeqPageViewImpl();
			ScanSeqPageView scanSeqPageView = (ScanSeqPageView) view;
			if (state instanceof ScanSeqPageState) {
				presenter = new ScanSeqPagePresenter(scanSeqPageView,
						(ScanSeqPageState) state, user);
			} else {
				presenter = new ScanSeqPagePresenter(scanSeqPageView, null,
						user);
			}
			scanSeqPageView.setPresenter((ScanSeqPagePresenter) presenter);
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.FEATURE_SCAN_SEQ_RESULT
						.getId())) {
			view = new ScanSeqResultPageViewImpl(user);
			if (state instanceof ScanSeqResultPageState) {
				presenter = new ScanSeqResultPagePresenter(
						(ScanSeqResultPageView) view,
						(ScanSeqResultPageState) state);
			} else {
				presenter = new ScanSeqResultPagePresenter(
						(ScanSeqResultPageView) view);
			}
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.FEATURE_SCAN_ORTHOLOGS
						.getId())) {
			view = new ScanOrthologsPageViewImpl();
			ScanOrthologsPageView scanOrthologsPageView = (ScanOrthologsPageView) view;
			if (state instanceof ScanOrthologsPageState) {
				presenter = new ScanOrthologsPagePresenter(
						scanOrthologsPageView, (ScanOrthologsPageState) state,
						user);
			} else {
				presenter = new ScanOrthologsPagePresenter(
						scanOrthologsPageView, null, user);
			}
			scanOrthologsPageView
					.setPresenter((ScanOrthologsPagePresenter) presenter);
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.FEATURE_SCAN_ORTHOLOGS_RESULT
						.getId())) {
			view = new ScanOrthologsResultPageViewImpl();
			if (state instanceof ScanOrthologsResultPageState) {
				presenter = new ScanOrthologsResultPagePresenter(
						(ScanOrthologsResultPageView) view,
						(ScanOrthologsResultPageState) state);
			} else {
				presenter = new ScanOrthologsResultPagePresenter(
						(ScanOrthologsResultPageView) view);
			}
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.FEATURE_PREDICT_LOCALIZATION
						.getId())) {
			view = new PredictLocalizationPageViewImpl();
			PredictLocalizationPageView predictLocalizationPageView = (PredictLocalizationPageView) view;
			if (state instanceof PredictLocalizationPageState) {
				presenter = new PredictLocalizationPagePresenter(
						predictLocalizationPageView, 
						(PredictLocalizationPageState) state, user);
			} else {
				presenter = new PredictLocalizationPagePresenter(
						predictLocalizationPageView, null, user);
			}
			predictLocalizationPageView
					.setPresenter((PredictLocalizationPagePresenter) presenter);
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.FEATURE_PREDICT_LOCALIZATION_RESULT
						.getId())) {
			view = new PredictLocalizationResultPageViewImpl();
			if (state instanceof PredictLocalizationResultPageState) {
				presenter = new PredictLocalizationResultPagePresenter(
						(PredictLocalizationResultPageView) view,
						(PredictLocalizationResultPageState) state);
			} else {
				presenter = new PredictLocalizationResultPagePresenter(
						(PredictLocalizationResultPageView) view);
			}
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.FEATURE_CALC_MOLWEIGHT
						.getId())) {
			view = new CalcMolWeightPageViewImpl();
			CalcMolWeightPageView calcMolWeightPageView = (CalcMolWeightPageView) view;
			if (state instanceof CalcMolWeightPageState) {
				presenter = new CalcMolWeightPagePresenter(
						calcMolWeightPageView, (CalcMolWeightPageState) state);
			} else {
				presenter = new CalcMolWeightPagePresenter(
						calcMolWeightPageView, null);
			}
			calcMolWeightPageView
					.setPresenter((CalcMolWeightPagePresenter) presenter);
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.FEATURE_CALC_COMPOSITION
						.getId())) {
			view = new CalcCompositionPageViewImpl();
			CalcCompositionPageView calcCompositionPageView = (CalcCompositionPageView) view;
			if (state instanceof ChooseProteinWidgetState) {
				presenter = new CalcCompositionPagePresenter(
						calcCompositionPageView,
						(ChooseProteinWidgetState) state);
			} else {
				presenter = new CalcCompositionPagePresenter(
						calcCompositionPageView, null);
			}
			calcCompositionPageView
					.setPresenter((CalcCompositionPagePresenter) presenter);
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.MOTIFS.getId())) {
			view = new MotifsPageViewImpl(user);
			presenter = new MotifsPagePresenter((MotifsPageView) view);
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.TUTORIAL.getId())) {
			view = new TutorialPageView();
			presenter = new TutorialPagePresenter((TutorialPageView) view);
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.PSCAN_TUTORIAL.getId())) {
			view = new ScanProteinTutorialPageView();
			presenter = new ScanProteinTutorialPagePresenter((ScanProteinTutorialPageView) view);
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.DBSEARCH_TUTORIAL.getId())) {
			view = new SearchDbTutorialPageView();
			presenter = new SearchDbTutorialPagePresenter((SearchDbTutorialPageView) view);
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.SEQMATCH_TUTORIAL.getId())) {
			view = new SequenceMatchTutorialPageView();
			presenter = new SequenceMatchTutorialPagePresenter((SequenceMatchTutorialPageView) view);
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.ABOUT.getId())) {
			view = new AboutPageView();
			presenter = new AboutPagePresenter((AboutPageView) view);
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.FAQ.getId())) {
			view = new FAQPageView();
			presenter = new FAQPagePresenter((FAQPageView) view);
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.CITE.getId())) {
			view = new CitingScansitePageView();
			presenter = new CitingScansitePagePresenter(
					(CitingScansitePageView) view);
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.FEEDBACK
				.getId())) {
			view = new FeedbackPageView();
			presenter = new FeedbackPagePresenter((FeedbackPageView) view);
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.ADMIN.getId())) {
			if (user != null) {
				view = new AdminPageView(user);
				presenter = new AdminPagePresenter((AdminPageView) view);
			} else {
				view = new AdminLoginPageView();
				presenter = new AdminLoginPagePresenter(
						(AdminLoginPageView) view);
			}
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.ADMIN_NEWS
				.getId())) {
			if (user != null) {
				if(user.isAdmin()) {
					view = new NewsMgmtPageViewImpl(user);
					NewsMgmtPageView newsMgmtPageView = (NewsMgmtPageView) view;
					presenter = new NewsMgmtPagePresenter(newsMgmtPageView);
					newsMgmtPageView
							.setPresenter((NewsMgmtPagePresenter) presenter);
				} else {
					view = new NotFoundPageView();
					presenter = new NotFoundPagePresenter((NotFoundPageView) view);
				}
			} else {
				view = new AdminLoginPageView();
				presenter = new AdminLoginPagePresenter(
						(AdminLoginPageView) view);
			}
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.ADMIN_USER
				.getId())) {
			if (user != null) {
				if(user.isAdmin()) {
					view = new UserMgmtPageViewImpl(user);
					UserMgmtPageView userMgmtPageView = (UserMgmtPageView) view;
					presenter = new UserMgmtPagePresenter(userMgmtPageView);
					userMgmtPageView
							.setPresenter((UserMgmtPagePresenter) presenter);
				} else {
					view = new NotFoundPageView();
					presenter = new NotFoundPagePresenter((NotFoundPageView) view);
				}

			} else {
				view = new AdminLoginPageView();
				presenter = new AdminLoginPagePresenter(
						(AdminLoginPageView) view);
			}
		} else if (token
				.equalsIgnoreCase(NavigationEvent.PageId.ADMIN_MOTIF_GROUP
						.getId())) {
			if (user != null) {
				if(user.isAdmin()) {
					view = new MotifGroupMgmtPageViewImpl(user);
					MotifGroupMgmtPageView motifGroupMgmtPageView = (MotifGroupMgmtPageView) view;
					presenter = new MotifGroupMgmtPagePresenter(
							motifGroupMgmtPageView);
					motifGroupMgmtPageView
							.setPresenter((MotifGroupMgmtPagePresenter) presenter);
				} else {
					view = new NotFoundPageView();
					presenter = new NotFoundPagePresenter((NotFoundPageView) view);
				}
			} else {
				view = new AdminLoginPageView();
				presenter = new AdminLoginPagePresenter(
						(AdminLoginPageView) view);
			}
		} else if (token.equalsIgnoreCase(NavigationEvent.PageId.ADMIN_MOTIF
				.getId())) {
			if (user != null) {
				if(user.isAdmin() || user.isCollaborator()) {
					view = new MotifMgmtPageViewImpl(user);
					MotifMgmtPageView motifMgmtPageView = (MotifMgmtPageView) view;
					presenter = new MotifMgmtPagePresenter(motifMgmtPageView, user);
					motifMgmtPageView
							.setPresenter((MotifMgmtPagePresenter) presenter);
				} else {
					view = new NotFoundPageView();
					presenter = new NotFoundPagePresenter((NotFoundPageView) view);
				}
			} else {
				view = new AdminLoginPageView();
				presenter = new AdminLoginPagePresenter(
						(AdminLoginPageView) view);
			}
		} else {
			view = new NotFoundPageView();
			presenter = new NotFoundPagePresenter((NotFoundPageView) view);
		}
		update(view, navigationBarPresenter, breadcrumbsPresenter);
		return presenter;
	}

	private void update(PageView view,
			NavigationBarPresenter navigationBarPresenter,
			BreadcrumbsPresenter breadcrumbsPresenter) {
		if (view != null) {
			updateTitle(view.getPageTitle());
			updateNavigationBar(navigationBarPresenter, view.getMajorPageId());
			updateBreadcrumbs(breadcrumbsPresenter, view.getBreadcrumbs());
		}
	}

	private void updateTitle(String title) {
		if (Document.get() != null) {
			Document.get().setTitle(title + titleSuffix);
		}
	}

	private void updateBreadcrumbs(BreadcrumbsPresenter breadcrumbsPresenter,
			String breadcrumbsHTML) {
		breadcrumbsPresenter.set(breadcrumbsHTML);
	}

	private void updateNavigationBar(
			NavigationBarPresenter navigationBarPresenter, String majorPageId) {
		navigationBarPresenter.select(majorPageId);
	}

	public Presenter getScanProteinResultPagePresenter(
			ProteinScanResult result,
			NavigationBarPresenter navigationBarPresenter,
			BreadcrumbsPresenter breadcrumbsPresenter, User user) {
		PageView view = new ScanProteinResultPageViewImpl(user);
		update(view, navigationBarPresenter, breadcrumbsPresenter);
		return new ScanProteinResultPagePresenter(
				(ScanProteinResultPageViewImpl) view, result);
	}

	public Presenter getScanDatabaseResultPagePresenter(
			DatabaseScanResult result,
			NavigationBarPresenter navigationBarPresenter,
			BreadcrumbsPresenter breadcrumbsPresenter, User user) {
		PageView view = new ScanDatabaseResultPageViewImpl(user);
		update(view, navigationBarPresenter, breadcrumbsPresenter);
		return new ScanDatabaseResultPagePresenter(
				(ScanDatabaseResultPageView) view, result);
	}

	public Presenter getSequenceMatchResultPagePresenter(
			SequenceMatchResult result,
			NavigationBarPresenter navigationBarPresenter,
			BreadcrumbsPresenter breadcrumbsPresenter, User user) {
		PageView view = new ScanSeqResultPageViewImpl(user);
		update(view, navigationBarPresenter, breadcrumbsPresenter);
		return new ScanSeqResultPagePresenter((ScanSeqResultPageView) view,
				result);
	}

	public Presenter getScanOrthologsResultPagePresenter(
			OrthologScanResult result,
			NavigationBarPresenter navigationBarPresenter,
			BreadcrumbsPresenter breadcrumbsPresenter) {
		PageView view = new ScanOrthologsResultPageViewImpl();
		update(view, navigationBarPresenter, breadcrumbsPresenter);
		return new ScanOrthologsResultPagePresenter(
				(ScanOrthologsResultPageView) view, result);
	}

	public Presenter getPredictLocalizationResultPagePresenter(
			PredictLocalizationResult result,
			NavigationBarPresenter navigationBarPresenter,
			BreadcrumbsPresenter breadcrumbsPresenter) {
		PageView view = new PredictLocalizationResultPageViewImpl();
		update(view, navigationBarPresenter, breadcrumbsPresenter);
		return new PredictLocalizationResultPagePresenter(
				(PredictLocalizationResultPageView) view, result);
	}
}