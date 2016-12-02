package edu.mit.scansite.client.ui.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class NavigationEvent extends GwtEvent<NavigationEventHandler> {
	public static Type<NavigationEventHandler> TYPE = new Type<NavigationEventHandler>();

	public enum PageId {
		HOME("home"), NEWS("news"), FEATURE_SCAN_PROTEIN("scanProtein"), FEATURE_SCAN_PROTEIN_RESULT(
				"scanProteinResult"), FEATURE_SCAN_DB("scanDb"), FEATURE_SCAN_DB_RESULT(
				"scanDbResult"), FEATURE_SCAN_SEQ("scanSeq"), FEATURE_SCAN_SEQ_RESULT(
				"scanSeqResult"), FEATURE_SCAN_ORTHOLOGS("scanOrthologs"), FEATURE_SCAN_ORTHOLOGS_RESULT(
				"scanOrthologsResult"), FEATURE_PREDICT_LOCALIZATION(
				"predictLocalization"), FEATURE_PREDICT_LOCALIZATION_RESULT(
				"predictLocalizationResult"), FEATURE_CALC_MOLWEIGHT(
				"calcMolWeight"), FEATURE_CALC_COMPOSITION("calcComposition"), MOTIFS(
				"motifs"), TUTORIAL("tutorial"), ABOUT("about"), FAQ("faq"), CITE(
				"cite"), FEEDBACK("feedback"), ADMIN("admin"), ADMIN_MOTIF(
				"motifMgmt"), ADMIN_MOTIF_GROUP("motifGroupMgmt"), ADMIN_NEWS(
				"newsMgmt"), ADMIN_USER("userMgmt");

		String id;

		PageId(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}

		@Override
		public String toString() {
			return id;
		}
	};

	private final PageId navigationTargetId;

	public NavigationEvent(PageId navigationId) {
		this.navigationTargetId = navigationId;
	}

	public PageId getNavigationTargetId() {
		return navigationTargetId;
	}

	@Override
	public Type<NavigationEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NavigationEventHandler handler) {
		handler.onNavigationEvent(this);
	}
}
