package edu.mit.scansite.client.ui.presenter.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import edu.mit.scansite.client.ui.event.EventBus;
import edu.mit.scansite.client.ui.event.MessageEvent;
import edu.mit.scansite.client.ui.event.MessageEvent.MessageEventPriority;
import edu.mit.scansite.client.ui.presenter.Presenter;
import edu.mit.scansite.client.ui.view.admin.UserMgmtPageView;
import edu.mit.scansite.shared.dispatch.user.UserAddAction;
import edu.mit.scansite.shared.dispatch.user.UserDeleteAction;
import edu.mit.scansite.shared.dispatch.user.UserRetrieverAction;
import edu.mit.scansite.shared.dispatch.user.UserRetrieverResult;
import edu.mit.scansite.shared.dispatch.user.UserUpdateAction;
import edu.mit.scansite.shared.transferobjects.User;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserMgmtPagePresenter extends Presenter implements
		UserMgmtPageView.Presenter {
	private UserMgmtPageView view;

	public UserMgmtPagePresenter(UserMgmtPageView view) {
		this.view = view;
	}

	@Override
	public void bind() {
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		retrieveUsers();
	}

	private void retrieveUsers() {
		dispatch.execute(new UserRetrieverAction(),
				new AsyncCallback<UserRetrieverResult>() {
					public void onFailure(Throwable caught) {
						EventBus.instance()
								.fireEvent(
										new MessageEvent(
												MessageEventPriority.ERROR,
												"Unable to retrieve users from database",
												this.getClass().toString(),
												caught));
					}

					public void onSuccess(UserRetrieverResult result) {
						if (result.isSuccess()) {
							view.displayUserList(result.getUsers());
						} else {
							EventBus.instance()
									.fireEvent(
											new MessageEvent(
													MessageEventPriority.ERROR,
													"Unable to retrieve users from database",
													this.getClass().toString(),
													null));
						}
					}
				});
	}

	@Override
	public void onAddButtonClicked(User user) {
		dispatch.execute(new UserAddAction(user),
				new AsyncCallback<UserRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										caught.getMessage(), this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(UserRetrieverResult result) {
						if (result.isSuccess()) {
							EventBus.instance().fireEvent(
									new MessageEvent(MessageEventPriority.INFO,
											"User successfully added", this
													.getClass().toString(),
											null));
							view.resetInputFields();
							view.displayUserList(result.getUsers());
						} else {
							EventBus.instance().fireEvent(
									new MessageEvent(
											MessageEventPriority.ERROR,
											"Server-side error", this
													.getClass().toString(),
											null));
						}
					}
				});
	}

	@Override
	public void onUpdateButtonClicked(User user) {
		dispatch.execute(new UserUpdateAction(user, !user.getPassword()
				.isEmpty()), new AsyncCallback<UserRetrieverResult>() {
			@Override
			public void onFailure(Throwable caught) {
				EventBus.instance().fireEvent(
						new MessageEvent(MessageEventPriority.ERROR, caught
								.getMessage(), this.getClass().toString(),
								caught));
			}

			@Override
			public void onSuccess(UserRetrieverResult result) {
				if (result.isSuccess()) {
					EventBus.instance().fireEvent(
							new MessageEvent(MessageEventPriority.INFO,
									"User successfully updated", this
											.getClass().toString(), null));
					view.resetInputFields();
					view.displayUserList(result.getUsers());
				} else {
					EventBus.instance().fireEvent(
							new MessageEvent(MessageEventPriority.ERROR,
									"Server-side error", this.getClass()
											.toString(), null));
				}
			}
		});
	}

	@Override
	public void onDeleteButtonClicked(User user) {
		dispatch.execute(new UserDeleteAction(user.getEmail()),
				new AsyncCallback<UserRetrieverResult>() {
					@Override
					public void onFailure(Throwable caught) {
						EventBus.instance().fireEvent(
								new MessageEvent(MessageEventPriority.ERROR,
										caught.getMessage(), this.getClass()
												.toString(), caught));
					}

					@Override
					public void onSuccess(UserRetrieverResult result) {
						if (result.isSuccess()) {
							EventBus.instance().fireEvent(
									new MessageEvent(MessageEventPriority.INFO,
											"User successfully deleted", this
													.getClass().toString(),
											null));
							view.resetInputFields();
							view.displayUserList(result.getUsers());
						} else {
							EventBus.instance().fireEvent(
									new MessageEvent(
											MessageEventPriority.ERROR,
											"Server-side error", this
													.getClass().toString(),
											null));
						}
					}
				});
	}
}
