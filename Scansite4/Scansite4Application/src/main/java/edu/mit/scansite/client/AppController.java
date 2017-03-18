package edu.mit.scansite.client;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import net.customware.gwt.dispatch.client.DefaultExceptionHandler;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.client.standard.StandardDispatchAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.logging.client.SimpleRemoteLogHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.DatabaseScanEvent;
import edu.mit.scansite.client.ui.event.DatabaseScanEventHandler;
import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.LoginEvent;
import edu.mit.scansite.client.ui.event.LoginEventHandler;
import edu.mit.scansite.client.ui.event.LogoutEvent;
import edu.mit.scansite.client.ui.event.LogoutEventHandler;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.event.MessageEventHandler;
import edu.mit.scansite.client.ui.event.NavigationEvent;
import edu.mit.scansite.client.ui.event.OrthologScanMotifEvent;
import edu.mit.scansite.client.ui.event.OrthologScanMotifEventHandler;
import edu.mit.scansite.client.ui.event.OrthologScanSequencePatternEvent;
import edu.mit.scansite.client.ui.event.OrthologScanSequencePatternEventHandler;
import edu.mit.scansite.client.ui.event.PredictLocalizationEvent;
import edu.mit.scansite.client.ui.event.PredictLocalizationEventHandler;
import edu.mit.scansite.client.ui.event.ProteinScanEvent;
import edu.mit.scansite.client.ui.event.ProteinScanEventHandler;
import edu.mit.scansite.client.ui.event.SequenceMatchEvent;
import edu.mit.scansite.client.ui.event.SequenceMatchEventHandler;
import edu.mit.scansite.client.ui.presenter.BreadcrumbsPresenter;
import edu.mit.scansite.client.ui.presenter.NavigationBarPresenter;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.presenter.PresenterFactory;
import edu.mit.scansite.client.ui.view.BreadcrumbsView;
import edu.mit.scansite.client.ui.view.NavigationBarView;
import edu.mit.scansite.shared.dispatch.features.DatabaseScanResult;
import edu.mit.scansite.shared.dispatch.features.OrthologScanMotifResult;
import edu.mit.scansite.shared.dispatch.features.OrthologScanSequencePatternResult;
import edu.mit.scansite.shared.dispatch.features.PredictLocalizationResult;
import edu.mit.scansite.shared.dispatch.features.ProteinScanResult;
import edu.mit.scansite.shared.dispatch.features.SequenceMatchResult;
import edu.mit.scansite.shared.dispatch.history.RetrieveHistoryStateAction;
import edu.mit.scansite.shared.dispatch.history.RetrieveHistoryStateResult;
import edu.mit.scansite.shared.dispatch.user.LoginAction;
import edu.mit.scansite.shared.dispatch.user.LoginResult;
import edu.mit.scansite.shared.transferobjects.User;
import edu.mit.scansite.shared.transferobjects.states.State;
import edu.mit.scansite.shared.util.Formatter;

/**
 * @author Konstantin Krismer
 */
public class AppController implements ValueChangeHandler<String> {
	protected static final String COOKIE_NAME = "scansitesid";
	private final int COOKIE_LIFETIME = 1000 * 60 * 60 * 24 * 5; // 5 days
	private final DispatchAsync dispatch = new StandardDispatchAsync(
			new DefaultExceptionHandler());

	private User user = null;

	private HasWidgets container;
	private NavigationBarPresenter navigationBarPresenter;
	private NavigationBarView navigationBarView;
	private BreadcrumbsPresenter breadcrumbsPresenter;

	public AppController(HasWidgets navigationBarContainer,
			HasWidgets breadcrumbsContainer) {
		bind();
		doLogin(null, null);
		initNavigationBar(navigationBarContainer);
		initPagePath(breadcrumbsContainer);
	}

	private void initPagePath(HasWidgets breadcrumbsContainer) {
		breadcrumbsPresenter = new BreadcrumbsPresenter(new BreadcrumbsView());
		breadcrumbsPresenter.bind();
		breadcrumbsPresenter.go(breadcrumbsContainer);
	}

	private void initNavigationBar(HasWidgets navigationBarContainer) {
		navigationBarView = new NavigationBarView();
		navigationBarPresenter = new NavigationBarPresenter(navigationBarView);
		navigationBarPresenter.bind();
		navigationBarPresenter.go(navigationBarContainer);
	}

