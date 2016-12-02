package edu.mit.scansite.server.dispatch;

import net.customware.gwt.dispatch.server.guice.ActionHandlerModule;
import edu.mit.scansite.server.dispatch.handler.datasource.DataSourceSizesRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.datasource.DataSourcesRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.datasourcetype.DataSourceTypesRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.features.DatabaseScanHandler;
import edu.mit.scansite.server.dispatch.handler.features.DbSearchHistogramRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.features.DomainPositionsGetHandler;
import edu.mit.scansite.server.dispatch.handler.features.GetNewProteinPlotHandler;
import edu.mit.scansite.server.dispatch.handler.features.OrthologScanMotifHandler;
import edu.mit.scansite.server.dispatch.handler.features.OrthologScanSequencePatternHandler;
import edu.mit.scansite.server.dispatch.handler.features.OrthologyCheckOracleHandler;
import edu.mit.scansite.server.dispatch.handler.features.PredictLocalizationMotifProteinPairHandler;
import edu.mit.scansite.server.dispatch.handler.features.PredictMotifsLocalizationHandler;
import edu.mit.scansite.server.dispatch.handler.features.PredictProteinsLocalizationHandler;
import edu.mit.scansite.server.dispatch.handler.features.ProteinCheckHandler;
import edu.mit.scansite.server.dispatch.handler.features.ProteinCheckOracleHandler;
import edu.mit.scansite.server.dispatch.handler.features.ProteinRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.features.ProteinScanHandler;
import edu.mit.scansite.server.dispatch.handler.features.SequenceMatchHandler;
import edu.mit.scansite.server.dispatch.handler.features.ShowMotifsForExpectedSiteGetHandler;
import edu.mit.scansite.server.dispatch.handler.features.ShowSequenceMatchesHtmlGetHandler;
import edu.mit.scansite.server.dispatch.handler.features.SiteInSequenceHtmlGetHandler;
import edu.mit.scansite.server.dispatch.handler.features.UtilitiesMwAndPiHandler;
import edu.mit.scansite.server.dispatch.handler.history.RetrieveHistoryStateHandler;
import edu.mit.scansite.server.dispatch.handler.history.StoreHistoryStateHandler;
import edu.mit.scansite.server.dispatch.handler.identifiertype.IdentifierTypeRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.motif.HistogramCreateHandler;
import edu.mit.scansite.server.dispatch.handler.motif.HistogramRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.motif.HistogramUpdateHandler;
import edu.mit.scansite.server.dispatch.handler.motif.LightWeightMotifGroupRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.motif.LightWeightMotifRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.motif.MotifAddHandler;
import edu.mit.scansite.server.dispatch.handler.motif.MotifDeleteHandler;
import edu.mit.scansite.server.dispatch.handler.motif.MotifGetHandler;
import edu.mit.scansite.server.dispatch.handler.motif.MotifGroupAddHandler;
import edu.mit.scansite.server.dispatch.handler.motif.MotifGroupDeleteHandler;
import edu.mit.scansite.server.dispatch.handler.motif.MotifGroupRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.motif.MotifGroupUpdateHandler;
import edu.mit.scansite.server.dispatch.handler.motif.MotifNumbersRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.motif.MotifRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.motif.MotifUpdateHandler;
import edu.mit.scansite.server.dispatch.handler.news.NewsAddHandler;
import edu.mit.scansite.server.dispatch.handler.news.NewsDeleteHandler;
import edu.mit.scansite.server.dispatch.handler.news.NewsRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.news.NewsUpdateHandler;
import edu.mit.scansite.server.dispatch.handler.user.LoginHandler;
import edu.mit.scansite.server.dispatch.handler.user.UserAddHandler;
import edu.mit.scansite.server.dispatch.handler.user.UserDeleteHandler;
import edu.mit.scansite.server.dispatch.handler.user.UserPrivilegesGetHandler;
import edu.mit.scansite.server.dispatch.handler.user.UserRetrieverHandler;
import edu.mit.scansite.server.dispatch.handler.user.UserUpdateHandler;
import edu.mit.scansite.shared.dispatch.datasource.DataSourceSizesRetrieverAction;
import edu.mit.scansite.shared.dispatch.datasource.DataSourceTypesRetrieverAction;
import edu.mit.scansite.shared.dispatch.datasource.DataSourcesRetrieverAction;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanAction;
import edu.mit.scansite.shared.dispatch.features.DbSearchHistogramRetrieverAction;
import edu.mit.scansite.shared.dispatch.features.DomainPositionsGetAction;
import edu.mit.scansite.shared.dispatch.features.GetNewProteinPlotAction;
import edu.mit.scansite.shared.dispatch.features.IdentifierTypeRetrieverAction;
import edu.mit.scansite.shared.dispatch.features.OrthologScanMotifAction;
import edu.mit.scansite.shared.dispatch.features.OrthologScanSequencePatternAction;
import edu.mit.scansite.shared.dispatch.features.OrthologyCheckOracleAction;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationMotifProteinPairAction;
import edu.mit.scansite.shared.dispatch.features.PredictMotifsLocalizationAction;
import edu.mit.scansite.shared.dispatch.features.PredictProteinsLocalizationAction;
import edu.mit.scansite.shared.dispatch.features.ProteinCheckAction;
import edu.mit.scansite.shared.dispatch.features.ProteinCheckOracleAction;
import edu.mit.scansite.shared.dispatch.features.ProteinRetrieverAction;
import edu.mit.scansite.shared.dispatch.features.ProteinScanAction;
import edu.mit.scansite.shared.dispatch.features.SequenceMatchAction;
import edu.mit.scansite.shared.dispatch.features.ShowMotifsForExpectedSiteGetAction;
import edu.mit.scansite.shared.dispatch.features.ShowSequenceMatchesHtmlGetAction;
import edu.mit.scansite.shared.dispatch.features.SiteInSequenceHtmlGetAction;
import edu.mit.scansite.shared.dispatch.features.UtilitiesMwAndPiAction;
import edu.mit.scansite.shared.dispatch.history.RetrieveHistoryStateAction;
import edu.mit.scansite.shared.dispatch.history.StoreHistoryStateAction;
import edu.mit.scansite.shared.dispatch.motif.HistogramCreateAction;
import edu.mit.scansite.shared.dispatch.motif.HistogramRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.HistogramUpdateAction;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifGroupRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.LightWeightMotifRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifAddAction;
import edu.mit.scansite.shared.dispatch.motif.MotifDeleteAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGetAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupAddAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupDeleteAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifGroupUpdateAction;
import edu.mit.scansite.shared.dispatch.motif.MotifNumbersRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifRetrieverAction;
import edu.mit.scansite.shared.dispatch.motif.MotifUpdateAction;
import edu.mit.scansite.shared.dispatch.news.NewsAddAction;
import edu.mit.scansite.shared.dispatch.news.NewsDeleteAction;
import edu.mit.scansite.shared.dispatch.news.NewsRetrieverAction;
import edu.mit.scansite.shared.dispatch.news.NewsUpdateAction;
import edu.mit.scansite.shared.dispatch.user.LoginAction;
import edu.mit.scansite.shared.dispatch.user.UserAddAction;
import edu.mit.scansite.shared.dispatch.user.UserDeleteAction;
import edu.mit.scansite.shared.dispatch.user.UserPrivilegesAction;
import edu.mit.scansite.shared.dispatch.user.UserRetrieverAction;
import edu.mit.scansite.shared.dispatch.user.UserUpdateAction;

