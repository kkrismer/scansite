package edu.mit.scansite;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.ui.RootPanel;

import edu.mit.scansite.client.AppController;
import edu.mit.scansite.shared.event.EventBus;
import edu.mit.scansite.shared.event.MessageEvent;
import edu.mit.scansite.shared.event.MessageEvent.MessageEventPriority;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint {
	private static final String HTML_CONTENT_ID = "content";
	private static final String HTML_NAVIGATION_ID = "majornav";
	private static final String HTML_BREADCRUMBS_ID = "breadcrumbs";

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				EventBus.instance().fireEvent(
						new MessageEvent(MessageEventPriority.ERROR,
								"severe server side error occurred", this
										.getClass().toString(), unwrap(e)));
			}

			public Throwable unwrap(Throwable e) {
				if (e instanceof UmbrellaException) {
					UmbrellaException ue = (UmbrellaException) e;
					if (ue.getCauses().size() == 1) {
						return unwrap(ue.getCauses().iterator().next());
					}
				}
				return e;
			}
		});

		AppController appViewer = new AppController(
				RootPanel.get(HTML_NAVIGATION_ID),
				RootPanel.get(HTML_BREADCRUMBS_ID));
		appViewer.go(RootPanel.get(HTML_CONTENT_ID));
	}
}