	private void bind() {
		History.addValueChangeHandler(this);

		EventBus.instance().addHandler(MessageEvent.TYPE,
				new MessageEventHandler() {
					@Override
					public void onMessageEvent(MessageEvent event) {
						SimpleRemoteLogHandler remoteLog = new SimpleRemoteLogHandler();
						Level level;
						switch (event.getPriority()) {
						case ERROR:
							level = Level.SEVERE;
							break;
						case WARNING:
							level = Level.WARNING;
							break;
						case INFO:
							level = Level.INFO;
							break;
						default:
							level = Level.WARNING;
						}
						if (event.getThrowable() != null) {
							remoteLog.publish(new LogRecord(level, event
									.getSource()
									+ ": "
									+ event.getMessage()
									+ " ("
									+ event.getThrowable().getMessage()
									+ ")"));
						} else {
							remoteLog.publish(new LogRecord(level, event
									.getSource() + ": " + event.getMessage()));
						}
						remoteLog.close();
					}
				});

		EventBus.instance().addHandler(ProteinScanEvent.TYPE,
				new ProteinScanEventHandler() {
					@Override
					public void onProteinScanEvent(ProteinScanResult scanResult) {
						setPresenter(PresenterFactory.instance()
								.getScanProteinResultPagePresenter(scanResult,
										navigationBarPresenter,
										breadcrumbsPresenter, user));
					}
				});

		EventBus.instance().addHandler(DatabaseScanEvent.TYPE,
				new DatabaseScanEventHandler() {
					@Override
					public void onDatabaseScanEvent(
							DatabaseScanResult scanResult) {
						setPresenter(PresenterFactory.instance()
								.getScanDatabaseResultPagePresenter(scanResult,
										navigationBarPresenter,
										breadcrumbsPresenter, user));
					}
				});

		EventBus.instance().addHandler(SequenceMatchEvent.TYPE,
				new SequenceMatchEventHandler() {
					@Override
					public void onSequenceMatchEvent(
							SequenceMatchResult scanResult) {
						setPresenter(PresenterFactory.instance()
								.getSequenceMatchResultPagePresenter(
										scanResult, navigationBarPresenter,
										breadcrumbsPresenter, user));
					}
				});

		EventBus.instance().addHandler(OrthologScanMotifEvent.TYPE,
				new OrthologScanMotifEventHandler() {
					@Override
					public void onOrthologScanMotifEvent(
							OrthologScanMotifResult scanResult) {
						setPresenter(PresenterFactory.instance()
								.getScanOrthologsResultPagePresenter(
										scanResult, navigationBarPresenter,
										breadcrumbsPresenter));
					}
				});

		EventBus.instance().addHandler(OrthologScanSequencePatternEvent.TYPE,
				new OrthologScanSequencePatternEventHandler() {
					@Override
					public void onOrthologScanSequencePatternEvent(
							OrthologScanSequencePatternResult scanResult) {
						setPresenter(PresenterFactory.instance()
								.getScanOrthologsResultPagePresenter(
										scanResult, navigationBarPresenter,
										breadcrumbsPresenter));
					}
				});

		EventBus.instance().addHandler(PredictLocalizationEvent.TYPE,
				new PredictLocalizationEventHandler() {
					@Override
					public void onPredictLocalizationEvent(
							PredictLocalizationResult scanResult) {
						setPresenter(PresenterFactory.instance()
								.getPredictLocalizationResultPagePresenter(
										scanResult, navigationBarPresenter,
										breadcrumbsPresenter));
					}
				});

		EventBus.instance().addHandler(LoginEvent.TYPE,
				new LoginEventHandler() {
					@Override
					public void onLoginEvent(String email, String password) {
						Formatter f = new Formatter();
						email = f.formatUsername(email);
						password = f.formatPassword(password);

						boolean inputOk = true;

						if (inputOk) {
							doLogin(email, password);
						}
					}
				});
		EventBus.instance().addHandler(LogoutEvent.TYPE,
				new LogoutEventHandler() {
					@Override
					public void onLogoutEvent() {
						doLogout();
					}
				});
	}

	protected void doLogout() {
		// in case removing cookie does not work, set it invalid and expired
		// first:
		Cookies.setCookie(COOKIE_NAME, "loggedOut", new Date(10), null, "/",
				false);
		Cookies.removeCookie(COOKIE_NAME, "/");
		if (Cookies.getCookie(COOKIE_NAME) != null) {
			String path = GWT.getModuleBaseURL();
			path = path.substring(0, path.length() - 1);
			int index = path.lastIndexOf('/');
			path = path.substring(index);
			Cookies.setCookie(COOKIE_NAME, "", new Date(10), null, path, false);
		}
		user = null;
		History.fireCurrentHistoryState();
		navigationBarView.showAdminArea(user);
	}

	private String getUserSessionIdFromCookie() {
		return Cookies.getCookie(COOKIE_NAME);
	}