/**
 * @author tobieh
 * @author Konstantin Krismer
 */
public class ActionsModule extends ActionHandlerModule {
	@Override
	protected void configureHandlers() {
		// news related servlet definitions
		bindHandler(NewsRetrieverAction.class, NewsRetrieverHandler.class);
		bindHandler(NewsDeleteAction.class, NewsDeleteHandler.class);
		bindHandler(NewsUpdateAction.class, NewsUpdateHandler.class);
		bindHandler(NewsAddAction.class, NewsAddHandler.class);

		// user related servlet definitions
		bindHandler(LoginAction.class, LoginHandler.class);
		bindHandler(UserPrivilegesAction.class, UserPrivilegesGetHandler.class);
		bindHandler(UserAddAction.class, UserAddHandler.class);
		bindHandler(UserDeleteAction.class, UserDeleteHandler.class);
		bindHandler(UserUpdateAction.class, UserUpdateHandler.class);
		bindHandler(UserRetrieverAction.class, UserRetrieverHandler.class);

		// motifgroup related servlet definitions
		bindHandler(MotifGroupAddAction.class, MotifGroupAddHandler.class);
		bindHandler(MotifGroupUpdateAction.class, MotifGroupUpdateHandler.class);
		bindHandler(MotifGroupDeleteAction.class, MotifGroupDeleteHandler.class);
		bindHandler(MotifGroupRetrieverAction.class,
				MotifGroupRetrieverHandler.class);
		bindHandler(LightWeightMotifGroupRetrieverAction.class,
				LightWeightMotifGroupRetrieverHandler.class);

		// motif and histogram related servlet definitions
		bindHandler(LightWeightMotifRetrieverAction.class,
				LightWeightMotifRetrieverHandler.class);
		bindHandler(MotifAddAction.class, MotifAddHandler.class);
		bindHandler(MotifDeleteAction.class, MotifDeleteHandler.class);
		bindHandler(MotifRetrieverAction.class, MotifRetrieverHandler.class);
		bindHandler(MotifUpdateAction.class, MotifUpdateHandler.class);
		bindHandler(HistogramCreateAction.class, HistogramCreateHandler.class);
		bindHandler(HistogramUpdateAction.class, HistogramUpdateHandler.class);
		bindHandler(HistogramRetrieverAction.class,
				HistogramRetrieverHandler.class);
		bindHandler(MotifNumbersRetrieverAction.class,
				MotifNumbersRetrieverHandler.class);
		bindHandler(MotifGetAction.class, MotifGetHandler.class);

		// data source related servlet definitions
		bindHandler(DataSourcesRetrieverAction.class,
				DataSourcesRetrieverHandler.class);
		bindHandler(DataSourceSizesRetrieverAction.class,
				DataSourceSizesRetrieverHandler.class);
		bindHandler(DataSourceTypesRetrieverAction.class,
				DataSourceTypesRetrieverHandler.class);

		// feature related servlet definitions
		bindHandler(ProteinCheckAction.class, ProteinCheckHandler.class);
		bindHandler(ProteinCheckOracleAction.class,
				ProteinCheckOracleHandler.class);
		bindHandler(GetNewProteinPlotAction.class,
				GetNewProteinPlotHandler.class);
		bindHandler(ProteinScanAction.class, ProteinScanHandler.class);
		bindHandler(SequenceMatchAction.class, SequenceMatchHandler.class);
		bindHandler(OrthologScanSequencePatternAction.class,
				OrthologScanSequencePatternHandler.class);
		bindHandler(OrthologScanMotifAction.class,
				OrthologScanMotifHandler.class);
		bindHandler(DatabaseScanAction.class, DatabaseScanHandler.class);
		bindHandler(DbSearchHistogramRetrieverAction.class,
				DbSearchHistogramRetrieverHandler.class);
		bindHandler(UtilitiesMwAndPiAction.class, UtilitiesMwAndPiHandler.class);
		bindHandler(SiteInSequenceHtmlGetAction.class,
				SiteInSequenceHtmlGetHandler.class);
		bindHandler(ShowSequenceMatchesHtmlGetAction.class,
				ShowSequenceMatchesHtmlGetHandler.class);
		bindHandler(ShowMotifsForExpectedSiteGetAction.class,
				ShowMotifsForExpectedSiteGetHandler.class);
		bindHandler(ProteinRetrieverAction.class, ProteinRetrieverHandler.class);
		bindHandler(DomainPositionsGetAction.class,
				DomainPositionsGetHandler.class);
		bindHandler(IdentifierTypeRetrieverAction.class,
				IdentifierTypeRetrieverHandler.class);
		bindHandler(OrthologyCheckOracleAction.class,
				OrthologyCheckOracleHandler.class);
		bindHandler(PredictProteinsLocalizationAction.class,
				PredictProteinsLocalizationHandler.class);
		bindHandler(PredictMotifsLocalizationAction.class,
				PredictMotifsLocalizationHandler.class);
		bindHandler(PredictLocalizationMotifProteinPairAction.class,
				PredictLocalizationMotifProteinPairHandler.class);

		// // history related servlet definitions
		bindHandler(StoreHistoryStateAction.class,
				StoreHistoryStateHandler.class);
		bindHandler(RetrieveHistoryStateAction.class,
				RetrieveHistoryStateHandler.class);
	}
}