	protected void doLogin(String email, String password) {
		final boolean onClick = email != null && password != null;
		LoginAction loginAction;
		String sessionId = getUserSessionIdFromCookie();
		if (sessionId == null && !onClick) { // no cookie and no click for login
												// (--> first page load without
												// cookie)
			return;
		} else if (sessionId == null) { // no cookie, but login clicked
			loginAction = new LoginAction(email, password);
		} else { // login via cookie
			loginAction = new LoginAction(sessionId);
		}
		dispatch.execute(loginAction, new AsyncCallback<LoginResult>() {
			@Override
			public void onFailure(Throwable e) {
				EventBus.instance().fireEvent(
						new MessageEvent(MessageEventPriority.INFO,
								"Login failed! Can't reach server!", this
										.getClass().toString(), e));
			}

			@Override
			public void onSuccess(LoginResult result) {
				if (result.isLoginSuccessful()) {
					Date expires = new Date(System.currentTimeMillis()
							+ COOKIE_LIFETIME);
					Cookies.setCookie(COOKIE_NAME, result.getUser()
							.getSessionId(), expires, null, "/", false);
					user = result.getUser();
					navigationBarView.showAdminArea(user);
					History.fireCurrentHistoryState();
					// HasWidgets adminPanel = display.getAdminPanel();
					// adminPanel.clear();
					// AdminFeatureListPresenter adminFeatureList = new
					// AdminFeatureListPresenter(
					// new AdminFeatureListView(), eventBus, result
					// .isAdmin());
					// adminFeatureList.bind();
					//
					// userEmailLabel = display.getUserEmailLabel();
					// userEmailLabel.setText(userEmail);
					// adminPanel.add(userEmailLabel);
					// adminPanel.add(adminFeatureList.getWidget());
					// Button loginButton = display.getLoginButton();
					// loginButton.setText("Logout");
					// loginButtonHandlerRegistration.removeHandler();
					// loginButtonHandlerRegistration = loginButton
					// .addClickHandler(new ClickHandler() {
					// @Override
					// public void onClick(ClickEvent event) {
					// eventBus.fireEvent(new LogoutEvent());
					// }
					// });
					//
					// display.getUserEmailTextBox().setText("");
					// display.getUserPasswordTextBox().setText("");
				} else {
					if (onClick) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										"E-mail or password were incorrect",
										this.getClass().toString(), null));
						// display.getUserPasswordTextBox().setText("");
					}
				}
			}
		});
	}

	public void setPresenter(Presenter presenter) {
		if (presenter != null) {
			presenter.bind();
			presenter.go(container);
		}
	}

	public void go(final HasWidgets container) {
		this.container = container;

		if (History.getToken().isEmpty()) {
			History.newItem(NavigationEvent.PageId.HOME.getId(), true);
		} else {
			History.fireCurrentHistoryState();
		}
	}

	// onHistoryTokenChanged
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		final String token = event.getValue();

		if (token != null) {
			// GWT.runAsync(new RunAsyncCallback() {
			// public void onFailure(Throwable caught) {
			// }
			//
			// public void onSuccess() {
			// boolean userIsLoggedIn = navBar.isUserLoggedIn();
			// String userSessionId = navBar.getUserSessionId();
			// String email = navBar.getEmail();

			final HistoryToken histToken = new HistoryToken(token);
			if (histToken.getParameters().containsKey("s")) {
				dispatch.execute(new RetrieveHistoryStateAction(histToken
						.getParameters().get("s")),
						new AsyncCallback<RetrieveHistoryStateResult>() {
							@Override
							public void onFailure(Throwable caught) {
								SimpleRemoteLogHandler remoteLog = new SimpleRemoteLogHandler();
								remoteLog.publish(new LogRecord(Level.SEVERE,
										caught.getMessage()));
								remoteLog.close();
							}

							@Override
							public void onSuccess(
									RetrieveHistoryStateResult result) {
								PresenterFactory presenterFactory = PresenterFactory
										.instance();
								State state = null;
								if (result.isSuccess()) {
									state = result.getSessionData();
								} else {
									SimpleRemoteLogHandler remoteLog = new SimpleRemoteLogHandler();
									remoteLog.publish(new LogRecord(
											Level.WARNING, result
													.getFailureMessage()));
									remoteLog.close();
								}
								Presenter presenter = presenterFactory
										.getPresenter(histToken,
												navigationBarPresenter,
												breadcrumbsPresenter, user,
												state);
								setPresenter(presenter);
							}
						});

			} else {
				Presenter presenter = null;
				PresenterFactory presenterFactory = PresenterFactory.instance();
				presenter = presenterFactory.getPresenter(histToken,
						navigationBarPresenter, breadcrumbsPresenter, user,
						null);
				setPresenter(presenter);
			}

			// }
			// });
		}
	}

	/**
	 * @return The logged-in user's session id or NULL if no user is logged in.
	 */
	public String getUserSessionId() {
		return getUserSessionIdFromCookie();
	}
}
